package com.cstav.genshinstrument.criteria;


import com.cstav.genshinstrument.event.InstrumentPlayedEvent;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModCriteria {

    public static final InstrumentPlayedTrigger INSTRUMENT_PLAYED_TRIGGER = CriteriaTriggers.register(new InstrumentPlayedTrigger());

    public static void register() {
        InstrumentPlayedEvent.EVENT.register((args) -> {
            if (args.level().isClientSide)
                return;
            // Only get player events
            if (args.playerInfo().isEmpty())
                return;

            final Item instrument = BuiltInRegistries.ITEM.get(args.soundMeta().instrumentId());
            // Perhaps troll packets
            if (instrument == Items.AIR)
                return;

            INSTRUMENT_PLAYED_TRIGGER.trigger(
                (ServerPlayer) args.playerInfo().get().player,
                new ItemStack(instrument)
            );
        });
    }
    
}