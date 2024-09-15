package com.cstav.genshinstrument.mixin.optional;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    // This was present in 1.20.6 and below for blurring.
    // Was removed in 1.21 for some reason.

    @Inject(method = "processBlurEffect", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/renderer/PostChain;setUniform(Ljava/lang/String;F)V",
        shift = Shift.BEFORE
    ))
    private void processBlurEffectInjectorBeforeUniform(float f, CallbackInfo ci) {
        RenderSystem.enableBlend();
    }

    @Inject(method = "processBlurEffect", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/renderer/PostChain;setUniform(Ljava/lang/String;F)V",
        shift = Shift.AFTER
    ))
    private void processBlurEffectInjectorAfterProcess(float f, CallbackInfo ci) {
        RenderSystem.disableBlend();
    }

}
