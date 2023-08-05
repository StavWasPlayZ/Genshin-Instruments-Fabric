package com.cstav.genshinstrument.mixin.util;

import java.util.Optional;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public abstract class CommonUtil {

    @SuppressWarnings("unchecked")
    public static <T extends Item> Optional<T> getItemInHands(final Class<T> item, final Player player) {
        final Item mainItem = player.getItemInHand(InteractionHand.MAIN_HAND).getItem(),
            offItem = player.getItemInHand(InteractionHand.OFF_HAND).getItem();

        if (item.isInstance(mainItem))
            return Optional.of((T)mainItem);
        else if (item.isInstance(offItem))
            return Optional.of((T)offItem);

        return Optional.empty();
    }
    
}
