package com.cstav.genshinstrument.networking.packet.instrument.c2s;

import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.networking.packet.instrument.util.InstrumentPacketUtil;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * A C2S packet for notifying the sever that the
 * client has closed their instrument screen
 */
public class CloseInstrumentPacket implements IModPacket {

    public CloseInstrumentPacket() {}
    public CloseInstrumentPacket(FriendlyByteBuf buf) {}


    @Override
    public void handle(Player player, PacketSender responseSender) {
        InstrumentPacketUtil.setInstrumentClosed((ServerPlayer) player);
    }

}