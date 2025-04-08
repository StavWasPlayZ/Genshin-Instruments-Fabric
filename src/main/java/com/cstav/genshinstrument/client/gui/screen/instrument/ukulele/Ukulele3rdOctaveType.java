package com.cstav.genshinstrument.client.gui.screen.instrument.ukulele;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum Ukulele3rdOctaveType {
    CHORDS("button.genshinstrument.ukulele_3rd_octave.chords"),
    TREBLE("button.genshinstrument.ukulele_3rd_octave.treble");

    public final String key;
    Ukulele3rdOctaveType(final String key) {
        this.key = key;
    }
}