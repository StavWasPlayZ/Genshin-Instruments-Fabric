package com.cstav.genshinstrument.mixin.util;

import com.cstav.genshinstrument.GInstrumentMod;

public abstract class MixinConstants {

    /**
     * The name of the root capability of this mod; all the entity data of the mod shall go under that compound tag
     */
    public static final String ROOT_CAP = GInstrumentMod.MODID+":instrument_caps";

    public static final float HAND_HEIGHT_ROT = .9f;

}
