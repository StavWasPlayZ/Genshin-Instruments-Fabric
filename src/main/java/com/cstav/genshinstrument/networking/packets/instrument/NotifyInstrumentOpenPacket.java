package com.cstav.genshinstrument.networking.packets.instrument;

import java.util.Optional;
import java.util.UUID;

import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.util.InstrumentEntityData;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class NotifyInstrumentOpenPacket implements IModPacket {

    private final UUID playerUUID;
    private final boolean isOpen;
    private final Optional<BlockPos> pos;
    
    public NotifyInstrumentOpenPacket(UUID playerUUID, final boolean isOpen) {
        this.playerUUID = playerUUID;

        this.isOpen = isOpen;
        this.pos = Optional.empty();
    }
    /**
     * Constructs a {@link NotifyInstrumentOpenPacket} that notifies of an open state
     * with an optional instrument block position.
     */
    public NotifyInstrumentOpenPacket(UUID playerUUID, Optional<BlockPos> pos) {
        this.playerUUID = playerUUID;

        this.isOpen = true;
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
                InstrumentEntityData.setOpen(_player, pos.get());
            else
                InstrumentEntityData.setOpen(_player);

        } else
            InstrumentEntityData.setClosed(_player);
    }
    
}
