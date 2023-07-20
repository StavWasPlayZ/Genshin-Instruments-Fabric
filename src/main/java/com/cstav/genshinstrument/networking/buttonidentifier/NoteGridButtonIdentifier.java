package com.cstav.genshinstrument.networking.buttonidentifier;

import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteGridButton;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;

public class NoteGridButtonIdentifier extends DefaultNoteButtonIdentifier {

    public final int row, column;
    
    @Environment(EnvType.CLIENT)
    public NoteGridButtonIdentifier(final NoteGridButton button) {
        super(button);
        this.row = button.row;
        this.column = button.column;
    }

    public NoteGridButtonIdentifier(FriendlyByteBuf buf) {
        super(buf);
        row = buf.readInt();
        column = buf.readInt();
    }
    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        super.writeToNetwork(buf);
        buf.writeInt(row);
        buf.writeInt(column);
    }


    @Override
    public boolean matches(NoteButtonIdentifier other) {
        return MatchType.perferMatch(other, this::gridMatch, super::matches);
    }
    private boolean gridMatch(final NoteGridButtonIdentifier other) {
        return (row == other.row) && (column == other.column);
    }

}