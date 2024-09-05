package com.cstav.genshinstrument.networking.packet.instrument.c2s;

import com.cstav.genshinstrument.event.ServerEvents;
import com.cstav.genshinstrument.networking.IModPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class ReqInstrumentOpenStatePacket implements IModPacket {
    private final UUID uuid;

    public ReqInstrumentOpenStatePacket(final UUID uuid) {
        this.uuid = uuid;
    }
    public ReqInstrumentOpenStatePacket(FriendlyByteBuf buf) {
        uuid = buf.readUUID();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
    }

    @Override
    public void handle(Player player, PacketSender responseSender) {
        ServerEvents.notifyOpenStateToPlayer(player.level().getPlayerByUUID(uuid), (ServerPlayer) player);
    }
}
