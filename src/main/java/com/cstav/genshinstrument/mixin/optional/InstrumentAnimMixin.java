package com.cstav.genshinstrument.mixin.optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cstav.genshinstrument.item.InstrumentItem;
import com.cstav.genshinstrument.mixin.util.MixinConstants;
import com.cstav.genshinstrument.util.ModEntityData;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

//TODO convert to event that must return arm pose of sorts
@Mixin(HumanoidModel.class)
public abstract class InstrumentAnimMixin {

	@Shadow
	private ModelPart rightArm;
	@Shadow
   	private ModelPart leftArm;

	//TODO find a method that is for both hands or split them
	@Inject(at = @At("HEAD"), method = "poseLeftArm", cancellable = true)
	private void injectInstrumentUseAnim(LivingEntity entity, CallbackInfo info) {
		if (!(entity instanceof Player))
            return;

		boolean isInRight = isInstrumentInHand(entity, InteractionHand.MAIN_HAND),
			isInLeft = isInstrumentInHand(entity, InteractionHand.OFF_HAND);

		if (!(isInLeft || isInRight))
			return;

		if (!ModEntityData.isInstrumentOpen((Player)entity))
			return;

		
		rightArm.xRot = -MixinConstants.HAND_HEIGHT_ROT;
		rightArm.zRot = -0.35f;
		
		leftArm.xRot = -MixinConstants.HAND_HEIGHT_ROT;
		leftArm.zRot = 0.85f;

		info.cancel();
	}


	private static boolean isInstrumentInHand(final LivingEntity entity, final InteractionHand hand) {
		return entity.getItemInHand(hand).getItem() instanceof InstrumentItem;
	}

}