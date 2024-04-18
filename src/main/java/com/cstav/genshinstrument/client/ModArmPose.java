package com.cstav.genshinstrument.client;

import com.cstav.genshinstrument.event.PosePlayerArmEvent.HandType;
import com.cstav.genshinstrument.event.PosePlayerArmEvent.PosePlayerArmEventArgs;
import com.cstav.genshinstrument.util.InstrumentEntityData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;

@Environment(EnvType.CLIENT)
public abstract class ModArmPose {
    public static final float HAND_HEIGHT_ROT = .9f;

    public static void poseForItemInstrument(final PosePlayerArmEventArgs args) {
        if (!InstrumentEntityData.isOpen(args.player) || !InstrumentEntityData.isItem(args.player))
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

    public static void poseForWindInstrument(final PosePlayerArmEventArgs args) {
        if (!InstrumentEntityData.isOpen(args.player) || !InstrumentEntityData.isItem(args.player))
			return;

        if (args.hand == HandType.RIGHT) {
            args.arm.xRot = -1.5f;
            args.arm.zRot = -0.35f;
            args.arm.yRot = -0.5f;
        } else {
            args.arm.xRot = -1.5f;
            args.arm.zRot = 0.55f;
            args.arm.yRot = 0.5f;
        }

        args.setCanceled(true);
    }


    public static void poseForBlockInstrument(final PosePlayerArmEventArgs args) {            
        args.arm.xRot = -HAND_HEIGHT_ROT;

        args.setCanceled(true);
    }

}