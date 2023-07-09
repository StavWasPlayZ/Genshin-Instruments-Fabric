package com.cstav.genshinstrument.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.cstav.genshinstrument.item.InstrumentItem;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

@Mixin(HumanoidModel.class)
public abstract class InstrumentAnimMixin {

	@Shadow
	private ModelPart rightArm;
	@Shadow
   	private ModelPart leftArm;

	@Inject(at = @At("HEAD"), method = "poseLeftArm", cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
	private void injectInstrumentUseAnim(LivingEntity entity, CallbackInfo info) {
		boolean isInRight = isInstrumentInHand(entity, InteractionHand.MAIN_HAND),
			isInLeft = isInstrumentInHand(entity, InteractionHand.OFF_HAND);

		if (!(isInLeft || isInRight))
			return;

		//TODO: Add some checks for it to play only when the player is playing

		// Think of it as a static final constant
		final float HAND_HEIGHT_ROT = .9f;

		
		rightArm.xRot = -HAND_HEIGHT_ROT;
		rightArm.zRot = -0.35f;
		
		leftArm.xRot = -HAND_HEIGHT_ROT;
		leftArm.zRot = 0.85f;

		info.cancel();
	}


	private static boolean isInstrumentInHand(final LivingEntity entity, final InteractionHand hand) {
		return entity.getItemInHand(hand).getItem() instanceof InstrumentItem;
	}

}