package com.cstav.genshinstrument.client.gui.screens.options.widget.copied;

import java.util.function.Supplier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.MutableComponent;

@Environment(EnvType.CLIENT)
public interface CreateNarration {
    MutableComponent createNarrationMessage(Supplier<MutableComponent> p_253695_);
}