package com.cstav.genshinstrument.sound;

import java.util.Optional;

import com.cstav.genshinstrument.client.gui.screens.instrument.partial.notegrid.AbstractGridInstrumentScreen;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public abstract class NoteSoundRegistrer {
    public static final String STEREO_SUFFIX = "_stereo";
    
    /**
     * Creates a {@link NoteSound} with null sounds, that will get filled
     * upon registration.
     * @param name The name of the sound's entry. Appends "_note{{@code note}}" to the mono entry
     * as well as {@link NoteSoundRegistrer#STEREO_SUFFIX "_stereo"} to the stereo entry.
     * @param note The index of the note
     * @param hasStereo If this note has a stereo version
     * 
     * @return The new instrument sound instance
     * @see ModSounds#registerNote(String, boolean)
     */
    public static NoteSound registerInstrument(ResourceLocation soundLocation, int note, boolean hasStereo) {
        return registerNote(soundLocation.withSuffix("_note_"+note), hasStereo);
    }
    /**
     * Creates a {@link NoteSound} with null sounds, that will get filled
     * upon registration.
     * @param name The name of the sound's entry. Appends {@link NoteSoundRegistrer#STEREO_SUFFIX "_stereo"} to the stereo entry, if exists.
     * @param hasStereo If this note has a stereo version
     * @return The new instrument sound instance
     */
    public static NoteSound registerNote(ResourceLocation soundLocation, boolean hasStereo) {
        return new NoteSound(
            // Mono
            registerSound(soundLocation),
            // Stereo (if exists)
            hasStereo
                ? Optional.of(registerSound(soundLocation.withSuffix(STEREO_SUFFIX)))
                : Optional.empty()
        );
    }
    public static NoteSound registerNote(ResourceLocation soundLocation) {
        return registerNote(soundLocation, false);
    }

    private static SoundEvent registerSound(final ResourceLocation soundLocation) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, soundLocation, createSoundUnsafe(soundLocation));
    }

    public static SoundEvent createSoundUnsafe(final ResourceLocation location) {
        return SoundEvent.createVariableRangeEvent(location);
    }


    /**
     * Registers a series of notes for a grid instrument.
     * @param soundRegistrer The registrer to register the sounds to
     * @param baseNoteLocation The base location of which to have the sounds in
     * @param hasStereo Does this instrument have stereo support?
     * @return An array of {@link NoteSound NoteSounds} consisting of all the
     * sounds of the described instrument
     * 
     * @see NoteSoundRegistrer#registerInstrument(DeferredRegister, ResourceLocation, int, boolean)
     */
    public static NoteSound[] createInstrumentNotes(ResourceLocation baseNoteLocation, boolean hasStereo, int rows, int columns) {

        final NoteSound[] sounds = new NoteSound[rows * columns];

        for (int i = 0; i < sounds.length; i++)
            sounds[i] = registerInstrument(baseNoteLocation, i, hasStereo);

        return sounds;
    }

    public static NoteSound[] createInstrumentNotes(ResourceLocation baseNoteLocation, boolean hasStereo) {
        return createInstrumentNotes(baseNoteLocation, hasStereo, AbstractGridInstrumentScreen.DEF_ROWS, AbstractGridInstrumentScreen.DEF_COLUMNS);
    }
    public static NoteSound[] createInstrumentNotes(final ResourceLocation baseNoteLocation) {
        return createInstrumentNotes(baseNoteLocation, false);
    }
}
