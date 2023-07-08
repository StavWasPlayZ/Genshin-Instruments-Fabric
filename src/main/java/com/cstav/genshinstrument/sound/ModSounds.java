package com.cstav.genshinstrument.sound;

import static com.cstav.genshinstrument.sound.NoteSoundRegistrer.createInstrumentNotes;
import static com.cstav.genshinstrument.sound.NoteSoundRegistrer.registerNote;

import com.cstav.genshinstrument.GInstrumentMod;

import net.minecraft.resources.ResourceLocation;

public class ModSounds {

    public static final NoteSound[]
        WINDSONG_LYRE_NOTE_SOUNDS = createInstrumentNotes(loc("windsong_lyre"), true),
        VINTAGE_LYRE_NOTE_SOUNDS = createInstrumentNotes(loc("vintage_lyre")),

        ZITHER_NEW_NOTE_SOUNDS = createInstrumentNotes(loc("floral_zither_new")),
        ZITHER_OLD_NOTE_SOUNDS = createInstrumentNotes(loc("floral_zither_old")),

        GLORIOUS_DRUM = new NoteSound[] {
            registerNote(loc("glorious_drum_don")),
            registerNote(loc("glorious_drum_ka"))
        }
    ;

    /**
     * Shorthand for {@code new ResourceLocation(GInstrumentMod.MODID, name)}
     */
    private static ResourceLocation loc(final String name) {
        return new ResourceLocation(GInstrumentMod.MODID, name);
    }


    public static void regsiter() {
        GInstrumentMod.LOGGER.info("Registered all note sounds for instruments");
    }

}
