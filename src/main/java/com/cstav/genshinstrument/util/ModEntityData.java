package com.cstav.genshinstrument.util;

import com.cstav.genshinstrument.mixin.util.IEntityModData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public abstract class ModEntityData {
    public static final String INSTRUMENT_OPEN_TAG_NAME = "instrumentOpen";


    public static CompoundTag getModTag(final Entity entity) {
        return ((IEntityModData)entity).getPersistentData();
    }


    public static boolean isInstrumentOpen(final Player player) {
        final CompoundTag modData = getModTag(player);
        return modData.contains(INSTRUMENT_OPEN_TAG_NAME, CompoundTag.TAG_BYTE)
            ? modData.getBoolean(INSTRUMENT_OPEN_TAG_NAME)
            : false;
    }

    public static void setInstrumentOpen(final Player player, final boolean isOpen) {
        getModTag(player).putBoolean(INSTRUMENT_OPEN_TAG_NAME, isOpen);
    }

}
