package com.cstav.genshinstrument.client.gui.screens.instrument.partial.notegrid;

import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteButtonRenderer;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteNotation;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.label.NoteLabelSupplier;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteGridButtonIdentifier;
import com.cstav.genshinstrument.sound.NoteSound;
import com.cstav.genshinstrument.util.LabelUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class NoteGridButton extends NoteButton {

    public final int row, column;

    public NoteGridButton(int row, int column, NoteSound sound, NoteLabelSupplier labelSupplier,
            AbstractGridInstrumentScreen instrumentScreen) {
        super(sound, labelSupplier, instrumentScreen);

        this.row = row;
        this.column = column;
    }
    public NoteGridButton(int row, int column, NoteSound sound, NoteLabelSupplier labelSupplier,
            AbstractGridInstrumentScreen instrumentScreen, int pitch) {
        super(sound, labelSupplier, instrumentScreen, pitch);

        this.row = row;
        this.column = column;
    }

    @Override
    public NoteGridButtonIdentifier getIdentifier() {
        return new NoteGridButtonIdentifier(this);
    }

    @Override
    public NoteNotation getNotation() {
        return ModClientConfigs.ACCURATE_NOTES.get()
            ? NoteNotation.getNotation(LabelUtil.getNoteName(this))
            : NoteNotation.NONE;
    }


    @Override
    protected NoteButtonRenderer initNoteRenderer() {
        return new NoteButtonRenderer(this, row, LabelUtil.ABC.length);
    }

    @Override
    public void updateNoteLabel() {
        super.updateNoteLabel();
        noteRenderer.noteTextureRow = ModClientConfigs.ACCURATE_NOTES.get()
            ? LabelUtil.getABCOffset(this) : row;
    }

}