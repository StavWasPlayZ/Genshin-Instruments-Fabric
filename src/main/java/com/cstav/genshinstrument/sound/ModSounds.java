package com.cstav.genshinstrument.sound;

import com.cstav.genshinstrument.GInstrumentMod;

import net.minecraft.resources.ResourceLocation;

public class ModSounds {

    public static final NoteSound[]
        WINDSONG_LYRE_NOTE_SOUNDS = nsr(loc("windsong_lyre")).stereo().registerGrid(),
        VINTAGE_LYRE_NOTE_SOUNDS = nsr(loc("vintage_lyre")).registerGrid(),

        ZITHER_NEW_NOTE_SOUNDS = nsr(loc("floral_zither_new")).registerGrid(),
        ZITHER_OLD_NOTE_SOUNDS = nsr(loc("floral_zither_old")).registerGrid(),

        GLORIOUS_DRUM = nsr(loc("glorious_drum"))
            .chain(loc("glorious_drum_don")).add()
            .chain(loc("glorious_drum_ka")).stereo().add()
        .registerAll()
    ;


    /**
     * Shorthand for {@code new ResourceLocation(GInstrumentMod.MODID, name)}
     */
    private static ResourceLocation loc(final String name) {
        return new ResourceLocation(GInstrumentMod.MODID, name);
    }


    public static void load() {
        GInstrumentMod.LOGGER.info("Registered all note sounds for instruments");
    }
    /**
     * Shorthand for {@code new NoteSoundRegistrar(instrumentId)}
     */
    private static NoteSoundRegistrar nsr(ResourceLocation instrumentId) {
        return new NoteSoundRegistrar(instrumentId);
    }

}
