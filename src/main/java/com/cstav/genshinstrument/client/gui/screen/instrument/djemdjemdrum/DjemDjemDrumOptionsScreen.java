
package com.cstav.genshinstrument.client.gui.screen.instrument.djemdjemdrum;

import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.InstrumentScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.label.INoteLabel;
import com.cstav.genshinstrument.client.gui.screen.options.instrument.partial.InstrumentOptionsScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DjemDjemDrumOptionsScreen extends InstrumentOptionsScreen {
    public DjemDjemDrumOptionsScreen(@Nullable InstrumentScreen screen) {
        super(screen);
    }

    @Override
    public INoteLabel[] getLabels() {
        return DjemDjemDrumNoteLabel.availableVals();
    }

    @Override
    public INoteLabel getCurrentLabel() {
        return ModClientConfigs.DJEM_DJEM_DRUM_LABEL_TYPE.get();
    }

    @Override
    protected void saveLabel(INoteLabel newLabel) {
        if (newLabel instanceof DjemDjemDrumNoteLabel label) {
            ModClientConfigs.DJEM_DJEM_DRUM_LABEL_TYPE.set(label);
        }
    }
}