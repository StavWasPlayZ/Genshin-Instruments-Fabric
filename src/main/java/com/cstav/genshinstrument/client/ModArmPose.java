package com.cstav.genshinstrument.client;

import com.cstav.genshinstrument.event.PosePlayerArmEvent.HandType;
import com.cstav.genshinstrument.event.PosePlayerArmEvent.PosePlayerArmEventArgs;
import com.cstav.genshinstrument.util.ModEntityData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;

@Environment(EnvType.CLIENT)
public abstract class ModArmPose {
    public static final float HAND_HEIGHT_ROT = .9f;

    public static void poseForItemInstrument(final PosePlayerArmEventArgs args) {
        if (!ModEntityData.isInstrumentOpen(args.player) || !ModEntityData.isInstrumentItem(args.player))
			return;
            
        final ModelPart arm = args.arm;
        if (args.hand == HandType.LEFT) {
            arm.xRot = -HAND_HEIGHT_ROT;
            arm.zRot = 0.85f;
        } else {
            arm.xRot = -HAND_HEIGHT_ROT;
            arm.zRot = -0.35f;
        }

        args.setCanceled(true);
    }

    public static void poseForBlockInstrument(final PosePlayerArmEventArgs args) {
        if (!ModEntityData.isInstrumentOpen(args.player) || ModEntityData.isInstrumentItem(args.player))
			return;
            
        args.arm.xRot = -HAND_HEIGHT_ROT;

        args.setCanceled(true);
    }

}