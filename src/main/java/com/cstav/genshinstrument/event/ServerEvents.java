package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.item.InstrumentItem;
import com.cstav.genshinstrument.networking.GIPacketHandler;
import com.cstav.genshinstrument.networking.packet.instrument.s2c.NotifyInstrumentOpenPacket;
import com.cstav.genshinstrument.networking.packet.instrument.util.InstrumentPacketUtil;
import com.cstav.genshinstrument.util.InstrumentEntityData;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
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
        ServerPlayConnectionEvents.DISCONNECT.register(ServerEvents::onPlayerJoin);
    }

    private static void onServerTick(final Level level) {
        level.getServer().getPlayerList().getPlayers().forEach((player) -> {
            if (shouldAbruptlyClose(player))
                InstrumentPacketUtil.setInstrumentClosed(player);
        });
    }

    private static void onPlayerLeave(ServerGamePacketListenerImpl handler, MinecraftServer server) {
        InstrumentPacketUtil.setInstrumentClosed(handler.player);
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

    //#region Sync the open state of players to a new player

    private static void onPlayerJoin(ServerGamePacketListenerImpl handler, MinecraftServer server) {
        final ServerPlayer player = handler.player;
        final Level level = player.level();
        if (level.isClientSide)
            return;

        level.getServer().getPlayerList().getPlayers().forEach((oPlayer) -> {
            if (oPlayer.equals(player))
                return;

            if (InstrumentEntityData.isOpen(oPlayer))
                notifyOpenStateToPlayer(oPlayer, player);
        });
    }

    private static void notifyOpenStateToPlayer(final Player player, final ServerPlayer target) {
        final NotifyInstrumentOpenPacket packet;

        if (InstrumentEntityData.isItem(player)) {
            packet = new NotifyInstrumentOpenPacket(
                player.getUUID(),
                InstrumentEntityData.getHand(player)
            );
        } else {
            packet = new NotifyInstrumentOpenPacket(
                player.getUUID(),
                InstrumentEntityData.getBlockPos(player)
            );
        }

        GIPacketHandler.sendToClient(packet, target);
    }

    //#endregion

}