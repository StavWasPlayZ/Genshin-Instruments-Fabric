package com.cstav.genshinstrument.block;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.block.partial.InstrumentBlockEntity;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class ModBlockEntities {
    public static void load() {};

    public static final BlockEntityType<InstrumentBlockEntity> INSTRUMENT_BE = regsiter("instrument_be",
        BlockEntityType.Builder.of((pos, state) -> new InstrumentBlockEntity(pos, state), ModBlocks.LYRE_BLOCK)
            .build(null)
    );
    
    private static <T extends BlockEntity> BlockEntityType<T> regsiter(final String name, final BlockEntityType<T> bet) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(GInstrumentMod.MODID, name), bet);
        return bet;
    }
}
