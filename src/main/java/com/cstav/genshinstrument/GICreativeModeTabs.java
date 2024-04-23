package com.cstav.genshinstrument;

import com.cstav.genshinstrument.item.GIItems;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public abstract class GICreativeModeTabs {
    public static void load() {}
    
    public static final ResourceKey<CreativeModeTab> INSTRUMENTS_TAB = register("instruments_tab",
        FabricItemGroup.builder()
            .icon(() -> new ItemStack(GIItems.FLORAL_ZITHER))
            .title(Component.translatable("genshinstrument.itemGroup.instruments"))
            .build()
    );

    private static ResourceKey<CreativeModeTab> register(final String name, final CreativeModeTab tab) {
        final ResourceKey<CreativeModeTab> tabKey = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            new ResourceLocation(GInstrumentMod.MODID, name)
        );

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, tabKey, tab);

        return tabKey;
    }

}
