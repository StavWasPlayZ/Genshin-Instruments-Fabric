package com.cstav.genshinstrument.networking.packets.instrument;

import java.util.Optional;
import java.util.UUID;

import com.cstav.genshinstrument.networking.ModPacket;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class PlayNotePacket implements ModPacket {
    
    private final BlockPos blockPos;
    private final NoteSound sound;
    private final float pitch;
    private final ResourceLocation instrumentId;
    
    private final Optional<UUID> playerUUID;
    private final Optional<InteractionHand> hand;

    public PlayNotePacket(BlockPos pos, NoteSound sound, float pitch, ResourceLocation instrumentId,
      Optional<UUID> playerUUID, Optional<InteractionHand> hand) {
        this.blockPos = pos;
        this.sound = sound;
        this.pitch = pitch;
        this.instrumentId = instrumentId;

        this.playerUUID = playerUUID;
        this.hand = hand;
    }
    public PlayNotePacket(FriendlyByteBuf buf) {
        blockPos = buf.readBlockPos();
        sound = NoteSound.readFromNetwork(buf);
        pitch = buf.readFloat();
        instrumentId = buf.readResourceLocation();

        playerUUID = buf.readOptional(FriendlyByteBuf::readUUID);
        hand = buf.readOptional((fbb) -> fbb.readEnum(InteractionHand.class));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        sound.writeToNetwork(buf);
        buf.writeFloat(pitch);
        buf.writeResourceLocation(instrumentId);

        buf.writeOptional(playerUUID, FriendlyByteBuf::writeUUID);
        buf.writeOptional(hand, FriendlyByteBuf::writeEnum);
    }


    @Override
    public void handle(Player player, PacketSender responseSender) {
        sound.playAtPos(pitch, playerUUID.orElse(null), hand.orElse(null), instrumentId, blockPos);
    }
}