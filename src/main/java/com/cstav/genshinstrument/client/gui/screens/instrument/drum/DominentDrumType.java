package com.cstav.genshinstrument.client.gui.screens.instrument.drum;

import com.cstav.genshinstrument.client.gui.screens.options.instrument.midi.DrumMidiOptionsScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum DominentDrumType {
    DON, KA, BOTH;

    public String getKey() {
        return (this == BOTH)
            ? (DrumMidiOptionsScreen.DDT_KEY+".both")
            : ((this == KA) ? DrumButtonType.KA : DrumButtonType.DON).getTransKey();
    }
}