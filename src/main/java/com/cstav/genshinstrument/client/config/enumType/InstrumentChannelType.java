package com.cstav.genshinstrument.client.config.enumType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Locale;

@Environment(EnvType.CLIENT)
public enum InstrumentChannelType {
    MONO, MIXED, STEREO;

    /**
     * @return The translation key name
     * for this element
     */
    public String getKey() {
        return toString().toLowerCase(Locale.ENGLISH);
    }
}
