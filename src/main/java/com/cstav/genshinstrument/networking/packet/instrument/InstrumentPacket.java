package com.cstav.genshinstrument.networking.packet.instrument;

import java.util.Optional;

import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.networking.packet.INoteIdentifierSender;
import com.cstav.genshinstrument.sound.NoteSound;
import com.cstav.genshinstrument.util.InstrumentEntityData;
import com.cstav.genshinstrument.util.ServerUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class InstrumentPacket implements INoteIdentifierSender {

    private final NoteSound sound;
    private final Optional<InteractionHand> hand;
    private final int pitch;
    private final BlockPos pos;

    private final ResourceLocation instrumentId;
    private final NoteButtonIdentifier noteIdentifier;

    public InstrumentPacket(BlockPos pos, NoteSound sound, int pitch, Optional<InteractionHand> hand,
            ResourceLocation instrumentId, NoteButtonIdentifier noteIdentifier) {
        this.pos = pos;
        this.sound = sound;
        this.hand = hand;
        this.pitch = pitch;
        
        this.instrumentId = instrumentId;
        this.noteIdentifier = noteIdentifier;
    }
    @Environment(EnvType.CLIENT)
    public InstrumentPacket(final NoteButton noteButton, final BlockPos pos) {
        this(pos, noteButton.getSound(), noteButton.getPitch(),
            noteButton.instrumentScreen.interactionHand,
            noteButton.instrumentScreen.getInstrumentId(), noteButton.getIdentifier()
        );
    }

    public InstrumentPacket(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
        sound = NoteSound.readFromNetwork(buf);
        hand = buf.readOptional((fbb) -> buf.readEnum(InteractionHand.class));
        pitch = buf.readInt();
        
        instrumentId = buf.readResourceLocation();
        noteIdentifier = readNoteIdentifierFromNetwork(buf);
    }

    @Override
    public void write(final FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        sound.writeToNetwork(buf);
        buf.writeOptional(hand, FriendlyByteBuf::writeEnum);
        buf.writeInt(pitch);

        buf.writeResourceLocation(instrumentId);
        noteIdentifier.writeToNetwork(buf);
    }


    @Override
    public void handle(Player player, PacketSender responseSender) {
        if (!InstrumentEntityData.isOpen(player))
            return;

        ServerUtil.sendPlayNotePackets((ServerPlayer)player, pos, hand, sound, instrumentId, noteIdentifier, pitch);
    }
    
}