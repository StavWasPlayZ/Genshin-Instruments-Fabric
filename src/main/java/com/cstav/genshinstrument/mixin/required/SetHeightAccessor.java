package com.cstav.genshinstrument.mixin.required;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.cstav.genshinstrument.mixin.util.ISetHeightAccessor;

import net.minecraft.client.gui.components.AbstractWidget;

@Mixin(AbstractWidget.class)
public abstract class SetHeightAccessor implements ISetHeightAccessor {
    
    @Shadow
    private int height;

    @Override
    public void setHeight(final int height) {
        this.height = height;
    }

}
