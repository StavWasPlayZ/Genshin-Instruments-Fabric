package com.cstav.genshinstrument.item;

import com.cstav.genshinstrument.ModCreativeModeTabs;
import com.cstav.genshinstrument.client.ModArmPose;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractInstrumentScreen;
import com.cstav.genshinstrument.event.PosePlayerArmEvent;
import com.cstav.genshinstrument.event.PosePlayerArmEvent.PosePlayerArmEventArgs;
import com.cstav.genshinstrument.networking.OpenInstrumentPacketSender;
import com.cstav.genshinstrument.util.CommonUtil;
import com.cstav.genshinstrument.util.ServerUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
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

    protected final OpenInstrumentPacketSender onOpenRequest;
    /**
     * @param onOpenRequest A server-side event fired when the player has requested to interact
     * with the instrument.
     * It should should send a packet to the given player for opening this instrument's screen.
     * 
     * This item will automatically be added to the {@link ModCreativeModeTabs#INSTRUMENTS instruments} creative tab.
     */
    public InstrumentItem(final OpenInstrumentPacketSender onOpenRequest) {
        this(onOpenRequest, new Properties().tab(ModCreativeModeTabs.INSTRUMENTS_TAB));
    }
    /**
     * @param onOpenRequest A server-side event fired when the player has requested to interact
     * with the instrument.
     * It should should send a packet to the given player for opening this instrument's screen.
     * @param properties The properties of this instrument item. {@link Properties#stacksTo stack size}
     * will always be set to 1.
     */
    public InstrumentItem(final OpenInstrumentPacketSender onOpenRequest, final Properties properties) {
        super(properties.stacksTo(1));
        this.onOpenRequest = onOpenRequest;
    }
    

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        return pLevel.isClientSide ? InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand))
            : ServerUtil.sendOpenPacket((ServerPlayer)pPlayer, pUsedHand, onOpenRequest)
                ? InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand))
                : InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
    }


    static {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER)
            PosePlayerArmEvent.EVENT.register(InstrumentItem::itemArmPose);
    }
    @Environment(EnvType.CLIENT)
    private static void itemArmPose(final PosePlayerArmEventArgs args) {
        CommonUtil.getItemInHands(InstrumentItem.class, args.player).ifPresent((item) ->
            item.onPosePlayerArm(args)
        );
    }

    @Environment(EnvType.CLIENT)
    public void onPosePlayerArm(PosePlayerArmEventArgs args) {
        ModArmPose.poseForItemInstrument(args);
    }
}
