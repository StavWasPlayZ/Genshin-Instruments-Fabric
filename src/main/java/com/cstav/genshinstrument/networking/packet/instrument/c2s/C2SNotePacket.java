package com.cstav.genshinstrument.networking.packet.instrument.c2s;

import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.networking.packet.INoteIdentifierSender;
import com.cstav.genshinstrument.networking.packet.instrument.NoteSoundMetadata;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

/**
 * A generic instrument note packet,
 * notifying that a specific note should be played in the level
 * @param <T> The sound object type
 */
public abstract class C2SNotePacket<T> implements INoteIdentifierSender {

    protected final T sound;
    protected final NoteSoundMetadata meta;


    public C2SNotePacket(T sound, NoteSoundMetadata meta) {
        this.sound = sound;
        this.meta = meta;
    }
    @Environment(EnvType.CLIENT)
    public C2SNotePacket(NoteButton noteButton, T sound, int pitch) {
        this(sound, new NoteSoundMetadata(
            Minecraft.getInstance().player.blockPosition(),
            pitch,
            noteButton.instrumentScreen.volume,
            noteButton.instrumentScreen.getInstrumentId(),
            Optional.ofNullable(noteButton.getIdentifier())
        ));
    }

    public C2SNotePacket(FriendlyByteBuf buf) {
        sound = readSound(buf);
        meta = NoteSoundMetadata.read(buf, this);
    }
    @Override
    public void write(final FriendlyByteBuf buf) {
        writeSound(buf);
        meta.write(buf);
    }

    protected abstract T readSound(FriendlyByteBuf buf);
    protected abstract void writeSound(FriendlyByteBuf buf);


    @Override
    public void handle(Player player, PacketSender responseSender) {
        sendPlayNotePackets((ServerPlayer) player);
    }
    protected abstract void sendPlayNotePackets(final ServerPlayer player);
}