package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.event.HeldNoteSoundPlayedEvent.HeldNoteSoundPlayedEventArgs;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.InstrumentPlayedEventArgs;
import com.cstav.genshinstrument.event.impl.Cancelable;
import com.cstav.genshinstrument.event.impl.ModEvent;
import com.cstav.genshinstrument.networking.packet.instrument.NoteSoundMetadata;
import com.cstav.genshinstrument.networking.packet.instrument.util.HeldSoundPhase;
import com.cstav.genshinstrument.sound.held.HeldNoteSound;
import com.cstav.genshinstrument.sound.held.HeldNoteSounds;
import com.cstav.genshinstrument.sound.held.InitiatorID;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import static com.cstav.genshinstrument.event.InstrumentPlayedEvent.handleInsEvent;

/**
 * Fired when a {@link HeldNoteSound} has been produced.
 * This event is fired on the Forge event bus
 */
@FunctionalInterface
public interface HeldNoteSoundPlayedEvent extends ModEvent<HeldNoteSoundPlayedEventArgs> {

    Event<HeldNoteSoundPlayedEvent> EVENT = EventFactory.createArrayBacked(HeldNoteSoundPlayedEvent.class,
        (listeners) -> args -> handleInsEvent(listeners, args)
    );

    @Cancelable
    class HeldNoteSoundPlayedEventArgs extends InstrumentPlayedEventArgs<HeldNoteSound> {
        public final HeldSoundPhase phase;
        /**
         * The ID of the sound initiator.
         * Can be used to access held sound instances from
         * {@link HeldNoteSounds}.
         */
        public final InitiatorID initiatorID;

        public HeldNoteSoundPlayedEventArgs(Level level, HeldNoteSound sound, NoteSoundMetadata soundMeta,
                                            HeldSoundPhase phase, InitiatorID initiatorID) {
            super(level, sound, soundMeta);
            this.phase = phase;
            this.initiatorID = initiatorID;
        }
        public HeldNoteSoundPlayedEventArgs(Entity initiator, HeldNoteSound sound, NoteSoundMetadata soundMeta,
                                            HeldSoundPhase phase, InitiatorID initiatorID) {
            super(initiator, sound, soundMeta);
            this.phase = phase;
            this.initiatorID = initiatorID;
        }
    }
}
