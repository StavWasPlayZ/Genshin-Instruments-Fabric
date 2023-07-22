package com.cstav.genshinstrument.networking.packets.instrument;

import java.util.Optional;
import java.util.UUID;

import com.cstav.genshinstrument.networking.ModPacket;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class PlayNotePacket implements ModPacket {
    public static final PacketType<PlayNotePacket> TYPE = ModPacket.type(PlayNotePacket.class);


    private final BlockPos blockPos;
    private final NoteSound sound;
    private final int pitch;
    private final ResourceLocation instrumentId;
    private final NoteButtonIdentifier noteIdentifier;
    
    private final Optional<UUID> playerUUID;
    private final Optional<InteractionHand> hand;

    public PlayNotePacket(BlockPos pos, NoteSound sound, int pitch, ResourceLocation instrumentId,
            NoteButtonIdentifier noteIdentifier, Optional<UUID> playerUUID, Optional<InteractionHand> hand) {

        this.blockPos = pos;
        this.sound = sound;
        this.pitch = pitch;
        this.instrumentId = instrumentId;
        this.noteIdentifier = noteIdentifier;

        this.playerUUID = playerUUID;
        this.hand = hand;
    }
    public PlayNotePacket(FriendlyByteBuf buf) {
        blockPos = buf.readBlockPos();
        sound = NoteSound.readFromNetwork(buf);
        pitch = buf.readInt();
        instrumentId = buf.readResourceLocation();
        noteIdentifier = NoteButtonIdentifier.readIdentifier(buf);

        playerUUID = buf.readOptional(FriendlyByteBuf::readUUID);
        hand = buf.readOptional((fbb) -> fbb.readEnum(InteractionHand.class));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        sound.writeToNetwork(buf);
        buf.writeInt(pitch);
        buf.writeResourceLocation(instrumentId);
        noteIdentifier.writeToNetwork(buf);

        buf.writeOptional(playerUUID, FriendlyByteBuf::writeUUID);
        buf.writeOptional(hand, FriendlyByteBuf::writeEnum);
    }


    @Override
    public void handle(Player player, PacketSender responseSender) {
        sound.playAtPos(
            pitch, playerUUID.orElse(null), hand.orElse(null),
            instrumentId, noteIdentifier, blockPos
        );
    }
}