package com.cstav.genshinstrument.client.gui.screen.instrument.drum;

import com.cstav.genshinstrument.client.gui.screen.options.instrument.midi.DrumMidiOptionsScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Locale;

@Environment(EnvType.CLIENT)
public enum DominantDrumType {
    DON, KA, BOTH;

    public static final String DDT_KEY = "button.genshinstrument.dominantDrumType";


    public String getKey() {
        return (this == BOTH)
            ? (DDT_KEY + ".both")
            : ((this == KA) ? DrumButtonType.KA : DrumButtonType.DON).getTransKey();
    }

    public String getDescKey() {
        return DDT_KEY + "." + name().toLowerCase(Locale.ENGLISH) + ".tooltip";
    }
}