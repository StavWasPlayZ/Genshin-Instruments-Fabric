package com.cstav.genshinstrument.item;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.ModItemGroup;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public abstract class ModItems {
    
    public static final Item
        WINDSONG_LYRE = register("windsong_lyre", new InstrumentItem(new Settings())),
        VINTAGE_LYRE = register("vintage_lyre", new InstrumentItem(new Settings())),
        FLORAL_ZITHER = register("floral_zither", new InstrumentItem(new Settings())),
        GLORIOUS_DRUM = register("glorious_drum", new InstrumentItem(new Settings()))
    ;
    
    
    private static Item register(final String id, final Item item) {
        Registry.register(Registries.ITEM, new Identifier(GInstrumentMod.MODID, id), item);
        addToItemGroups(item);

        return item;
    }


    private static void addToItemGroups(final Item item) {
        // All shall go to the instruments and toold tab
        addToItemGroup(ModItemGroup.INSTRUMENTS, item);
        addToItemGroup(ItemGroups.TOOLS, item);
    }

    private static void addToItemGroup(final ItemGroup group, final Item item) {
        addToItemGroup(Registries.ITEM_GROUP.getKey(group).get(), item);
    }
    private static void addToItemGroup(final RegistryKey<ItemGroup> group, final Item item) {
        // All shall go to the instruments and toold tab
        ItemGroupEvents.modifyEntriesEvent(group).register((content) -> content.add(item));
    }


    
    public static void register() {
        GInstrumentMod.LOGGER.info("Registered instruments under " + GInstrumentMod.MODID);
    }

}
