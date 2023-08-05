package com.cstav.genshinstrument.util;

import com.cstav.genshinstrument.mixin.util.IEntityModData;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

//TODO: Change to "InstrumentEntityData" and remove all "instruments" from method names
public abstract class ModEntityData {
    public static final String OPEN_TAG = "instrumentOpen",
        IS_ITEM_TAG = "isItem", BLOCK_POS_TAG = "blockPos";


    public static CompoundTag getModTag(final Entity entity) {
        return ((IEntityModData)entity).getPersistentData();
    }


    public static boolean isInstrumentOpen(final Player player) {
        return getModTag(player).getBoolean(OPEN_TAG);
    }
    public static boolean isInstrumentItem(final Player player) {
        return getModTag(player).getBoolean(IS_ITEM_TAG);
    }

    public static BlockPos getInstrumentBlockPos(final Player player) {
        final CompoundTag posTag = getModTag(player).getCompound(BLOCK_POS_TAG);
        return posTag.isEmpty() ? null : NbtUtils.readBlockPos(posTag);
    }


    public static void setInstrumentOpen(final Player player, final BlockPos pos) {
        final CompoundTag modTag = getModTag(player);
        modTag.putBoolean(OPEN_TAG, true);
        modTag.put(BLOCK_POS_TAG, NbtUtils.writeBlockPos(pos));

        modTag.putBoolean(IS_ITEM_TAG, false);
    }
    public static void setInstrumentOpen(final Player player) {
        final CompoundTag modTag = getModTag(player);
        modTag.putBoolean(OPEN_TAG, true);

        modTag.putBoolean(IS_ITEM_TAG, true);
    }

    public static void setInstrumentClosed(final Player player) {
        getModTag(player).putBoolean(OPEN_TAG, false);
    }

}
