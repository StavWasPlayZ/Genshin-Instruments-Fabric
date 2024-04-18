package com.cstav.genshinstrument.criteria;


import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ModCriteria {

    public static final InstrumentPlayedTrigger INSTRUMENT_PLAYED_TRIGGER = register("instrument_played", new InstrumentPlayedTrigger());

    private static <T extends CriterionTrigger<?>> T register(String name, T criterionTrigger) {
        return Registry.register(
            BuiltInRegistries.TRIGGER_TYPES,
            new ResourceLocation(GInstrumentMod.MODID, name),
            criterionTrigger
        );
    }


    public static void register() {
        InstrumentPlayedEvent.ByPlayer.EVENT.register((args) -> {
            if (!args.level.isClientSide)
                INSTRUMENT_PLAYED_TRIGGER.trigger(
                    (ServerPlayer)args.player,
                    new ItemStack(BuiltInRegistries.ITEM.get(args.instrumentId))
                );
        });
    }
    
}