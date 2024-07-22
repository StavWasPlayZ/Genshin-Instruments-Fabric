package com.cstav.genshinstrument.client.midi;

import com.cstav.genshinstrument.sound.NoteSound;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Environment(EnvType.CLIENT)
public record MidiOverflowResult(
    NoteSound newNoteSound,
    int pitchOffset,
    int fixedOctaveNote,
    OverflowType type
) {
    public static enum OverflowType {TOP, BOTTOM}
}