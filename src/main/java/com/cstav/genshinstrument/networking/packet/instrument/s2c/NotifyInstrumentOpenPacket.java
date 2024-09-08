package com.cstav.genshinstrument.networking.packet.instrument.s2c;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.networking.IModPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;

import java.util.Optional;
import java.util.UUID;

/**
 * A S2C packet to update the {@code instrument open} state
 * of a certain player
 */
public class NotifyInstrumentOpenPacket extends IModPacket {
    public static final String MOD_ID = GInstrumentMod.MODID;
    public static final StreamCodec<RegistryFriendlyByteBuf, NotifyInstrumentOpenPacket> CODEC = CustomPacketPayload.codec(
        NotifyInstrumentOpenPacket::write,
        NotifyInstrumentOpenPacket::new
    );

    public final UUID playerUUID;
    public final boolean isOpen;
    public final Optional<BlockPos> pos;
    public final Optional<InteractionHand> hand;

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
    
    public NotifyInstrumentOpenPacket(final RegistryFriendlyByteBuf buf) {
        playerUUID = buf.readUUID();
        isOpen = buf.readBoolean();
        pos = buf.readOptional(RegistryFriendlyByteBuf::readBlockPos);
        hand = buf.readOptional((fbb) -> fbb.readEnum(InteractionHand.class));
    }
    
    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeBoolean(isOpen);
        buf.writeOptional(pos, RegistryFriendlyByteBuf::writeBlockPos);
        buf.writeOptional(hand, FriendlyByteBuf::writeEnum);
    }
}
