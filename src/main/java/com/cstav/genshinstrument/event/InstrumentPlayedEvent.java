package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.block.partial.InstrumentBlockEntity;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.InstrumentPlayedEventArgs;
import com.cstav.genshinstrument.event.impl.Cancelable;
import com.cstav.genshinstrument.event.impl.EventArgs;
import com.cstav.genshinstrument.event.impl.ModEvent;

import com.cstav.genshinstrument.networking.packet.instrument.NoteSoundMetadata;
import com.cstav.genshinstrument.util.InstrumentEntityData;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * An abstract implementation of a sound played event.
 * @param <T> The sound object type
 */
@FunctionalInterface
public interface InstrumentPlayedEvent<T> extends ModEvent<InstrumentPlayedEventArgs<T>> {

    static Event<InstrumentPlayedEvent<Object>> EVENT = EventFactory.createArrayBacked(InstrumentPlayedEvent.class,
        (listeners) -> args -> ModEvent.handleEvent(listeners, args)
    );

    /**
     * Handles the instrument event.
     * Only invokes for subscribers whose sound type is
     * an instance of the called event.
     * @param <E> The event type
     * @param <A> The args type
     */
    public static <E extends ModEvent<A>, A extends InstrumentPlayedEventArgs<?>>
    void handleInsEvent(
        E[] listeners, A args
    ) {
        handleInsEvent(listeners, args, true);
    }
    /**
     * Invokes the provided event subscribers,
     * then passes to the {@link InstrumentPlayedEvent#EVENT general instrument event}.
     * @param <E> The event type
     * @param <A> The args type
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <E extends ModEvent<A>, A extends InstrumentPlayedEventArgs<?>>
    void handleInsEvent(
        E[] listeners, A args, boolean callMain
    ) {
        ModEvent.handleEvent(listeners, args);

        if (callMain) {
            EVENT.invoker().triggered((InstrumentPlayedEventArgs) args);
        }
    }


    /**
     * @param <T> The sound object type
     */
    @Cancelable
    public static class InstrumentPlayedEventArgs<T> extends EventArgs {

        private final T sound;
        private final NoteSoundMetadata soundMeta;
        private final Level level;

        /**
         * Information about the player initiator.
         * Present if there is indeed a player initiator.
         */
        private final Optional<ByPlayerArgs> playerInfo;

        /**
         * Constructor for creating a non-player event
         */
        public InstrumentPlayedEventArgs(Level level, T sound, NoteSoundMetadata soundMeta) {
            this.level = level;
            this.sound = sound;
            this.soundMeta = soundMeta;

            this.playerInfo = Optional.empty();
        }

        /**
         * Constructor for creating a by-player event
         */
        public InstrumentPlayedEventArgs(Player player, T sound, NoteSoundMetadata soundMeta) {
            this.level = player.level();
            this.sound = sound;
            this.soundMeta = soundMeta;

            this.playerInfo = Optional.of(new ByPlayerArgs(player));
        }


        public T sound() {
            return sound;
        }
        public NoteSoundMetadata soundMeta() {
            return soundMeta;
        }
        public Level level() {
            return level;
        }
        public Optional<ByPlayerArgs> playerInfo() {
            return playerInfo;
        }

        /**
         * Convenience method to convert the volume of the note
         * into a {@code float} percentage
         */
        public float volume() {
            return soundMeta.volume() / 100f;
        }


        /**
         * An object containing information
         * about the player who initiated the event
         */
        public class ByPlayerArgs {
            public final Player player;
            public final Optional<InteractionHand> hand;

            protected final InstrumentPlayedEventArgs<T> baseEvent = InstrumentPlayedEventArgs.this;

            public ByPlayerArgs(Player player) {
                this.player = player;

                if (InstrumentEntityData.isItem(player)) {
                    this.hand = Optional.of(InstrumentEntityData.getHand(player));
                } else {
                    this.hand = Optional.empty();
                }
            }

            /**
             * <p>Returns whether this event was fired by an item instrument.</p>
             * A {@code false} result does NOT indicate a block instrument.
             * @see ByPlayerArgs#isBlockInstrument
             */
            public boolean isItemInstrument() {
                return hand.isPresent();
            }
            /**
             * <p>Returns whether this event was fired by a block instrument.</p>
             * A {@code false} result does NOT indicate an instrument item.
             * @see ByPlayerArgs#isItemInstrument()
             */
            public boolean isBlockInstrument() {
                return !isItemInstrument()
                    && player.level().getBlockEntity(baseEvent.soundMeta.pos())
                    instanceof InstrumentBlockEntity;
            }

            /**
             * @return Whether the played sound was not produced by an instrument
             */
            public boolean isNotInstrument() {
                return !isBlockInstrument() && !isItemInstrument();
            }
        }
    }

}
