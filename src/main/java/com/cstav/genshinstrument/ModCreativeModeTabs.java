package com.cstav.genshinstrument;

import com.cstav.genshinstrument.item.ModItems;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {
    
    public static final CreativeModeTab
        INSTRUMENTS = FabricItemGroupBuilder.build(
            new ResourceLocation(GInstrumentMod.MODID, "instruments_group"),
            () -> new ItemStack(ModItems.FLORAL_ZITHER)
        )
    ;

}
