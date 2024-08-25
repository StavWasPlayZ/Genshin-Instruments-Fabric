package com.cstav.genshinstrument.sound.registrar.impl;

import net.minecraft.resources.ResourceLocation;

public abstract class AbstractNoteSoundRegistrar<T, R extends AbstractNoteSoundRegistrar<T, R>> {
    protected final ResourceLocation baseSoundLocation;

    public AbstractNoteSoundRegistrar(ResourceLocation baseSoundLocation) {
        this.baseSoundLocation = baseSoundLocation;
    }

    public abstract R getThis();
}
