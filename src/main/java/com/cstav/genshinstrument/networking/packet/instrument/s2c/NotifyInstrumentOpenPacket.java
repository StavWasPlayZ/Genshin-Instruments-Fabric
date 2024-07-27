package com.cstav.genshinstrument.networking.packet.instrument.s2c;

import java.util.Optional;
import java.util.UUID;

import com.cstav.genshinstrument.event.InstrumentOpenStateChangedEvent;
import com.cstav.genshinstrument.event.InstrumentOpenStateChangedEvent.InstrumentOpenStateChangedEventArgs;
import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.sound.held.HeldNoteSounds;
import com.cstav.genshinstrument.util.InstrumentEntityData;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/**
 * A S2C packet to update the {@code instrument open} state
 * of a certain player
 */
public class NotifyInstrumentOpenPacket implements IModPacket {

    private final UUID playerUUID;
    private final boolean isOpen;
    private final Optional<BlockPos> pos;
    private final Optional<InteractionHand> hand;

    /**
     * Constructs packet notifying of a closed instrument
     */
    public NotifyInstrumentOpenPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;

        this.isOpen = false;
        this.pos = Optional.empty();
        this.hand = Optional.empty();
    }
    /**
     * Constructs a {@link NotifyInstrumentOpenPacket} that notifies of an open state
     * with an optional instrument block position.
     */
    public NotifyInstrumentOpenPacket(UUID playerUUID, BlockPos pos) {
        this.playerUUID = playerUUID;

        this.isOpen = true;
        this.pos = Optional.of(pos);
        this.hand = Optional.empty();
    }
    /**
     * Constructs packet notifying of an open item instrument
     * at the specified hand
     */
    public NotifyInstrumentOpenPacket(UUID playerUUID, InteractionHand hand) {
        this.playerUUID = playerUUID;

        this.isOpen = true;
        this.pos = Optional.empty();
        this.hand = Optional.of(hand);
    }
    
    public NotifyInstrumentOpenPacket(final FriendlyByteBuf buf) {
        playerUUID = buf.readUUID();
        isOpen = buf.readBoolean();
        pos = buf.readOptional(FriendlyByteBuf::readBlockPos);
        hand = buf.readOptional((fbb) -> fbb.readEnum(InteractionHand.class));
    }
    
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeBoolean(isOpen);
        buf.writeOptional(pos, FriendlyByteBuf::writeBlockPos);
        buf.writeOptional(hand, FriendlyByteBuf::writeEnum);
    }


    @SuppressWarnings("resource")
    @Override
    public void handle(Player player, PacketSender responseSender) {
        final Player _player = Minecraft.getInstance().level.getPlayerByUUID(playerUUID);

        if (isOpen) {

            if (pos.isPresent()) // is block instrument
                InstrumentEntityData.setOpen(_player, pos.get());
            else
                InstrumentEntityData.setOpen(_player, hand.get());

        } else {
            InstrumentEntityData.setClosed(player);
        }

        InstrumentOpenStateChangedEvent.EVENT.invoker().triggered(
            new InstrumentOpenStateChangedEventArgs(isOpen, player, pos, hand)
        );
    }
    
}
