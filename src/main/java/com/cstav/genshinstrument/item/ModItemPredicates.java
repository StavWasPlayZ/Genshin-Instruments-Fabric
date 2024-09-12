package com.cstav.genshinstrument.item;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.util.InstrumentEntityData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ModItemPredicates {

    public static void register() {
        ItemProperties.registerGeneric(GInstrumentMod.loc("instrument_open"),
            ModItemPredicates::instrumentOpenPredicate
        );
    }

    /**
     * Derives {@link InstrumentEntityData} capability as item model predicate
     */
    public static float instrumentOpenPredicate(ItemStack pStack, ClientLevel pLevel, LivingEntity pEntity, int pSeed) {
        if (!(pEntity instanceof Player player))
            return 0;

        if (!InstrumentEntityData.isOpen(player) || !InstrumentEntityData.isItem(player))
            return 0;

        return ItemStack.matches(pStack, player.getItemInHand(InstrumentEntityData.getHand(player))) ? 1 : 0;
    }

}