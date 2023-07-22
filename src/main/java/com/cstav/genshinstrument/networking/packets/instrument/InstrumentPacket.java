package com.cstav.genshinstrument.networking.packets.instrument;

import com.cstav.genshinstrument.networking.ModPacket;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.sound.NoteSound;
import com.cstav.genshinstrument.util.ModEntityData;
import com.cstav.genshinstrument.util.ServerUtil;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class InstrumentPacket implements ModPacket {

    private final NoteSound sound;
    private final InteractionHand hand;
    private final int pitch;

    private final ResourceLocation instrumentId;
    private final NoteButtonIdentifier noteIdentifier;

    public InstrumentPacket(NoteSound sound, int pitch, InteractionHand hand,
            ResourceLocation instrumentId, NoteButtonIdentifier noteIdentifier) {
        this.sound = sound;
        this.hand = hand;
        this.pitch = pitch;
        
        this.instrumentId = instrumentId;
        this.noteIdentifier = noteIdentifier;
    }
    public InstrumentPacket(FriendlyByteBuf buf) {
        sound = NoteSound.readFromNetwork(buf);
        hand = buf.readEnum(InteractionHand.class);
        pitch = buf.readInt();
        
        instrumentId = buf.readResourceLocation();
        noteIdentifier = NoteButtonIdentifier.readIdentifier(buf);
    }

    @Override
    public void write(final FriendlyByteBuf buf) {
        sound.writeToNetwork(buf);
        buf.writeEnum(hand);
        buf.writeInt(pitch);

        buf.writeResourceLocation(instrumentId);
        noteIdentifier.writeToNetwork(buf);
    }


    @Override
    public void handle(Player player, PacketSender responseSender) {
        if (!ModEntityData.isInstrumentOpen(player))
            return;

            ServerUtil.sendPlayNotePackets((ServerPlayer)player, hand, sound, instrumentId, noteIdentifier, pitch);
    }
    
}