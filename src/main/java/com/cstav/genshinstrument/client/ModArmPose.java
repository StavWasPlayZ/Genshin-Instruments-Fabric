// //TODO implement when new armpose system is implemented
// //NOTE: Load on ClientInitiator if done through mixin
// package com.cstav.genshinstrument.client;

// import net.fabricmc.api.EnvType;
// import net.fabricmc.api.Environment;
// import net.minecraft.client.model.HumanoidModel;
// import net.minecraft.client.model.HumanoidModel.ArmPose;
// import net.minecraft.world.entity.HumanoidArm;
// import net.minecraft.world.entity.LivingEntity;
// import net.minecraftforge.client.IArmPoseTransformer;

// @Environment(EnvType.CLIENT)
// public abstract class ModArmPose {
//     public static final float HAND_HEIGHT_ROT = .9f;

//     public static void register() {}


//     public static final ArmPose PLAYING_ITEM_INSTRUMENT = ArmPose.create("playing_item_instrument", true, new IArmPoseTransformer() {

//         @Override
//         public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
//             model.rightArm.xRot = -HAND_HEIGHT_ROT;
//             model.rightArm.zRot = -0.35f;

//             model.leftArm.xRot = -HAND_HEIGHT_ROT;
//             model.leftArm.zRot = 0.85f;
//         }

//     });
//     public static final ArmPose PLAYING_BLOCK_INSTRUMENT = ArmPose.create("playing_block_instrument", true, new IArmPoseTransformer() {

//         @Override
//         public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
//             model.rightArm.xRot = -HAND_HEIGHT_ROT;

//             model.leftArm.xRot = -HAND_HEIGHT_ROT;
//         }

//     });

// }