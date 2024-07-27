package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.event.InstrumentOpenStateChangedEvent.InstrumentOpenStateChangedEventArgs;
import com.cstav.genshinstrument.event.impl.EventArgs;
import com.cstav.genshinstrument.event.impl.ModEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import javax.sound.midi.MidiMessage;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public interface InstrumentOpenStateChangedEvent extends ModEvent<InstrumentOpenStateChangedEventArgs> {

    Event<InstrumentOpenStateChangedEvent> EVENT = EventFactory.createArrayBacked(InstrumentOpenStateChangedEvent.class,
        (listeners) -> args -> ModEvent.handleEvent(listeners, args)
    );

    public static class InstrumentOpenStateChangedEventArgs extends EventArgs {
        public final boolean isOpen;
        public final Player player;

        /**
         * The position of the block instrument
         */
        public final Optional<BlockPos> pos;
        /**
         * The hand of the item instrument
         */
        public final Optional<InteractionHand> hand;

        public InstrumentOpenStateChangedEventArgs(boolean isOpen, Player player, Optional<BlockPos> pos, Optional<InteractionHand> hand) {
            this.isOpen = isOpen;
            this.player = player;
            this.pos = pos;
            this.hand = hand;
        }
    }

}