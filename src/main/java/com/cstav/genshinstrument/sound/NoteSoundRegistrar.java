package com.cstav.genshinstrument.sound;

import com.cstav.genshinstrument.client.gui.screen.instrument.partial.notegrid.AbstractGridInstrumentScreen;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteSoundRegistrar {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final HashMap<ResourceLocation, NoteSound[]> SOUNDS_REGISTRY = new HashMap<>();
    public static final String STEREO_SUFFIX = "_stereo";

    public static NoteSound[] getSounds(final ResourceLocation baseSoundName) {
        return SOUNDS_REGISTRY.get(baseSoundName);
    }


    /* ----------- Registration Builder ----------- */

    private final ResourceLocation baseSoundLocation;

    private boolean hasStereo = false;
    private boolean alreadyRegistered = false;

    public NoteSoundRegistrar(ResourceLocation baseSoundLocation) {
        this.baseSoundLocation = baseSoundLocation;
    }

    /**
     * Defines that this note sound will support stereo.
     * Stereo sounds are suffixed with {@code "_stereo"}.
     */
    public NoteSoundRegistrar stereo() {
        hasStereo = true;
        return this;
    }
    /**
     * Skips the process of registering this note's SoundEvents with Minecraft.
     * For use with already registered sounds.
     */
    public NoteSoundRegistrar alreadyRegistered() {
        alreadyRegistered = true;
        return this;
    }


    public NoteSound[] register(final NoteSound[] noteSounds) {
        SOUNDS_REGISTRY.put(baseSoundLocation, noteSounds);

        LOGGER.info("Successfully registered "+noteSounds.length+" note sounds of "+baseSoundLocation);
        return noteSounds;
    }


    /* ----------- Registration Methods ----------- */

    // Grid registrer
    /**
     * Registers a matrix of sounds for a grid instrument.
     */
    public NoteSound[] registerGrid(final int rows, final int columns) {
        final NoteSound[] sounds = new NoteSound[rows * columns];

        for (int i = 0; i < sounds.length; i++)
            sounds[i] = createNote(i);

        return register(sounds);
    }

    /**
     * Registers a matrix of sounds for a grid instrument, with the
     * default amount of {@link AbstractGridInstrumentScreen#DEF_ROWS rows} and {@link AbstractGridInstrumentScreen#DEF_COLUMNS columns}.
     */
    public NoteSound[] registerGrid() {
        return registerGrid(AbstractGridInstrumentScreen.DEF_ROWS, AbstractGridInstrumentScreen.DEF_COLUMNS);
    }


    //#region Singles registrar

    private final ArrayList<NoteSound> stackedSounds = new ArrayList<>();
    /**
     * <p>Chains a note sound to this registrar.</p>
     * <p>Call back {@link ChainedNoteSoundRegistrar#add()}
     * to perform the chain and return here.</p>
     *
     * <p>Call {@link NoteSoundRegistrar#registerAll()} after all registrations
     * are complete.</p>
     */
    public ChainedNoteSoundRegistrar chain(ResourceLocation soundLocation) {
        validateNotChained();
        return new ChainedNoteSoundRegistrar(soundLocation);
    }

    public NoteSound peek() {
        validateNotChained();
        return stackedSounds.get(stackedSounds.size() - 1);
    }

    /**
     * Registers all NoteSounds added via {@link NoteSoundRegistrar#add}
     */
    public NoteSound[] registerAll() {
        validateNotChained();
        return register(stackedSounds.toArray(NoteSound[]::new));
    }

    public NoteSoundRegistrar add() {
        throw new IllegalStateException("Called add() on a non-chained registrar!");
    }


    public class ChainedNoteSoundRegistrar extends NoteSoundRegistrar {

        private final ResourceLocation soundLocation;
        private ChainedNoteSoundRegistrar(ResourceLocation soundLocation) {
            super(NoteSoundRegistrar.this.baseSoundLocation);
            this.soundLocation = soundLocation;
        }

        @Override
        public NoteSoundRegistrar add() {
            final NoteSoundRegistrar original = NoteSoundRegistrar.this;
            final ArrayList<NoteSound> stackedSounds = original.stackedSounds;

            stackedSounds.add(createNote(soundLocation, stackedSounds.size()));
            return original;
        }

    }
    private void validateNotChained() {
        if (this instanceof ChainedNoteSoundRegistrar)
            throw new IllegalStateException("Called non-chainable method on a chained registrar!");
    }

    //#endregion


    // Single registrar
    /**
     * Creates a singular {@link NoteSound} with null sounds, that will get filled
     * upon registration.
     */
    public NoteSound registerNote() {
        return createNote(baseSoundLocation, 0);
    }


    /**
     * Creates a singular {@link NoteSound} with null sounds, that will get filled
     * upon registration.
     */
    protected NoteSound createNote(ResourceLocation soundLocation, int index) {
        return new NoteSound(index, baseSoundLocation,
            getOrCreateSound(soundLocation),
            hasStereo
                ? getOrCreateSound(soundLocation.withSuffix(STEREO_SUFFIX))
                : null
        );
    }

    private SoundEvent getOrCreateSound(ResourceLocation soundLocation) {
        return alreadyRegistered
            ? BuiltInRegistries.SOUND_EVENT.get(soundLocation)
            : registerSound(soundLocation);
    }


    /**
     * Creates and registers a {@link NoteSound} with null sounds, that will get filled
     * upon registration.
     * The name of the registered sound entry will be suffixed by "_note{@code noteIndex}".
     * @param noteIndex The index of the note
     */
    public NoteSound createNote(int noteIndex) {
        return createNote(baseSoundLocation.withSuffix("_note_"+noteIndex), noteIndex);
    }
    

    protected SoundEvent registerSound(final ResourceLocation soundLocation) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, soundLocation, createSoundUnsafe(soundLocation));
    }
    private static SoundEvent createSoundUnsafe(final ResourceLocation location) {
        return SoundEvent.createVariableRangeEvent(location);
    }
}
