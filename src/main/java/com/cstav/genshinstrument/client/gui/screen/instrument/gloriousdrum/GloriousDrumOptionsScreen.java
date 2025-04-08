package com.cstav.genshinstrument.client.gui.screen.instrument.gloriousdrum;

import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.config.enumType.label.GloriousDrumNoteLabel;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.label.INoteLabel;
import com.cstav.genshinstrument.client.gui.screen.options.instrument.partial.InstrumentOptionsScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GloriousDrumOptionsScreen extends InstrumentOptionsScreen {

    public GloriousDrumOptionsScreen(AratakisGreatAndGloriousDrumScreen screen) {
        super(screen);
    }


    @Override
    protected void saveLabel(INoteLabel newLabel) {
        if (newLabel instanceof GloriousDrumNoteLabel label) {
            ModClientConfigs.GLORIOUS_DRUM_LABEL_TYPE.set(label);
        }
    }

    @Override
    public INoteLabel[] getLabels() {
        return GloriousDrumNoteLabel.availableVals();
    }
    @Override
    public GloriousDrumNoteLabel getCurrentLabel() {
        return ModClientConfigs.GLORIOUS_DRUM_LABEL_TYPE.get();
    }

    @Override
    protected void openMidiOptions() {
        minecraft.setScreen(new GloriousDrumMidiOptionsScreen(MIDI_OPTIONS, this, instrumentScreen));
    }
}