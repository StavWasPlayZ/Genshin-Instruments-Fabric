package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.ByPlayer;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.ByPlayer.ByPlayerArgs;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.InstrumentPlayedEventArgs;
import com.cstav.genshinstrument.item.InstrumentItem;
import com.cstav.genshinstrument.util.ServerUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public abstract class ClientEvents {

    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    
    public static void register() {
        InstrumentPlayedEvent.EVENT.register(ClientEvents::onInstrumentPlayed);
        ClientTickEvents.START_CLIENT_TICK.register(ClientEvents::onWorldTick);
    }


    
    // Responsible for closing the instrument screen when
    // an instrument item is missing from the player's hands
    public static void onWorldTick(Minecraft mc) {
        if (!(MINECRAFT.screen instanceof AbstractInstrumentScreen))
            return;
            
        final AbstractInstrumentScreen screen = (AbstractInstrumentScreen) MINECRAFT.screen;
        if (!(MINECRAFT.player.getItemInHand(screen.interactionHand).getItem() instanceof InstrumentItem))
            screen.onClose();
    }

    
    // Responsible for showing the notes other players play
    public static void onInstrumentPlayed(final InstrumentPlayedEventArgs args) {
        if (!args.isClientSide)
            return;

        // If this sound was produced by a player, and that player is ourselves - omit.
        if ((args instanceof ByPlayer) && ((ByPlayerArgs)(args)).player.equals(MINECRAFT.player))
            return;

        if (!ModClientConfigs.SHARED_INSTRUMENT.get())
            return;


        if (!(MINECRAFT.screen instanceof AbstractInstrumentScreen))
            return;

        final AbstractInstrumentScreen screen = (AbstractInstrumentScreen) MINECRAFT.screen;
        // The instrument sound produced has to match the current screen's sounds
        if (!screen.getInstrumentId().equals(args.instrumentId))
            return;
        // Only show the played note in a close distance
        if (!args.pos.closerThan(MINECRAFT.player.blockPosition(), ServerUtil.PLAY_DISTANCE / 3))
            return;


        for (NoteButton note : screen.notesIterable())
            if (note.sound.equals(args.sound)) {
                note.playNoteAnimation(true);
                return;
            }
    }

}
