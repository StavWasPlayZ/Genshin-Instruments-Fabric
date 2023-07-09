package com.cstav.genshinstrument.networking.packets.instrument;

import java.util.UUID;

import com.cstav.genshinstrument.networking.ModPacket;
import com.cstav.genshinstrument.util.ModEntityData;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;

public class NotifyInstrumentOpenPacket implements ModPacket {
    public static final PacketType<NotifyInstrumentOpenPacket> TYPE = ModPacket.type(NotifyInstrumentOpenPacket.class);


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
    public void handle(LocalPlayer player, PacketSender responseSender) {
        ModEntityData.setInstrumentOpen(player.level().getPlayerByUUID(playerUUID), isOpen);
    }
    
}
