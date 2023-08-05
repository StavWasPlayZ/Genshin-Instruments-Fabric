package com.cstav.genshinstrument.block.partial.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel.ArmPose;

@Environment(EnvType.CLIENT)
public interface IClientArmPoseProvider {
    //TODO change ArmPose to a different custom thing
    // Or look how to implement custom enums on mixins
    ArmPose getArmPose();
}