package com.cstav.genshinstrument.client.gui.screen.options.instrument.partial;

import java.awt.Color;
import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.InstrumentScreen;
import com.cstav.genshinstrument.client.util.ClientUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public abstract class AbstractInstrumentOptionsScreen extends Screen {

    public final @Nullable InstrumentScreen instrumentScreen;
    public final Screen lastScreen;

    public final boolean isOverlay;

    public AbstractInstrumentOptionsScreen(Component pTitle, InstrumentScreen instrumentScreen, Screen lastScreen) {
        super(pTitle);
        this.instrumentScreen = instrumentScreen;
        this.lastScreen = lastScreen;

        this.isOverlay = instrumentScreen != null;
    }
    public AbstractInstrumentOptionsScreen(Component pTitle, InstrumentScreen instrumentScreen) {
        this(pTitle, instrumentScreen, null);
    }
    public AbstractInstrumentOptionsScreen(Component pTitle, Screen lastScreen) {
        this(pTitle, null, lastScreen);
    }


    public int getSmallButtonWidth() {
        return 190;
    }
    public int getBigButtonWidth() {
        return (getSmallButtonWidth() + ClientUtil.GRID_HORZ_PADDING) * 2;
    }
    public int getButtonHeight() {
        return 20;
    }


    @Override
    public void render(GuiGraphics gui, int pMouseX, int pMouseY, float pPartialTick) {
        if (isOverlay) {
            instrumentScreen.render(gui, Integer.MAX_VALUE, Integer.MAX_VALUE, pPartialTick);
            // Push the options screen infront
            gui.pose().translate(0, 0, 1);
        }
        
        
        renderBackground(gui);
        gui.drawCenteredString(font, title, width/2, 15, Color.WHITE.getRGB());
        
        super.render(gui, pMouseX, pMouseY, pPartialTick);
    }


    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        // Pass keys to the instrument screen if they are consumed
        if (isOverlay && instrumentScreen.isKeyConsumed(pKeyCode, pScanCode))
            instrumentScreen.keyPressed(pKeyCode, pScanCode, pModifiers);

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        if (isOverlay && instrumentScreen.isKeyConsumed(pKeyCode, pScanCode))
            instrumentScreen.keyReleased(pKeyCode, pScanCode, pModifiers);

        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }

    // Also resizing
    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        if (isOverlay)
            instrumentScreen.resize(minecraft, width, height);
            
        super.resize(minecraft, width, height);
    }


    @Override
    public boolean isPauseScreen() {
        return instrumentScreen == null;
    }

    @Override
    public void onClose() {
        saveOptions();

        if (lastScreen != null)
            minecraft.setScreen(lastScreen);
        else
            super.onClose();
    }


    /* ---------------- Save System --------------- */

    protected final HashMap<String, Runnable> appliedOptions = new HashMap<>();

    /**
     * Queues the given option to later be saved.
     * Most notably, a save occurs when the client closes this screen.
     * @param optionKey A unique identifier of this option. If a duplicate entry
     * exists, it will be overwritten.
     * @param saveRunnable The runnable for saving the option
     */
    protected void queueToSave(String optionKey, final Runnable saveRunnable) {
        final String modId = modId();
        if (modId != null)
            optionKey = modId + ":" + optionKey;

        if (appliedOptions.containsKey(optionKey))
            appliedOptions.replace(optionKey, saveRunnable);
        else
            appliedOptions.put(optionKey, saveRunnable);
    }

    protected void saveOptions() {
        if (appliedOptions.isEmpty())
            return;

        appliedOptions.values().forEach(Runnable::run);
        ModClientConfigs.CONFIGS.save();

        GInstrumentMod.LOGGER.info("Successfully saved "+appliedOptions.size()+" option(s) for "+title.getString());
    }


    /**
     * Fetches the Mod ID of the instrument being used
     * @apiNote Should be overwritten in the case of not being used by an instrument
     */
    public String modId() {
        return isOverlay ? instrumentScreen.getModId() : null;
    }
    
}
