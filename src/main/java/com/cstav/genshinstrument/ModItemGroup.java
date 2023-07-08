package com.cstav.genshinstrument;

import com.cstav.genshinstrument.item.ModItems;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    
    public static final ItemGroup INSTRUMENTS = FabricItemGroup.builder()
        //TODO: Change to Zither once it is added
        .icon(() -> new ItemStack(ModItems.WINDSONG_LYRE))
        .displayName(Text.translatable("genshinstrument.itemGroup.instruments"))
        .build();

    public static void regsiter() {
        Registry.register(Registries.ITEM_GROUP, new Identifier(GInstrumentMod.MODID, "instruments_group"), INSTRUMENTS);
    }

}
