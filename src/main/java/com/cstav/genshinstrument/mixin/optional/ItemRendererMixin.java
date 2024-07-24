package com.cstav.genshinstrument.mixin.optional;

import com.cstav.genshinstrument.event.ModifyItemRendersModelEvent;
import com.cstav.genshinstrument.event.ModifyItemRendersModelEvent.ModifyItemRendersModelEventArgs;
import com.cstav.genshinstrument.item.GIItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Final
    @Shadow
    private ItemModelShaper itemModelShaper;

    @ModifyVariable(at = @At("HEAD"), method = "render", argsOnly = true)
    private BakedModel injectRender(BakedModel model, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        final ModifyItemRendersModelEventArgs args = new ModifyItemRendersModelEventArgs(
            model, itemStack, displayContext, itemModelShaper
        );
        ModifyItemRendersModelEvent.EVENT.invoker().triggered(args);

        return args.getModel();
    }

}