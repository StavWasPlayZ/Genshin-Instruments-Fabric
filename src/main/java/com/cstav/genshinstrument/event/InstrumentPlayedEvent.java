package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.event.impl.Cancelable;
import com.cstav.genshinstrument.event.impl.EventArgs;
import com.cstav.genshinstrument.event.impl.ModEvent;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifier;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface InstrumentPlayedEvent extends ModEvent<InstrumentPlayedEvent.InstrumentPlayedEventArgs> {

    Event<InstrumentPlayedEvent> EVENT = EventFactory.createArrayBacked(InstrumentPlayedEvent.class,
        (listeners) -> args -> ModEvent.handleEvent(listeners, args)
    );

    @Cancelable
    public static class InstrumentPlayedEventArgs extends EventArgs {

        public final NoteSound sound;
        public final Level level;
        public final boolean isClientSide;

        public final ResourceLocation instrumentId;
        public final NoteButtonIdentifier noteIdentifier;
        public final BlockPos pos;
        

        public InstrumentPlayedEventArgs(NoteSound sound, Level level, BlockPos pos,
                ResourceLocation instrumentId, NoteButtonIdentifier noteIdentifier, boolean isClientSide) {

            this.sound = sound;
            this.level = level;
            this.pos = pos;
            this.isClientSide = isClientSide;
            this.instrumentId = instrumentId;
            this.noteIdentifier = noteIdentifier;

            // Handle provided invalid id
            if (!Registry.ITEM.containsKey(instrumentId))
                setCanceled(true);
        }
    }



    @FunctionalInterface
    public interface ByPlayer extends ModEvent<ByPlayer.ByPlayerArgs> {

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
            /** The instrument held by the player who initiated the sound */
            public final ItemStack instrument;
            public final InteractionHand hand;
    
            public ByPlayerArgs(NoteSound sound, Player player, InteractionHand hand,
                    ResourceLocation instrumentId, NoteButtonIdentifier noteIdentifier, boolean isClientSide) {

                super(sound, player.getLevel(), player.blockPosition(), instrumentId, noteIdentifier, isClientSide);
                this.player = player;
                this.hand = hand;
    
                instrument = (hand == null) ? null : player.getItemInHand(hand);
    
                // Handle provided unmatching id
                if (!instrumentId.equals(Registry.ITEM.getKey(instrument.getItem())))
                    setCanceled(true);
            }
        }
    }

}
