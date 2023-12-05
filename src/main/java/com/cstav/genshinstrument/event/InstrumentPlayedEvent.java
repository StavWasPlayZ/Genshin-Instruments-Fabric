package com.cstav.genshinstrument.event;

import java.util.Optional;

import com.cstav.genshinstrument.block.partial.InstrumentBlockEntity;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.ByPlayer.ByPlayerArgs;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.InstrumentPlayedEventArgs;
import com.cstav.genshinstrument.event.impl.Cancelable;
import com.cstav.genshinstrument.event.impl.EventArgs;
import com.cstav.genshinstrument.event.impl.ModEvent;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface InstrumentPlayedEvent extends ModEvent<InstrumentPlayedEventArgs> {

    Event<InstrumentPlayedEvent> EVENT = EventFactory.createArrayBacked(InstrumentPlayedEvent.class,
        (listeners) -> args -> ModEvent.handleEvent(listeners, args)
    );

    @Cancelable
    public static class InstrumentPlayedEventArgs extends EventArgs {

        public final NoteSound sound;
        public final int pitch, volume;

        public final Level level;
        public final boolean isClientSide;

        public final ResourceLocation instrumentId;
        public final Optional<NoteButtonIdentifier> noteIdentifier;
        public final BlockPos playPos;


        /**
         * Convenience method to convert the volume of the note
         * into a {@code float} percentage
         */
        public float volume() {
            return volume / 100f;
        }
        

        public InstrumentPlayedEventArgs(NoteSound sound, int pitch, int volume, Level level, BlockPos pos,
                ResourceLocation instrumentId, NoteButtonIdentifier noteIdentifier, boolean isClientSide) {
                    
            this.sound = sound;
            this.pitch = pitch;
            this.volume = volume;

            this.level = level;
            this.playPos = pos;
            this.isClientSide = isClientSide;

            this.instrumentId = instrumentId;
            this.noteIdentifier = Optional.ofNullable(noteIdentifier);
        }
    }



    @FunctionalInterface
    public interface ByPlayer extends ModEvent<ByPlayerArgs> {

        Event<ByPlayer> EVENT = EventFactory.createArrayBacked(ByPlayer.class,
            (listeners) -> args -> {
                ModEvent.handleEvent(listeners, args);
                // Also fire the event on the regular played event
                InstrumentPlayedEvent.EVENT.invoker().triggered(args);
            }
        );

        @Cancelable
        public static class ByPlayerArgs extends InstrumentPlayedEventArgs {
            public final Player player;

            // The values below will only be supplied if initiated from an item
            /** The instrument held by the player who initiated the sound */
            public final Optional<ItemStack> itemInstrument;
            /** The hand holding the instrument played by this player */
            public final Optional<InteractionHand> hand;


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
                    && player.getLevel().getBlockEntity(playPos) instanceof InstrumentBlockEntity;
            }

            /**
             * @return Whether the played sound was not produced by an instrument
             */
            public boolean isNotInstrument() {
                return !isBlockInstrument() && !isItemInstrument();
            }

    
            public ByPlayerArgs(NoteSound sound, int pitch, int volume, Player player, BlockPos pos, InteractionHand hand,
                    ResourceLocation instrumentId, NoteButtonIdentifier noteIdentifier, boolean isClientSide) {
                super(
                    sound, pitch, volume,
                    player.getLevel(), pos,
                    instrumentId, noteIdentifier,
                    isClientSide
                );

                this.player = player;
                this.hand = Optional.ofNullable(hand);

                itemInstrument = isItemInstrument()
                    ? Optional.of(player.getItemInHand(hand))
                    : Optional.empty();
            }
        }
    }

}
