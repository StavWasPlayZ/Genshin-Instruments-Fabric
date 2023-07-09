package com.cstav.genshinstrument.client.gui.screens.instrument.partial.note;

import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractGridInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.label.NoteLabelSupplier;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class NoteGridButton extends NoteButton {

    public final int row, column;

    public NoteGridButton(int row, int column, NoteSound sound, NoteLabelSupplier labelSupplier,
      AbstractGridInstrumentScreen instrumentScreen) {
        super(sound, labelSupplier, row, instrumentScreen.rows(), instrumentScreen);
        
        
        this.row = row;
        this.column = column;
    }
    
}
