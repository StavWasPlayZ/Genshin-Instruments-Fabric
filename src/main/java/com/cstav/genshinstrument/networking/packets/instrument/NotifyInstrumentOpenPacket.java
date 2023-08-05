package com.cstav.genshinstrument.networking.packets.instrument;

import java.util.Optional;
import java.util.UUID;

import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.util.ModEntityData;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class NotifyInstrumentOpenPacket implements IModPacket {

    private final UUID playerUUID;
    private final boolean isOpen;
    private final Optional<BlockPos> pos;
    
    public NotifyInstrumentOpenPacket(UUID playerUUID, boolean isOpen, Optional<BlockPos> pos) {
        this.playerUUID = playerUUID;
        this.isOpen = isOpen;
        this.pos = pos;
    }
    public NotifyInstrumentOpenPacket(final FriendlyByteBuf buf) {
        playerUUID = buf.readUUID();
        isOpen = buf.readBoolean();
        pos = buf.readOptional(FriendlyByteBuf::readBlockPos);
    }
    
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeBoolean(isOpen);
        buf.writeOptional(pos, FriendlyByteBuf::writeBlockPos);
    }


    @SuppressWarnings("resource")
    @Override
    public void handle(Player player, PacketSender responseSender) {
        final Player _player = Minecraft.getInstance().level.getPlayerByUUID(playerUUID);

        if (isOpen) {

            if (pos.isPresent())
                ModEntityData.setInstrumentOpen(_player, pos.get());
            else
                ModEntityData.setInstrumentOpen(_player);

        } else
            ModEntityData.setInstrumentClosed(_player);
    }
    
}
