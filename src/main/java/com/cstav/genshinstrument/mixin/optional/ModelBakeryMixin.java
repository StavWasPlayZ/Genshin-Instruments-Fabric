package com.cstav.genshinstrument.mixin.optional;

import com.cstav.genshinstrument.event.RegisterAdditionalModelsEvent;
import com.cstav.genshinstrument.event.RegisterAdditionalModelsEvent.RegisterAdditionalModelsEventArgs;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelBakery.LoadedJson;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Invoker
    public abstract void invokeLoadTopLevel(ModelResourceLocation location);

    @Inject(method = "<init>", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/resources/model/ModelBakery;loadTopLevel(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V",
        ordinal = 3,
        shift = Shift.AFTER
    ))
    private void initInjector(BlockColors blockColors, ProfilerFiller profilerFiller, Map<ResourceLocation, BlockModel> modelResources, Map<ResourceLocation, List<LoadedJson>> blockStateResources, CallbackInfo ci) {
        final RegisterAdditionalModelsEventArgs args = new RegisterAdditionalModelsEventArgs();
        RegisterAdditionalModelsEvent.EVENT.invoker().triggered(args);

        args.getModels().forEach(this::invokeLoadTopLevel);
    }

}
