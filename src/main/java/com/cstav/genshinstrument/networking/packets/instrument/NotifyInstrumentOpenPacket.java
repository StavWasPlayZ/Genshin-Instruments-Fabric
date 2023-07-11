package com.cstav.genshinstrument.networking.packets.instrument;

import java.util.UUID;

import com.cstav.genshinstrument.networking.ModPacket;
import com.cstav.genshinstrument.util.ModEntityData;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class NotifyInstrumentOpenPacket implements ModPacket {

    private final UUID playerUUID;
    private final boolean isOpen;
    public NotifyInstrumentOpenPacket(final UUID playerUUID, final boolean isOpen) {
        this.playerUUID = playerUUID;
        this.isOpen = isOpen;
    }
    public NotifyInstrumentOpenPacket(final FriendlyByteBuf buf) {
        playerUUID = buf.readUUID();
        isOpen = buf.readBoolean();
    }
    
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeBoolean(isOpen);
    }


    @Override
    public void handle(Player player, PacketSender responseSender) {
        ModEntityData.setInstrumentOpen(player.getLevel().getPlayerByUUID(playerUUID), isOpen);
    }
    
}
