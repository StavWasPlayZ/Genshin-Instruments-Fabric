package com.cstav.genshinstrument.client;

import java.awt.Color;
import java.awt.Point;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ClientUtil {
    
    /**
     * @return The point in the center of the described widget
     */
    public static Point getInitCenter(int initX, int initY, int initSize, int currSize) {
        return new Point(
            (initSize - currSize) / 2 + initX,
            (initSize - currSize) / 2 + initY
        );
    }

    public static void setShaderColor(final Color color, final float alpha) {
        RenderSystem.setShaderColor(
            color.getRed() / 255f,
            color.getGreen() / 255f,
            color.getBlue() / 255f,
            alpha
        );
    }
    public static void setShaderColor(final Color color) {
        setShaderColor(color, 1);
    }
    public static void resetShaderColor() {
        setShaderColor(Color.WHITE);
    }


    public static void displaySprite(final ResourceLocation location) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, location);
    }

}
