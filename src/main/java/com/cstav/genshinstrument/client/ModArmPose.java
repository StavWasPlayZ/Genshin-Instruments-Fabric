package com.cstav.genshinstrument.client;

import com.cstav.genshinstrument.event.PosePlayerArmEvent.HandType;
import com.cstav.genshinstrument.event.PosePlayerArmEvent.PosePlayerArmEventArgs;
import com.cstav.genshinstrument.util.InstrumentEntityData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;

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

    public static void poseForBlockInstrument(final PosePlayerArmEventArgs args) {
        args.arm.xRot = -HAND_HEIGHT_ROT;

        args.setCanceled(true);
    }


    /**Applies the default*/
    private static void defRightWind(ModelPart arm) {
        arm.xRot = -1.5f;
        arm.zRot = -0.35f;
        arm.yRot = -0.5f;
    }
    private static void defLeftWind(ModelPart arm) {
        arm.xRot = -1.5f;
        arm.zRot = 0.55f;
        arm.yRot = 0.5f;
    }

    public static void poseForWindInstrument(final PosePlayerArmEventArgs args) {
        if (!InstrumentEntityData.isOpen(args.player) || !InstrumentEntityData.isItem(args.player))
			return;

        if (args.hand == HandType.RIGHT) {
            defRightWind(args.arm);
        } else {
            defLeftWind(args.arm);
        }

        args.setCanceled(true);
    }

    public static void poseForNightwindHornInstrument(final PosePlayerArmEventArgs args) {
        if (args.hand == HandType.RIGHT) {
            defRightWind(args.arm);

            args.model.leftArm.xRot = -1.65f;
            args.model.leftArm.zRot = -0.1f;
            args.model.leftArm.yRot = -0.1f;
        } else {
            defLeftWind(args.arm);

            args.model.rightArm.xRot = -1.65f;
            args.model.rightArm.zRot = 0.1f;
            args.model.rightArm.yRot = 0;
        }
    }

}