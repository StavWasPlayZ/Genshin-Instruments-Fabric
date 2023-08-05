package com.cstav.genshinstrument.client.gui.screens.instrument.vintagelyre;

import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractGridInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteGridButton;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteNotation;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.label.NoteLabelSupplier;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class VintageNoteButton extends NoteGridButton {

    public VintageNoteButton(int row, int column,
            NoteSound sound, NoteLabelSupplier labelSupplier, AbstractGridInstrumentScreen instrumentScreen) {
        super(row, column, sound, labelSupplier, instrumentScreen);
    }

    
    private boolean isDefaultFlat() {
        return (row == 6) || (row == 2) ||
            ((row == 1) && (column == 0)) || ((row == 5) && (column == 0));
    }

    @Override
    public NoteNotation getNotation() {
        return ModClientConfigs.ACCURATE_NOTES.get()
            ? super.getNotation()
            : isDefaultFlat() ? NoteNotation.FLAT : NoteNotation.NONE;
    }
    
}
