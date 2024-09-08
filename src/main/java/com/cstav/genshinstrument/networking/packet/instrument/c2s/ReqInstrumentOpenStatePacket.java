package com.cstav.genshinstrument.networking.packet.instrument.c2s;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.event.ServerEvents;
import com.cstav.genshinstrument.networking.IModPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.Context;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class ReqInstrumentOpenStatePacket extends IModPacket {
    public static final String MOD_ID = GInstrumentMod.MODID;
    public static final StreamCodec<RegistryFriendlyByteBuf, ReqInstrumentOpenStatePacket> CODEC = CustomPacketPayload.codec(
        ReqInstrumentOpenStatePacket::write,
        ReqInstrumentOpenStatePacket::new
    );

    private final UUID uuid;

    public ReqInstrumentOpenStatePacket(final UUID uuid) {
        this.uuid = uuid;
    }
    public ReqInstrumentOpenStatePacket(RegistryFriendlyByteBuf buf) {
        uuid = buf.readUUID();
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(uuid);
    }

    @Override
    public void handleServer(Context context) {
        final ServerPlayer player = context.player();
        ServerEvents.notifyOpenStateToPlayer(player.level().getPlayerByUUID(uuid), player);
    }
}
