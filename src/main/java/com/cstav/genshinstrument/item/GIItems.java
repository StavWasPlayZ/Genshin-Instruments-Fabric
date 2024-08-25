package com.cstav.genshinstrument.item;

import com.cstav.genshinstrument.GICreativeModeTabs;
import com.cstav.genshinstrument.GInstrumentMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import static com.cstav.genshinstrument.networking.packet.instrument.util.InstrumentPacketUtil.sendOpenPacket;

public class GIItems {
    
    public static final Item
        WINDSONG_LYRE = register("windsong_lyre", new InstrumentItem(
            (player) -> sendOpenPacket(player, loc("windsong_lyre"))
        )),
        VINTAGE_LYRE = register("vintage_lyre", new InstrumentItem(
            (player) -> sendOpenPacket(player, loc("vintage_lyre"))
        )),
        FLORAL_ZITHER = register("floral_zither", new InstrumentItem(
            (player) -> sendOpenPacket(player, loc("floral_zither"))
        )),
        GLORIOUS_DRUM = register("glorious_drum", new InstrumentItem(
            (player) -> sendOpenPacket(player, loc("glorious_drum"))
        )),

        NIGHTWIND_HORN = register("nightwind_horn", new NightwindHornItem(
            (player) -> sendOpenPacket(player, loc("nightwind_horn"))
        ))
    ;
    
    
    private static Item register(final String id, final Item item) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(GInstrumentMod.MODID, id), item);
        addToItemGroups(item);

        return item;
    }


    private static void addToItemGroups(final Item item) {
        // All shall go to the instruments and tools tab
        addToTab(GICreativeModeTabs.INSTRUMENTS_TAB, item);
        addToTab(CreativeModeTabs.TOOLS_AND_UTILITIES, item);
    }

    private static void addToTab(final CreativeModeTab tab, final Item item) {
        addToTab(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).get(), item);
    }
    private static void addToTab(final ResourceKey<CreativeModeTab> tab, final Item item) {
        ItemGroupEvents.modifyEntriesEvent(tab).register((content) -> content.accept(item));
    }


    private static ResourceLocation loc(final String path) {
        return new ResourceLocation(GInstrumentMod.MODID, path);
    }

    
    public static void load() {
        GInstrumentMod.LOGGER.info("Registered all instrument items");
    }

}
