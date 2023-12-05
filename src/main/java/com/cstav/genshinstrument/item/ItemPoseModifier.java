package com.cstav.genshinstrument.item;

import com.cstav.genshinstrument.event.PosePlayerArmEvent.PosePlayerArmEventArgs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface ItemPoseModifier {
    
    @Environment(EnvType.CLIENT)
    void onPosePlayerArm(final PosePlayerArmEventArgs args);

}
