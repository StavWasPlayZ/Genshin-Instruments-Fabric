package com.cstav.genshinstrument.networking.packet.instrument;

import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.networking.GIPacketHandler;
import com.cstav.genshinstrument.util.InstrumentEntityData;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class CloseInstrumentPacket implements IModPacket {

    public CloseInstrumentPacket() {}
    public CloseInstrumentPacket(FriendlyByteBuf buf) {}

    
    @Override
    public void handle(Player player, PacketSender responseSender) {
        InstrumentEntityData.setClosed(player);

        for (final Player oPlayer : player.level().players())
            GIPacketHandler.sendToClient(new NotifyInstrumentOpenPacket(player.getUUID()), (ServerPlayer)oPlayer);
    }
    
}