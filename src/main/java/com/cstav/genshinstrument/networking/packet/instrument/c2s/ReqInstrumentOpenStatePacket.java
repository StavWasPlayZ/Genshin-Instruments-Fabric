package com.cstav.genshinstrument.networking.packet.instrument.c2s;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.event.ServerEvents;
import com.cstav.genshinstrument.networking.IModPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.Context;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

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
        final ServerPlayer target = context.player();
        final Player player = target.level().getPlayerByUUID(uuid);

        // 'player' was reportedly null for some players.
        // This could stem from the NPCs mod used in issue #5 (Fabric),
        // where these "clients" don't actually hold a true ServerPlayer UUID.
        if (player == null)
            return;

        ServerEvents.notifyOpenStateToPlayer(player, target);
    }
}
