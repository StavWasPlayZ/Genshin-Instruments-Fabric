package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.item.InstrumentItem;
import com.cstav.genshinstrument.util.InstrumentEntityData;
import com.cstav.genshinstrument.util.ServerUtil;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class ServerEvents {
    private static final int MAX_BLOCK_INSTRUMENT_DIST = 6;

    public static void register() {
        ServerTickEvents.START_WORLD_TICK.register(ServerEvents::onServerTick);
    }

    public static void onServerTick(final Level level) {
        level.players().forEach((player) -> {
            if (shouldAbruptlyClose(player))
                ServerUtil.setInstrumentClosed(player);
        });
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