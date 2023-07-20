package com.cstav.genshinstrument.networking.buttonidentifier;

import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;

/**
 * The default note button identifier. Uses a button's {@link NoteSound} as an identifier.
 */
public class DefaultNoteButtonIdentifier extends NoteButtonIdentifier {

    private NoteSound sound;

    public DefaultNoteButtonIdentifier(final NoteSound sound) {
        this.sound = sound;
    }
    @Environment(EnvType.CLIENT)
    public DefaultNoteButtonIdentifier(final NoteButton note) {
        this(note.getSound());
    }

    public void setSound(NoteSound sound) {
        this.sound = sound;
    }


    public DefaultNoteButtonIdentifier(final FriendlyByteBuf buf) {
        sound = NoteSound.readFromNetwork(buf);
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        super.writeToNetwork(buf);
        sound.writeToNetwork(buf);
    }


    public boolean matches(NoteButtonIdentifier other) {
        return MatchType.forceMatch(other, this::matchSound);
    }
    private boolean matchSound(final DefaultNoteButtonIdentifier other) {
        return other.sound.equals(sound);
    }

}