package com.cstav.genshinstrument.block;

import com.cstav.genshinstrument.GInstrumentMod;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public abstract class ModBlocks {
    public static void load() {};

    //NOTE for testing purposes
    public static final Block
        LYRE_BLOCK = register("lyre_block", new LyreInstrumentBlock(Properties.copy(Blocks.OAK_WOOD)))
    ;


    private static Block register(final String name, final Block block) {
        Registry.register(Registry.BLOCK, new ResourceLocation(GInstrumentMod.MODID, name), block);
        return block;
    }

}
