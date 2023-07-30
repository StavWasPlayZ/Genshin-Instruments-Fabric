package com.cstav.genshinstrument.client.gui.screens.instrument.partial.note;

import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractGridInstrumentScreen;
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

    @Override
    public NoteGridButtonIdentifier getIdentifier() {
        return new NoteGridButtonIdentifier(this);
    }

    @Override
    public NoteNotation getNotation() {
        return ModClientConfigs.ACCURATE_ACCIDENTALS.get()
            ? NoteNotation.getNotation(LabelUtil.getNoteName(this))
            : NoteNotation.NONE;
    }


    @Override
    protected NoteButtonRenderer initNoteRenderer() {
        return new NoteButtonRenderer(this,
            row, ((AbstractGridInstrumentScreen)instrumentScreen).rows(),
            57, .9f, 1.025f
        );
    }


    //NOTE: testing purposes only!
    @Override
    public String toString() {
        return "NoteGridButton[Position: (Row: "+row+", Column: "+column+")]";
    }

}