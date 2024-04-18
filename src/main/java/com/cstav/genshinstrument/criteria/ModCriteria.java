package com.cstav.genshinstrument.criteria;


import com.cstav.genshinstrument.event.InstrumentPlayedEvent;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ModCriteria {

    public static final InstrumentPlayedTrigger INSTRUMENT_PLAYED_TRIGGER = CriteriaTriggers.register(new InstrumentPlayedTrigger());

    public static void register() {
        InstrumentPlayedEvent.ByPlayer.EVENT.register((args) -> {
            if (!args.level.isClientSide)
                INSTRUMENT_PLAYED_TRIGGER.trigger((ServerPlayer)args.player, new ItemStack(BuiltInRegistries.ITEM.get(args.instrumentId)));
        });
    }
    
}