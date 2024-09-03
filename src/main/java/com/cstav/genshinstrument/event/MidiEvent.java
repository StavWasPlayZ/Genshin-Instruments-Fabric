package com.cstav.genshinstrument.event;

import javax.sound.midi.MidiMessage;

import com.cstav.genshinstrument.event.MidiEvent.MidiEventArgs;
import com.cstav.genshinstrument.event.impl.EventArgs;
import com.cstav.genshinstrument.event.impl.ModEvent;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@Environment(EnvType.CLIENT)
public interface MidiEvent extends ModEvent<MidiEventArgs> {

    Event<MidiEvent> EVENT = EventFactory.createArrayBacked(MidiEvent.class,
        (listeners) -> args -> ModEvent.handleEvent(listeners, args)
    );

    public static class MidiEventArgs extends EventArgs {
        public final MidiMessage message;
        public final long timeStamp;

        public MidiEventArgs(final MidiMessage message, final long timeStamp) {
            this.message = message;
            this.timeStamp = timeStamp;
        }
    }

}