package com.cstav.genshinstrument.client.config.enumType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum InstrumentChannelType {
    MONO, MIXED, STEREO
}
