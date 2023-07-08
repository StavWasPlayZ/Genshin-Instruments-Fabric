package com.cstav.genshinstrument.item;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.ModCreativeModeTabs;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public abstract class ModItems {
    
    public static final Item
        WINDSONG_LYRE = register("windsong_lyre", new InstrumentItem(
            (player, hand) -> InstrumentItem.sendOpenRequest(player, hand, "windsong_lyre")
        )),
        VINTAGE_LYRE = register("vintage_lyre", new InstrumentItem(
            (player, hand) -> InstrumentItem.sendOpenRequest(player, hand, "vintage_lyre")
        )),
        FLORAL_ZITHER = register("floral_zither", new InstrumentItem(
            (player, hand) -> InstrumentItem.sendOpenRequest(player, hand, "floral_zither")
        )),
        GLORIOUS_DRUM = register("glorious_drum", new InstrumentItem(
            (player, hand) -> InstrumentItem.sendOpenRequest(player, hand, "glorious_drum")
        ))
    ;
    
    
    private static Item register(final String id, final Item item) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(GInstrumentMod.MODID, id), item);
        addToItemGroups(item);

        return item;
    }


    private static void addToItemGroups(final Item item) {
        // All shall go to the instruments and toold tab
        addToItemGroup(ModCreativeModeTabs.INSTRUMENTS, item);
        addToItemGroup(CreativeModeTabs.TOOLS_AND_UTILITIES, item);
    }

    private static void addToItemGroup(final CreativeModeTab tab, final Item item) {
        // addToItemGroup(Registries.CREATIVE_MODE_TAB., item);
    }
    private static void addToItemGroup(final ResourceKey<CreativeModeTab> group, final Item item) {
        // All shall go to the instruments and toold tab
        ItemGroupEvents.modifyEntriesEvent(group).register((content) -> content.accept(item));
    }


    
    public static void register() {
        GInstrumentMod.LOGGER.info("Registered all instrument items");
    }

}
