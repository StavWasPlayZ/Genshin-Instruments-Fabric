package com.cstav.genshinstrument;

import com.cstav.genshinstrument.item.GIItems;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public abstract class GICreativeModeTabs {

    public static final CreativeModeTab
        INSTRUMENTS_TAB = FabricItemGroupBuilder.build(
        new ResourceLocation(GInstrumentMod.MODID, "instruments_tab"),
        () -> new ItemStack(GIItems.FLORAL_ZITHER)
    )
        ;

}
