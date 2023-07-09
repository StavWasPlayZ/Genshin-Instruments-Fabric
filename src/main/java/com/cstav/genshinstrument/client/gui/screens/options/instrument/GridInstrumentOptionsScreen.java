package com.cstav.genshinstrument.client.gui.screens.options.instrument;

import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.config.enumType.label.NoteGridLabel;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractGridInstrumentScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GridInstrumentOptionsScreen extends AbstractInstrumentOptionsScreen {

    public GridInstrumentOptionsScreen(final AbstractGridInstrumentScreen screen) {
        super(screen);
    }


    @Override
    public NoteGridLabel[] getLabels() {
        return NoteGridLabel.values();
    }
    @Override
    public NoteGridLabel getCurrentLabel() {
        return ModClientConfigs.GRID_LABEL_TYPE.get();
    }
    
}
