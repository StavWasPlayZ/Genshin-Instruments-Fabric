package com.cstav.genshinstrument.block;

public abstract class ModBlockEntities {
    public static void load() {};

    // public static final BlockEntityType<InstrumentBlockEntity> INSTRUMENT_BE = regsiter("instrument_be",
    //     BlockEntityType.Builder.of((pos, state) -> new InstrumentBlockEntity(pos, state), ModBlocks.LYRE_BLOCK)
    //         .build(null)
    // );
    
    // private static <T extends BlockEntity> BlockEntityType<T> regsiter(final String name, final BlockEntityType<T> bet) {
    //     Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, GInstrumentMod.loc(name), bet);
    //     return bet;
    // }
}
