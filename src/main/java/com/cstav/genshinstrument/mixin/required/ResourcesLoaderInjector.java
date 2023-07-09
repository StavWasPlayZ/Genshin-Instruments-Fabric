package com.cstav.genshinstrument.mixin.required;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cstav.genshinstrument.event.ResourcesLoadedEvent;
import com.cstav.genshinstrument.event.impl.EventArgs;

import net.minecraft.client.ResourceLoadStateTracker;

@Mixin(ResourceLoadStateTracker.class)
public abstract class ResourcesLoaderInjector {
    
    @Inject(method = "finishReload", at = @At("HEAD"))
    private void onReloadComplete(final CallbackInfo info) {
        ResourcesLoadedEvent.EVENT.invoker().triggered(new EventArgs.Empty());
    }

}
