package com.cstav.genshinstrument.util;

import com.cstav.genshinstrument.mixin.util.IEntityModData;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public abstract class InstrumentEntityData {
    public static final String
        OPEN_TAG = "InstrumentOpen",
        IS_ITEM_TAG = "IsItem",
        BLOCK_POS_TAG = "BlockPos",
        HAND_TAG = "IsOffhand"
    ;


    public static CompoundTag getModTag(final Entity entity) {
        return ((IEntityModData)entity).getPersistentData();
    }


    public static boolean isOpen(final Player player) {
        return getModTag(player).getBoolean(OPEN_TAG);
    }
    public static boolean isItem(final Player player) {
        return getModTag(player).getBoolean(IS_ITEM_TAG);
    }

    /**
     * The hand holding the instrument.
     * Present only for when {@link InstrumentEntityData#isItem not an item}.
     */
    public static InteractionHand getHand(final Player player) {
        return getModTag(player).getBoolean(HAND_TAG) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }
    /**
     * The position of the instrument block.
     * Present only for when {@link InstrumentEntityData#isItem is an item}.
     */
    public static BlockPos getBlockPos(final Player player) {
        final CompoundTag posTag = getModTag(player).getCompound(BLOCK_POS_TAG);
        return posTag.isEmpty() ? null : NbtUtils.readBlockPos(posTag);
    }


    public static void setOpen(final Player player, final BlockPos pos) {
        final CompoundTag modTag = getModTag(player);
        modTag.putBoolean(OPEN_TAG, true);

        modTag.put(BLOCK_POS_TAG, NbtUtils.writeBlockPos(pos));
        modTag.putBoolean(IS_ITEM_TAG, false);
    }
    public static void setOpen(final Player player, final InteractionHand hand) {
        final CompoundTag modTag = getModTag(player);
        modTag.putBoolean(OPEN_TAG, true);

        modTag.putBoolean(IS_ITEM_TAG, true);
        modTag.putBoolean(HAND_TAG, hand == InteractionHand.OFF_HAND);
    }

    public static void setClosed(final Player player) {
        getModTag(player).putBoolean(OPEN_TAG, false);
    }

}
