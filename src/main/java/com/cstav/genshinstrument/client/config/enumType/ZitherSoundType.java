package com.cstav.genshinstrument.client.config.enumType;

import java.util.function.Supplier;

import com.cstav.genshinstrument.sound.ModSounds;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum ZitherSoundType implements SoundType {
    OLD(() -> ModSounds.ZITHER_OLD_NOTE_SOUNDS),
    NEW(() -> ModSounds.ZITHER_NEW_NOTE_SOUNDS);

    private Supplier<NoteSound[]> soundArr;
    private ZitherSoundType(final Supplier<NoteSound[]> soundType) {
        this.soundArr = soundType;
    }

    public Supplier<NoteSound[]> getSoundArr() {
        return soundArr;
    }
}