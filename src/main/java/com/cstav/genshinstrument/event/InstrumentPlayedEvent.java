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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * An abstract implementation of a sound played event.
 * @param <T> The sound object type
 */
@FunctionalInterface
public interface InstrumentPlayedEvent<T> extends ModEvent<InstrumentPlayedEventArgs<T>> {

    Event<InstrumentPlayedEvent<Object>> EVENT = EventFactory.createArrayBacked(InstrumentPlayedEvent.class,
        (listeners) -> args -> ModEvent.handleEvent(listeners, args)
    );

    /**
     * Handles the instrument event.
     * Only invokes for subscribers whose sound type is
     * an instance of the called event.
     * @param <E> The event type
     * @param <A> The args type
     */
    static <E extends ModEvent<A>, A extends InstrumentPlayedEventArgs<?>>
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
    static <E extends ModEvent<A>, A extends InstrumentPlayedEventArgs<?>>
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
    class InstrumentPlayedEventArgs<T> extends EventArgs {

        private final T sound;
        private final NoteSoundMetadata soundMeta;
        private final Level level;

        /**
         * Information about the entity initiator.
         * Present if there is indeed an entity initiator.
         */
        private final Optional<EntityInfo> entityInfo;
        public boolean isByEntity() {
            return entityInfo.isPresent();
        }
        public boolean isByPlayer() {
            return isByEntity() && (entityInfo.get().entity instanceof Player);
        }

        /**
         * Constructor for creating a non-entity event
         */
        public InstrumentPlayedEventArgs(Level level, T sound, NoteSoundMetadata soundMeta) {
            this.level = level;
            this.sound = sound;
            this.soundMeta = soundMeta;

            this.entityInfo = Optional.empty();
        }

        /**
         * Constructor for creating a by-entity event
         */
        public InstrumentPlayedEventArgs(Entity initiator, T sound, NoteSoundMetadata soundMeta) {
            this.level = initiator.level();
            this.sound = sound;
            this.soundMeta = soundMeta;

            this.entityInfo = Optional.of(new EntityInfo(initiator));
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
        public Optional<EntityInfo> entityInfo() {
            return entityInfo;
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
         * about the entity who initiated the event
         */
        public class EntityInfo {
            public final Entity entity;

            /**
             * The hand carrying the <b>item</b> instrument.
             * Empty for when not played by an instrument
             * or is not a player.
             */
            public final Optional<InteractionHand> hand;

            protected final InstrumentPlayedEventArgs<T> baseEvent = InstrumentPlayedEventArgs.this;

            public EntityInfo(Entity entity) {
                this.entity = entity;

                if (
                    (entity instanceof Player player)
                    && (InstrumentEntityData.isItem(player))
                ) {
                    hand = Optional.of(InstrumentEntityData.getHand(player));
                } else {
                    hand = Optional.empty();
                }
            }

            /**
             * <p>Returns whether this event was fired by an item instrument.</p>
             * A {@code false} result does NOT indicate a block instrument.
             * @see EntityInfo#isBlockInstrument
             */
            public boolean isItemInstrument() {
                return hand.isPresent();
            }
            /**
             * <p>Returns whether this event was fired by a block instrument.</p>
             * A {@code false} result does NOT indicate an instrument item.
             * @see EntityInfo#isItemInstrument()
             */
            public boolean isBlockInstrument() {
                return !isItemInstrument()
                    && level.getBlockEntity(baseEvent.soundMeta.pos())
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
