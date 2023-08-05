package com.cstav.genshinstrument.mixin.optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cstav.genshinstrument.event.PosePlayerArmEvent;
import com.cstav.genshinstrument.event.PosePlayerArmEvent.HandType;
import com.cstav.genshinstrument.event.PosePlayerArmEvent.PosePlayerArmEventArgs;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

//TODO convert to event that must return arm pose of sorts
@Mixin(HumanoidModel.class)
public abstract class InstrumentAnimMixin {

	@Shadow
	private ModelPart rightArm;
	@Shadow
   	private ModelPart leftArm;

	
	@Inject(at = @At("HEAD"), method = "poseLeftArm", cancellable = true)
	private void injectLeftArmPose(LivingEntity entity, CallbackInfo info) {
		if (!(entity instanceof Player player))
            return;

		final PosePlayerArmEventArgs args = new PosePlayerArmEventArgs(player, HandType.LEFT, leftArm);
		PosePlayerArmEvent.EVENT.invoker().triggered(args);

		if (args.isCanceled())
			info.cancel();
	}

	@Inject(at = @At("HEAD"), method = "poseRightArm", cancellable = true)
	private void injectRightArmPose(LivingEntity entity, CallbackInfo info) {
		if (!(entity instanceof Player player))
            return;

		final PosePlayerArmEventArgs args = new PosePlayerArmEventArgs(player, HandType.RIGHT, rightArm);
		PosePlayerArmEvent.EVENT.invoker().triggered(args);

		if (args.isCanceled())
			info.cancel();
	}

}