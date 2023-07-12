package com.cstav.genshinstrument.client.gui.screens.options.widget.copied;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;

@Environment(EnvType.CLIENT)
public interface OnPress {
    void onPress(Button pButton);
}