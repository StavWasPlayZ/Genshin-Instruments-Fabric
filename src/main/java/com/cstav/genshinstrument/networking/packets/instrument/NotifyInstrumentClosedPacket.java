package com.cstav.genshinstrument.networking.packets.instrument;

import java.util.UUID;

import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.util.ModEntityData;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class NotifyInstrumentClosedPacket implements IModPacket {
    public static final PacketType<NotifyInstrumentClosedPacket> TYPE = IModPacket.type(NotifyInstrumentClosedPacket.class);


    private final UUID playerUUID;
    public NotifyInstrumentClosedPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }
    public NotifyInstrumentClosedPacket(final FriendlyByteBuf buf) {
        playerUUID = buf.readUUID();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
    }


    @SuppressWarnings("resource")
    @Override
    public void handle(Player player, PacketSender responseSender) {
        ModEntityData.setInstrumentClosed(Minecraft.getInstance().level.getPlayerByUUID(playerUUID));
    }
}
