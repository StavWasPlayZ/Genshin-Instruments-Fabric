package com.cstav.genshinstrument.client;

import java.awt.Color;
import java.awt.Point;

import com.cstav.genshinstrument.client.gui.screens.options.widget.copied.GridWidget;
import com.cstav.genshinstrument.client.keyMaps.InstrumentKeyMappings;
import com.mojang.blaze3d.platform.InputConstants.Key;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ClientUtil {
    public static final int GRID_HORZ_PADDING = 4, GRID_VERT_PADDING = 2;


    private static Boolean onQwerty;
    public static boolean isOnQwerty() {
        if (onQwerty != null)
            return onQwerty;


        final String qwerty = "QWERTY";
        final Key[] keyRow = InstrumentKeyMappings.GRID_INSTRUMENT_MAPPINGS[0];
    
        // Assuming there will be more than 6 entries here
        for (int i = 0; i < qwerty.length(); i++) {
            if (qwerty.charAt(i) != keyRow[i].getDisplayName().getString(1).charAt(0))
                return onQwerty = false;
        }
    
        return onQwerty = true;
    }
    
    
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

    public static GridWidget createSettingsGrid() {
        final GridWidget grid = new GridWidget();
        grid.defaultCellSetting()
            .padding(GRID_HORZ_PADDING, GRID_VERT_PADDING)
            .alignVertically(.5f)
            .alignHorizontallyCenter();

        return grid;
    }

    public static void alignGrid(GridWidget grid, int screenWidth, int screenHeight) {
        grid.pack();
        grid.setX((screenWidth - grid.getWidth()) / 2);
        grid.setY(30);
        grid.pack();
    }

    public static int lowerButtonsY(int desiredY, int desiredHeight, int screenHeight) {
        return Math.min(desiredY + desiredHeight + 50, screenHeight - 20 - 15);
    }

}
