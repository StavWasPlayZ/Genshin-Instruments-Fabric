package com.cstav.genshinstrument;

import com.cstav.genshinstrument.item.ModItems;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTabs {
    
    public static final CreativeModeTab
        INSTRUMENTS = FabricItemGroup.builder(new ResourceLocation(GInstrumentMod.MODID, "instruments_group"))
            .icon(() -> new ItemStack(ModItems.FLORAL_ZITHER))
            .title(Component.translatable("genshinstrument.itemGroup.instruments"))
            .build()
    ;

}
