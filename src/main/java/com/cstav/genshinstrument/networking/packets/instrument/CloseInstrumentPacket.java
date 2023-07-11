package com.cstav.genshinstrument.networking.packets.instrument;

import com.cstav.genshinstrument.networking.ModPacket;
import com.cstav.genshinstrument.networking.ModPacketHandler;
import com.cstav.genshinstrument.util.ModEntityData;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class CloseInstrumentPacket implements ModPacket {
    
    public CloseInstrumentPacket() {}
    public CloseInstrumentPacket(FriendlyByteBuf buf) {}


    @Override
    public void handle(Player player, PacketSender responseSender) {
        ModEntityData.setInstrumentOpen(player, false);

        for (final Player oPlayer : player.getLevel().players())
            ModPacketHandler.sendToClient(new NotifyInstrumentOpenPacket(player.getUUID(), false), (ServerPlayer)oPlayer);
    }
    
}