package com.cstav.genshinstrument.item;

import com.cstav.genshinstrument.GICreativeModeTabs;
import com.cstav.genshinstrument.GInstrumentMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
        Registry.register(BuiltInRegistries.ITEM, GInstrumentMod.loc(id), item);
        addToItemGroups(item);

        return item;
    }


    private static void addToItemGroups(final Item item) {
        // All shall go to the instruments and tools tab
        GICreativeModeTabs.addToInstrumentsTab(0, item);
        GICreativeModeTabs.addToTab(0, CreativeModeTabs.TOOLS_AND_UTILITIES, item);
    }


    private static ResourceLocation loc(final String path) {
        return GInstrumentMod.loc(path);
    }

    
    public static void load() {
        GInstrumentMod.LOGGER.info("Registered all instrument items");
    }

}
