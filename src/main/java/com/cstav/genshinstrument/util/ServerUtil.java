package com.cstav.genshinstrument.util;

import com.cstav.genshinstrument.event.InstrumentPlayedEvent;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.ByPlayer.ByPlayerArgs;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.InstrumentPlayedEventArgs;
import com.cstav.genshinstrument.networking.ModPacketHandler;
import com.cstav.genshinstrument.networking.OpenInstrumentPacketSender;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.networking.packet.instrument.NotifyInstrumentOpenPacket;
import com.cstav.genshinstrument.networking.packet.instrument.OpenInstrumentPacket;
import com.cstav.genshinstrument.networking.packet.instrument.PlayNotePacket;
import com.cstav.genshinstrument.sound.NoteSound;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;

public class ServerUtil {
    public static final int PLAY_DISTANCE = 16;

    
    /**
     * Sends {@link PlayNotePacket}s in the specified {@link ServerUtil#PLAY_DISTANCE}.
     * This method treats the sound as it was produced by a player.
     * @param player The player producing the sounds
     * @param hand The hand of the player producing the sounds. Empty for when it was not a player.
     * @param sound The sound tp initiate
     * @param instrumentId The ID of the instrument initiating the sound
     * @param pitch The pitch of the sound to initiate
     * @param volume The volume of the sound to initiate
     */
    public static void sendPlayNotePackets(ServerPlayer player, Optional<InteractionHand> hand,
            NoteSound sound, ResourceLocation instrumentId, int pitch, int volume) {

        sendPlayNotePackets(
            player, Optional.empty(), hand,
            sound, instrumentId, null,
            pitch, volume,
            PlayNotePacket::new
        );
    }
    /**
     * Sends {@link PlayNotePacket}s in the specified {@link ServerUtil#PLAY_DISTANCE}.
     * This method treats the sound as it was produced by a player.
     * @param player The player producing the sounds
     * @param hand The hand of the player producing the sounds
     * @param sound The sound tp initiate
     * @param pitch The pitch of the sound to initiate
     * @param volume The volume of the sound to initiate
     * @param notePacketDelegate The initiator of the {@link PlayNotePacket} to be sent
     */
    public static void sendPlayNotePackets(ServerPlayer player, Optional<BlockPos> pos, Optional<InteractionHand> hand,
            NoteSound sound, ResourceLocation instrumentId, NoteButtonIdentifier noteIdentifier, int pitch, int volume,
            PlayNotePacketDelegate notePacketDelegate) {

        final PlayNotePacket packet = notePacketDelegate.create(
            pos, sound, pitch, volume,
            instrumentId, Optional.ofNullable(noteIdentifier),
            Optional.of(player.getUUID()), hand
        );

        final BlockPos playeredPos = CommonUtil.getPlayeredPosition(player, pos);

        for (final Player listener : noteListeners(player.getLevel(), playeredPos))
            ModPacketHandler.sendToClient(packet, (ServerPlayer)listener);

        // Trigger an instrument game event
        // This is done so that sculk sensors can pick up the instrument's sound
        player.getLevel().gameEvent(
            GameEvent.INSTRUMENT_PLAY, playeredPos,
            GameEvent.Context.of(player)
        );

        // Fire a player-specific event
        InstrumentPlayedEvent.ByPlayer.EVENT.invoker().triggered(
            new ByPlayerArgs(sound, pitch, volume, player, playeredPos, hand, instrumentId, noteIdentifier, false)
        );
    }

