package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.event.InstrumentPlayedEvent.InstrumentPlayedEventArgs;
import com.cstav.genshinstrument.event.NoteSoundPlayedEvent.NoteSoundPlayedEventArgs;
import com.cstav.genshinstrument.event.impl.Cancelable;
import com.cstav.genshinstrument.event.impl.ModEvent;
import com.cstav.genshinstrument.networking.packet.instrument.NoteSoundMetadata;
import com.cstav.genshinstrument.sound.NoteSound;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.cstav.genshinstrument.event.InstrumentPlayedEvent.handleInsEvent;

/**
 * An event fired when a {@link NoteSound} has been produced.
 * This event is fired on the Forge event bus
 */
@FunctionalInterface
public interface NoteSoundPlayedEvent extends ModEvent<NoteSoundPlayedEventArgs> {

    static Event<NoteSoundPlayedEvent> EVENT = EventFactory.createArrayBacked(NoteSoundPlayedEvent.class,
        (listeners) -> args -> handleInsEvent(listeners, args, NoteSound.class)
    );

    @Cancelable
    public class NoteSoundPlayedEventArgs extends InstrumentPlayedEventArgs<NoteSound> {
        public NoteSoundPlayedEventArgs(Level level, NoteSound sound, NoteSoundMetadata soundMeta) {
            super(level, sound, soundMeta);
        }
        public NoteSoundPlayedEventArgs(Player player, NoteSound sound, NoteSoundMetadata soundMeta) {
            super(player, sound, soundMeta);
        }
    }
}
