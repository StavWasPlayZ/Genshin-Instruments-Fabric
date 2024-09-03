package com.cstav.genshinstrument.sound;

import com.cstav.genshinstrument.GInstrumentMod;

import com.cstav.genshinstrument.client.gui.screen.instrument.partial.grid.GridInstrumentScreen;
import com.cstav.genshinstrument.sound.held.HeldNoteSound;
import com.cstav.genshinstrument.sound.registrar.HeldNoteSoundRegistrar;
import com.cstav.genshinstrument.sound.registrar.NoteSoundRegistrar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class GISounds {

    public static void load() {
        GInstrumentMod.LOGGER.info("Registered all note sounds for instruments");
    }

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

    // Metadata stuff
    private static final float
        WINDSONG_HOLD_DURATION = 3f,
        WINDSONG_FADE_TIME = .25f
    ;

    public static final HeldNoteSound[]
        NIGHTWIND_HORN = hnsr(loc("nightwind_horn"))
            .holdBuilder(GISounds::nightwindSoundBuilder)
            .attackBuilder(GISounds::nightwindSoundBuilder)

//            //NOTE Test for release sound
//            .releaseBuilder((builder) -> builder
//                .chain(SoundEvents.COW_DEATH.getLocation())
//                .alreadyRegistered()
//                .add(GridInstrumentScreen.DEF_ROWS * 2)
//                .registerAll()
//            )

            .holdDelay(.03f)
            .chainedHoldDelay(-WINDSONG_FADE_TIME * 2)
            .releaseFadeOut(WINDSONG_FADE_TIME / 10)
            .fullHoldFadeoutTime(2)
            .decays(7)
        .register(WINDSONG_HOLD_DURATION)
    ;


    private static NoteSound[] nightwindSoundBuilder(final NoteSoundRegistrar builder) {
        return builder.stereo().registerGrid(GridInstrumentScreen.DEF_ROWS, 2);
    }


    /**
     * Shorthand for {@code new ResourceLocation(GInstrumentMod.MODID, name)}
     */
    private static ResourceLocation loc(final String name) {
        return new ResourceLocation(GInstrumentMod.MODID, name);
    }

    /**
     * Shorthand for {@code new NoteSoundRegistrar(instrumentId)}
     */
    private static NoteSoundRegistrar nsr(ResourceLocation instrumentId) {
        return new NoteSoundRegistrar(instrumentId);
    }
    /**
     * Shorthand for {@code new HeldNoteSoundRegistrar(soundRegistrar, instrumentId)}
     */
    private static HeldNoteSoundRegistrar hnsr(ResourceLocation instrumentId) {
        return new HeldNoteSoundRegistrar(instrumentId);
    }

}
