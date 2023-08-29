package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.block.partial.AbstractInstrumentBlock;
import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.AbstractInstrumentScreen;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.ByPlayer.ByPlayerArgs;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.InstrumentPlayedEventArgs;
import com.cstav.genshinstrument.event.MidiEvent.MidiEventArgs;
import com.cstav.genshinstrument.event.PosePlayerArmEvent.PosePlayerArmEventArgs;
import com.cstav.genshinstrument.sound.NoteSound;
import com.cstav.genshinstrument.util.InstrumentEntityData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

@Environment(EnvType.CLIENT)
public abstract class ClientEvents {

    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    
    public static void register() {
        PosePlayerArmEvent.EVENT.register(ClientEvents::posePlayerArmEvent);
        ClientTickEvents.START_CLIENT_TICK.register(ClientEvents::onClientTick);
        InstrumentPlayedEvent.EVENT.register(ClientEvents::onInstrumentPlayed);
        MidiEvent.EVENT.register(ClientEvents::onMidiEvent);
    }

    // Handle block instrument arm pose
    public static void posePlayerArmEvent(final PosePlayerArmEventArgs args) {
        final Player player = args.player;

        if (!InstrumentEntityData.isOpen(player) || InstrumentEntityData.isItem(player))
			return;


        final Block block = player.level().getBlockState(InstrumentEntityData.getBlockPos(player)).getBlock();
        if (block instanceof AbstractInstrumentBlock blockInstrument)
            blockInstrument.onPosePlayerArm(args);
    }
    
    // Responsible for closing the instrument screen when
    // an instrument item is missing from the player's hands
    public static void onClientTick(Minecraft mc) {
        AbstractInstrumentScreen.getCurrentScreen(mc).ifPresent(AbstractInstrumentScreen::handleAbruptClosing);
    }

    
    // Responsible for showing the notes other players play
    public static void onInstrumentPlayed(final InstrumentPlayedEventArgs args) {
        if (!args.isClientSide)
            return;
        if (!ModClientConfigs.SHARED_INSTRUMENT.get())
            return;

        // If this sound was produced by a player, and that player is ourselves - omit.
        if ((args instanceof ByPlayerArgs) && ((ByPlayerArgs)(args)).player.equals(MINECRAFT.player))
            return;

        // Only show play notes in the local range
        if (!args.pos.closerThan(MINECRAFT.player.blockPosition(), NoteSound.LOCAL_RANGE))
            return;


        AbstractInstrumentScreen.getCurrentScreen(MINECRAFT).ifPresent((screen) -> {
            // The produced instrument sound has to match the current screen's sounds
            if (!screen.getInstrumentId().equals(args.instrumentId))
                return;

            try {

                screen.getNoteButton(args.noteIdentifier).playNoteAnimation(true);

            } catch (Exception e) {}
        });
    }
    
    // Subscribe active instruments to a MIDI event
    public static void onMidiEvent(final MidiEventArgs args) {
        AbstractInstrumentScreen.getCurrentScreen(Minecraft.getInstance()).ifPresent((screen) -> screen.onMidi(args));
    }

}
