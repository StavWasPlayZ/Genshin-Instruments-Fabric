package com.cstav.genshinstrument.event;

import java.util.Optional;

import com.cstav.genshinstrument.event.InstrumentPlayedEvent.ByPlayer.ByPlayerArgs;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.InstrumentPlayedEventArgs;
import com.cstav.genshinstrument.event.impl.Cancelable;
import com.cstav.genshinstrument.event.impl.EventArgs;
import com.cstav.genshinstrument.event.impl.ModEvent;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.sound.NoteSound;
import com.cstav.genshinstrument.util.InstrumentEntityData;

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
        public final int pitch;

        public final Level level;
        public final boolean isClientSide;

        public final ResourceLocation instrumentId;
        public final NoteButtonIdentifier noteIdentifier;
        public final BlockPos pos;
        

        public InstrumentPlayedEventArgs(NoteSound sound, int pitch, Level level, BlockPos pos,
                ResourceLocation instrumentId, NoteButtonIdentifier noteIdentifier, boolean isClientSide) {
                    
            this.sound = sound;
            this.pitch = pitch;

            this.level = level;
            this.pos = pos;
            this.isClientSide = isClientSide;

            this.instrumentId = instrumentId;
            this.noteIdentifier = noteIdentifier;
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

            public final Optional<BlockPos> blockInstrumentPos;
    
            public ByPlayerArgs(NoteSound sound, int pitch, Player player, BlockPos pos, Optional<InteractionHand> hand,
                    ResourceLocation instrumentId, NoteButtonIdentifier noteIdentifier, boolean isClientSide) {

                super(sound, pitch, player.getLevel(), pos, instrumentId, noteIdentifier, isClientSide);
                this.player = player;
    
                if (hand.isPresent()) {
                    this.hand = hand;
                    itemInstrument = Optional.of((hand == null) ? null : player.getItemInHand(hand.get()));
    
                    blockInstrumentPos = Optional.empty();
                } else {
                    itemInstrument = Optional.empty();
                    this.hand = Optional.empty();
                    blockInstrumentPos = Optional.ofNullable(InstrumentEntityData.getBlockPos(player));
                }
            }
        }
    }

}
