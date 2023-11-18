package com.cstav.genshinstrument.item;

import static com.cstav.genshinstrument.util.ServerUtil.sendInternalOpenPacket;

import com.cstav.genshinstrument.GInstrumentMod;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public abstract class ModItems {
    
    public static final Item
        WINDSONG_LYRE = register("windsong_lyre", new InstrumentItem(
            (player, hand) -> sendInternalOpenPacket(player, hand, "windsong_lyre")
        )),
        VINTAGE_LYRE = register("vintage_lyre", new InstrumentItem(
            (player, hand) -> sendInternalOpenPacket(player, hand, "vintage_lyre")
        )),
        FLORAL_ZITHER = register("floral_zither", new InstrumentItem(
            (player, hand) -> sendInternalOpenPacket(player, hand, "floral_zither")
        )),
        GLORIOUS_DRUM = register("glorious_drum", new InstrumentItem(
            (player, hand) -> sendInternalOpenPacket(player, hand, "glorious_drum")
        ))
    ;
    
    
    private static Item register(final String id, final Item item) {
        Registry.register(Registry.ITEM, new ResourceLocation(GInstrumentMod.MODID, id), item);
        return item;
    }


    
    public static void load() {
        GInstrumentMod.LOGGER.info("Registered all instrument items");
    }

}
