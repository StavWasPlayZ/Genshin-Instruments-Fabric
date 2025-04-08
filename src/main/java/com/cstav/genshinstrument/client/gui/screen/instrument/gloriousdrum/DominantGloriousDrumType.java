package com.cstav.genshinstrument.client.gui.screen.instrument.gloriousdrum;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Locale;

@Environment(EnvType.CLIENT)
public enum DominantGloriousDrumType {
    DON, KA, BOTH;

    public static final String DDT_KEY = "button.genshinstrument.dominantDrumType";


    public String getKey() {
        return (this == BOTH)
            ? (DDT_KEY + ".both")
            : ((this == KA) ? GloriousDrumButtonType.KA : GloriousDrumButtonType.DON).getTransKey();
    }

    public String getDescKey() {
        return DDT_KEY + "." + name().toLowerCase(Locale.ENGLISH) + ".tooltip";
    }
}