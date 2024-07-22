package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.event.HeldNoteSoundPlayedEvent.HeldNoteSoundPlayedEventArgs;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.InstrumentPlayedEventArgs;
import com.cstav.genshinstrument.event.impl.Cancelable;
import com.cstav.genshinstrument.event.impl.ModEvent;
import com.cstav.genshinstrument.networking.packet.instrument.NoteSoundMetadata;
import com.cstav.genshinstrument.networking.packet.instrument.util.HeldSoundPhase;
import com.cstav.genshinstrument.sound.held.HeldNoteSound;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.cstav.genshinstrument.event.InstrumentPlayedEvent.handleInsEvent;

/**
 * Fired when a {@link HeldNoteSound} has been produced.
 * This event is fired on the Forge event bus
 */
@FunctionalInterface
public interface HeldNoteSoundPlayedEvent extends ModEvent<HeldNoteSoundPlayedEventArgs> {

    static Event<HeldNoteSoundPlayedEvent> EVENT = EventFactory.createArrayBacked(HeldNoteSoundPlayedEvent.class,
        (listeners) -> args -> handleInsEvent(listeners, args, HeldNoteSound.class)
    );

    @Cancelable
    public static class HeldNoteSoundPlayedEventArgs extends InstrumentPlayedEventArgs<HeldNoteSound> {
        public final HeldSoundPhase phase;

        public HeldNoteSoundPlayedEventArgs(Level level, HeldNoteSound sound, NoteSoundMetadata soundMeta, HeldSoundPhase phase) {
            super(level, sound, soundMeta);
            this.phase = phase;
        }
        public HeldNoteSoundPlayedEventArgs(Player player, HeldNoteSound sound, NoteSoundMetadata soundMeta, HeldSoundPhase phase) {
            super(player, sound, soundMeta);
            this.phase = phase;
        }
    }
}
