package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.event.InstrumentOpenStateChangedEvent.InstrumentOpenStateChangedEventArgs;
import com.cstav.genshinstrument.item.InstrumentItem;
import com.cstav.genshinstrument.networking.packet.instrument.util.InstrumentPacketUtil;
import com.cstav.genshinstrument.sound.held.HeldNoteSounds;
import com.cstav.genshinstrument.sound.held.InitiatorID;
import com.cstav.genshinstrument.util.InstrumentEntityData;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class ServerEvents {
    private static final int MAX_BLOCK_INSTRUMENT_DIST = 6;

    public static void register() {
        ServerTickEvents.START_WORLD_TICK.register(ServerEvents::onServerTick);
        ServerPlayConnectionEvents.DISCONNECT.register(ServerEvents::onPlayerLeave);
        InstrumentOpenStateChangedEvent.EVENT.register(ServerEvents::onInstrumentScreenOpenStateChanged);
    }

    public static void onServerTick(final Level level) {
        level.players().forEach((player) -> {
            if (shouldAbruptlyClose(player))
                InstrumentPacketUtil.setInstrumentClosed(player);
        });
    }

    private static void onPlayerLeave(ServerGamePacketListenerImpl handler, MinecraftServer server) {
        InstrumentPacketUtil.setInstrumentClosed(handler.player);
    }

    private static void onInstrumentScreenOpenStateChanged(final InstrumentOpenStateChangedEventArgs args) {
        if (args.player.level().isClientSide)
            return;

        if (!args.isOpen) {
            // Remove their potential entry over at HeldNoteSounds
            HeldNoteSounds.release(InitiatorID.fromEntity(args.player));
        }
    }


    private static boolean shouldAbruptlyClose(final Player player) {
        if (!InstrumentEntityData.isOpen(player))
            return false;

        if (InstrumentEntityData.isItem(player)) {
            // Close instrument item if it is no longer in their hands
            final InteractionHand hand = InstrumentEntityData.getHand(player);
            if (hand == null)
                return true;

            final ItemStack handItem = player.getItemInHand(hand);
            // This is done like so because there is no event (that I know of) for when an item is moved/removed
            return !(handItem.getItem() instanceof InstrumentItem);
        } else {
            // Close an instrument block if the player is too far away
            return !InstrumentEntityData.getBlockPos(player)
                .closerToCenterThan(player.position(), MAX_BLOCK_INSTRUMENT_DIST);
        }
    }

}