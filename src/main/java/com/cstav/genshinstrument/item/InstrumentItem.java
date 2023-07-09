package com.cstav.genshinstrument.item;

import com.cstav.genshinstrument.networking.ModPacketHandler;
import com.cstav.genshinstrument.networking.packets.instrument.NotifyInstrumentOpenPacket;
import com.cstav.genshinstrument.networking.packets.instrument.OpenInstrumentPacket;
import com.cstav.genshinstrument.util.ModEntityData;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * An item responsible for opening an {@link AbstractInstrumentScreen}.
 */
public class InstrumentItem extends Item {

    protected final ServerPlayerRunnable onOpenRequest;
    /**
     * @param onOpenRequest A server-side event fired when the player has requested to interact
     * with the instrument.
     * It should should send a packet to the given player for opening this instrument's screen.
     */
    public InstrumentItem(final ServerPlayerRunnable onOpenRequest) {
        this(onOpenRequest, new Properties());
    }
    /**
     * @param onOpenRequest A server-side event fired when the player has requested to interact
     * with the instrument.
     * It should should send a packet to the given player for opening this instrument's screen.
     * @param properties The properties of this instrument item. {@link Properties#stacksTo stack size}
     * will always be set to 1.
     */
    public InstrumentItem(final ServerPlayerRunnable onOpenRequest, final Properties properties) {
        super(properties.stacksTo(1));
        this.onOpenRequest = onOpenRequest;
    }

    static void sendOpenRequest(ServerPlayer player, InteractionHand hand, String instrumentType) {
        ModPacketHandler.sendToClient(new OpenInstrumentPacket(instrumentType, hand), player);
    }
    

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            onOpenRequest.run((ServerPlayer)pPlayer, pUsedHand);

            // Update the the capabilty on server
            ModEntityData.setInstrumentOpen(pPlayer, true);
            // And clients
            pLevel.players().forEach((player) ->
                ModPacketHandler.sendToClient(
                    new NotifyInstrumentOpenPacket(pPlayer.getUUID(), true),
                    (ServerPlayer)player
                )
            );
        }
        
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
    

    @FunctionalInterface
    public static interface ServerPlayerRunnable {
        void run(final ServerPlayer player, final InteractionHand hand);
    }
}
