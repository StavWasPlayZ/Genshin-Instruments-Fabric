package com.cstav.genshinstrument;

import com.cstav.genshinstrument.item.ModItems;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public abstract class ModCreativeModeTabs {
    
    public static final CreativeModeTab INSTRUMENTS_TAB = FabricItemGroup.builder()
        .icon(() -> new ItemStack(ModItems.FLORAL_ZITHER))
        .title(Component.translatable("genshinstrument.itemGroup.instruments"))
        .build();

    public static void regsiter() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(GInstrumentMod.MODID, "instruments_tab"), INSTRUMENTS_TAB
        );
    }

}
