package com.cstav.genshinstrument;

import com.cstav.genshinstrument.item.GIItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * A class holding and managing creative mode tabs for the
 * Genshin Instruments mod
 */
public abstract class GICreativeModeTabs {
    public static void load() {}

    /**
     * A priority-based map for mapping item placements in tabs.
     * Tab -> priority -> item.
     */
    private static final HashMap<ResourceKey<CreativeModeTab>, TreeMap<Integer, List<Item>>> TAB_MAP = new HashMap<>();

    /**
     * You are encouraged to add to this tab via {@link GICreativeModeTabs#addToTab}.
     */
    private static final ResourceKey<CreativeModeTab> INSTRUMENTS_TAB = register("instruments_tab",
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


    /**
     * Queues the item to the registration event of the Instruments tab
     * @param priority The priority sort of this item. 0 is designated to the GI mod, 1 to EMI.
     * @param item The desired item
     */
    public static void addToInstrumentsTab(int priority, Item item) {
        addToTab(priority, INSTRUMENTS_TAB, item);
    }

    private static void addToTab(int priority, CreativeModeTab tab, Item item) {
        addToTab(priority, BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).orElseThrow(), item);
    }
    /**
     * Queues the item to the registration event of the given tab
     * @param priority The priority sort of this item. 0 is designated to the GI mod, 1 to EMI.
     * @param tab The desired tab
     * @param item The desired item
     */
    public static void addToTab(int priority, ResourceKey<CreativeModeTab> tab, Item item) {
        if (!TAB_MAP.containsKey(tab)) {
            ItemGroupEvents.modifyEntriesEvent(tab).register((content) -> {
                TAB_MAP.get(tab).values().forEach((items) ->
                    items.forEach(content::accept)
                );
                TAB_MAP.remove(tab);
            });

            TAB_MAP.put(tab, new TreeMap<>());
        }

        TAB_MAP.get(tab).computeIfAbsent(priority, (p) -> new ArrayList<>()).add(item);
    }

}
