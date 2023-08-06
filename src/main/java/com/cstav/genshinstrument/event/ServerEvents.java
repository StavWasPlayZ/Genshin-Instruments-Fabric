package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.item.InstrumentItem;
import com.cstav.genshinstrument.util.CommonUtil;
import com.cstav.genshinstrument.util.InstrumentEntityData;
import com.cstav.genshinstrument.util.ServerUtil;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class ServerEvents {

    public static void register() {
        ServerTickEvents.START_WORLD_TICK.register(ServerEvents::onServerTick);
    }

    public static void onServerTick(final Level level) {
        level.players().forEach(ServerEvents::handleAbruptInstrumentClose);
    }

    private static void handleAbruptInstrumentClose(final Player player) {
        if (!InstrumentEntityData.isOpen(player))
            return;

        if (InstrumentEntityData.isItem(player)) {
            // This is done like so because there is no event (that I know of) for when an item is moved/removed
            if (CommonUtil.getItemInHands(InstrumentItem.class, player).isEmpty())
                ServerUtil.setInstrumentClosed(player);
        } else {
            // Close an instrument block if the player is too far away
            if (InstrumentEntityData.getBlockPos(player).closerToCenterThan(player.position(), 5))
                ServerUtil.setInstrumentClosed(player);
        }
    }

}