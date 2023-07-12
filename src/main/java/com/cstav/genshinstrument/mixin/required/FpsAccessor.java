package com.cstav.genshinstrument.mixin.required;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.cstav.genshinstrument.mixin.util.IFpsAccessor;

import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public abstract class FpsAccessor implements IFpsAccessor {
    
    @Shadow
    private static int fps;

    @Override
    public int getFps() {
        return fps;
    }

}