    /**
     * Sends {@link PlayNotePacket}s in the specified {@link ServerUtil#PLAY_DISTANCE}.
     * This method treats the sound as it was NOT produced by a player.
     * @param level The world that the sound should initiate in
     * @param pos The position of the sound to initiate
     * @param sound The sound to initiate
     * @param instrumentId The ID of the instrument initiating the sound
     * @param pitch The pitch of the sound to initiate
     */
    public static void sendPlayNotePackets(Level level, BlockPos pos, NoteSound sound, ResourceLocation instrumentId,
            int pitch, int volume) {

        sendPlayNotePackets(
            level, pos, sound,
            instrumentId, null,
            pitch, volume,
            PlayNotePacket::new
        );
    }
    /**
     * Sends {@link PlayNotePacket}s in the specified {@link ServerUtil#PLAY_DISTANCE}.
     * This method treats the sound as it was NOT produced by a player.
     * @param level The world that the sound should initiate in
     * @param pos The position of the sound to initiate
     * @param sound The sound to initiate
     * @param instrumentId The ID of the instrument initiating the sound
     * @param noteIdentifier The identifier of the note
     * @param pitch The pitch of the sound to initiate
     * @param volume The volume of the sound to initiate
     * @param notePacketDelegate The initiator of the {@link PlayNotePacket} to be sent
     */
    public static void sendPlayNotePackets(Level level, BlockPos pos, NoteSound sound,
            ResourceLocation instrumentId, NoteButtonIdentifier noteIdentifier, int pitch, int volume,
            PlayNotePacketDelegate notePacketDelegate) {

        final PlayNotePacket packet = notePacketDelegate.create(
            Optional.of(pos), sound, pitch, volume,
            instrumentId, Optional.ofNullable(noteIdentifier),
            Optional.empty(), Optional.empty()
        );

        for (final Player listener : noteListeners(level, pos))
            ModPacketHandler.sendToClient(packet, (ServerPlayer)listener);


        final BlockState bs = level.getBlockState(pos);
        // The sound may have been coming from a block
        if (bs != Blocks.AIR.defaultBlockState())
            level.gameEvent(
                GameEvent.INSTRUMENT_PLAY, pos,
                GameEvent.Context.of(bs)
            );
        // idk what else
        else
            level.gameEvent(null, GameEvent.INSTRUMENT_PLAY, pos);

        // Fire a generic instrument event
        InstrumentPlayedEvent.EVENT.invoker().triggered(
            new InstrumentPlayedEventArgs(sound, pitch, volume, level, pos, instrumentId, noteIdentifier, false)
        );
    }


    private static List<Player> noteListeners(Level level, BlockPos pos) {
        return CommonUtil.getPlayersInArea(level,
            new AABB(pos).inflate(PLAY_DISTANCE)
        );
    }


    public static void setInstrumentClosed(final Player player) {
        // Update the capability on server
        InstrumentEntityData.setClosed(player);

        // And clients
        player.getLevel().players().forEach((nearbyPlayer) ->
            ModPacketHandler.sendToClient(
                new NotifyInstrumentOpenPacket(player.getUUID(), false),
                (ServerPlayer)nearbyPlayer
            )
        );
    }


    /* ------------------ */


    /**
     * Gets a {@link NoteButtonIdentifier} as described by the {@code classType} destination.
     * Will only return a class type if it is valid and included in the {@code acceptableIdentifiers} list.
     * @param classType The class name of the requested identifiers
     * @param acceptableIdentifiers
     * 
     * @return The class of the requested identifier
     * @throws ClassNotFoundException If the requested class was not found in the provided {@code acceptableIdentifiers} list
     */
    public static Class<? extends NoteButtonIdentifier> getValidNoteIdentifier(String classType,
            List<Class<? extends NoteButtonIdentifier>> acceptableIdentifiers) throws ClassNotFoundException {

        for (final Class<? extends NoteButtonIdentifier> identifier : acceptableIdentifiers) {
            if (identifier.getName().equals(classType))
                return identifier;
        }

        throw new ClassNotFoundException("Class type "+classType+" could not be evaluated as part of the acceptable identifiers");
    }


    // Item/block stuff
    /**
     * Sends an instrument open packet as an item
     */
    public static boolean sendOpenPacket(ServerPlayer player, InteractionHand usedHand, OpenInstrumentPacketSender onOpenRequest) {
        return sendOpenPacket(player, usedHand, onOpenRequest, null);
    }
    /**
     * Sends an instrument open packet as a block
     */
    public static boolean sendOpenPacket(ServerPlayer player, OpenInstrumentPacketSender onOpenRequest, BlockPos pos) {
        return sendOpenPacket(player, null, onOpenRequest, pos);
    }
    private static boolean sendOpenPacket(ServerPlayer player, InteractionHand usedHand, OpenInstrumentPacketSender onOpenRequest,
            BlockPos pos) {

        // Update the capability on the server
        if (pos == null)
            InstrumentEntityData.setOpen(player);
        else
            InstrumentEntityData.setOpen(player, pos);

        // Notify the other players
        final Optional<BlockPos> playPos = Optional.ofNullable(pos);

        player.getLevel().players().forEach((otherPlayer) ->
            ModPacketHandler.sendToClient(
                new NotifyInstrumentOpenPacket(player.getUUID(), playPos),
                (ServerPlayer)otherPlayer
            )
        );

        // Send open packet after everyone is aware of the state
        onOpenRequest.send(player, usedHand);

        return true;
    }

    /**
     * @apiNote This method should only be used by the internal Genshin Instruments mod!
     */
    public static void sendInternalOpenPacket(ServerPlayer player, InteractionHand hand, String instrumentType) {
        ModPacketHandler.sendToClient(new OpenInstrumentPacket(instrumentType, hand), player);
    }

}
