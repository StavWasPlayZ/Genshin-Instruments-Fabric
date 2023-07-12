package com.cstav.genshinstrument.client.gui.screens.options.widget.copied;

import org.joml.Vector2ic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;

@Environment(EnvType.CLIENT)
public interface ClientTooltipPositioner {
   Vector2ic positionTooltip(Screen pScreen, int pMouseX, int pMouseY, int pWidth, int pHeight);
}