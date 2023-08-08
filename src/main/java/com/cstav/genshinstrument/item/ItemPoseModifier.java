package com.cstav.genshinstrument.item;

import com.cstav.genshinstrument.event.PosePlayerArmEvent;
import com.cstav.genshinstrument.event.PosePlayerArmEvent.PosePlayerArmEventArgs;
import com.cstav.genshinstrument.util.CommonUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface ItemPoseModifier {

    @Environment(EnvType.CLIENT)
    public static void register() {
        PosePlayerArmEvent.EVENT.register((args) -> {

            CommonUtil.getItemInHands(ItemPoseModifier.class, args.player).ifPresent((item) ->
                item.onPosePlayerArm(args)
            );

        });
    }
    
    @Environment(EnvType.CLIENT)
    void onPosePlayerArm(final PosePlayerArmEventArgs args);

}
