package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.block.partial.AbstractInstrumentBlock;
import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.InstrumentScreen;
import com.cstav.genshinstrument.client.gui.screen.options.instrument.partial.SoundTypeOptionsScreen;
import com.cstav.genshinstrument.client.midi.MidiController;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.InstrumentPlayedEventArgs;
import com.cstav.genshinstrument.event.MidiEvent.MidiEventArgs;
import com.cstav.genshinstrument.event.PosePlayerArmEvent.PosePlayerArmEventArgs;
import com.cstav.genshinstrument.item.ItemPoseModifier;
import com.cstav.genshinstrument.sound.NoteSound;
import com.cstav.genshinstrument.sound.held.HeldNoteSounds;
import com.cstav.genshinstrument.util.InstrumentEntityData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

@Environment(EnvType.CLIENT)
public abstract class ClientEvents {

    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    
    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(ClientEvents::onClientTick);
        ServerWorldEvents.UNLOAD.register(ClientEvents::onLevelUnload);

        InstrumentPlayedEvent.EVENT.register(ClientEvents::onInstrumentPlayed);
        PosePlayerArmEvent.EVENT.register(ClientEvents::posePlayerArmEvent);

        ClientLifecycleEvents.CLIENT_STOPPING.register(ClientEvents::onGameShutdown);
        MidiEvent.EVENT.register(ClientEvents::onMidiEvent);
    }


    // Handle instrument arm pose

    private static void poseForBlockInstrument(PosePlayerArmEventArgs args, Player player) {
        final Block block = player.level().getBlockState(InstrumentEntityData.getBlockPos(player)).getBlock();
        if (!(block instanceof AbstractInstrumentBlock blockInstrument))
            return;

        if (!InstrumentEntityData.isOpen(args.player) || InstrumentEntityData.isItem(args.player))
            return;

        blockInstrument.onPosePlayerArm(args);
    }
    private static void poseForItemInstrument(PosePlayerArmEventArgs args, Player player) {
        final ItemStack instrumentItem = player.getItemInHand(InstrumentEntityData.getHand(player));
        if (instrumentItem == ItemStack.EMPTY)
            return;

        if (!(instrumentItem.getItem() instanceof ItemPoseModifier item))
            return;

        if (!InstrumentEntityData.isOpen(args.player) || !InstrumentEntityData.isItem(args.player))
            return;

        item.onPosePlayerArm(args);
    }

    public static void posePlayerArmEvent(final PosePlayerArmEventArgs args) {
        final Player player = args.player;
        if (!InstrumentEntityData.isOpen(player))
            return;

        if (InstrumentEntityData.isItem(player))
            poseForItemInstrument(args, player);
        else
            poseForBlockInstrument(args, player);
    }
    
    // Responsible for closing the instrument screen when
    // an instrument item is missing from the player's hands
    public static void onClientTick(Minecraft mc) {
        InstrumentScreen.getCurrentScreen(mc).ifPresent(InstrumentScreen::handleAbruptClosing);
    }

    
    // Responsible for showing the notes other players play
    public static void onInstrumentPlayed(final InstrumentPlayedEventArgs<?> args) {
        if (!args.level().isClientSide)
            return;
        if (!ModClientConfigs.SHARED_INSTRUMENT.get())
            return;

        // If this sound was produced by a player, and that player is ourselves - omit.
        if (args.playerInfo().isPresent()) {
            final Player initiator = args.playerInfo().get().player;

            if (initiator.equals(MINECRAFT.player))
                return;
        }

        // Only show play notes in the local range
        if (!args.soundMeta().pos().closerThan(MINECRAFT.player.blockPosition(), NoteSound.LOCAL_RANGE))
            return;


        InstrumentScreen.getCurrentScreen(MINECRAFT)
            // Filter instruments that do not match the one we're on.
            // If the note identifier is empty, it matters not - as the check
            // will be performed on the sound itself, which is bound to be unique for every note.
            .filter((screen) ->
                args.soundMeta().noteIdentifier().isEmpty()
                    || screen.getInstrumentId().equals(args.soundMeta().instrumentId())
            )
            .ifPresent((screen) -> screen.foreignPlay(args));
    }


    public static void onLevelUnload(MinecraftServer server, ServerLevel world) {
        HeldNoteSounds.releaseAll();
    }

    
    // Subscribe active instruments to a MIDI event
    public static void onMidiEvent(final MidiEventArgs args) {
        InstrumentScreen.getCurrentScreen(Minecraft.getInstance())
            .filter(InstrumentScreen::isMidiInstrument)
            .ifPresent((instrument) -> instrument.midiReceiver.onMidi(args));
        
        SoundTypeOptionsScreen.onMidiReceivedEvent(args);
    }

    // Safely close MIDI streams upon game shutdown
    public static void onGameShutdown(final Minecraft client) {
        MidiController.unloadDevice();
    }

}
