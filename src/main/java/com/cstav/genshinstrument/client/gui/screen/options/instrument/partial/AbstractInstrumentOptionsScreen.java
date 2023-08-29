package com.cstav.genshinstrument.client.gui.screen.options.instrument.partial;

import java.awt.Color;
import java.util.HashMap;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.cstav.genshinstrument.client.ClientUtil;
import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.AbstractInstrumentScreen;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance.TooltipSupplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public abstract class AbstractInstrumentOptionsScreen extends Screen {

    public final @Nullable AbstractInstrumentScreen instrumentScreen;
    public final Screen lastScreen;

    public final boolean isOverlay;

    public AbstractInstrumentOptionsScreen(Component pTitle, AbstractInstrumentScreen instrumentScreen, Screen lastScreen) {
        super(pTitle);
        this.instrumentScreen = instrumentScreen;
        this.lastScreen = lastScreen;

        this.isOverlay = instrumentScreen != null;
    }
    public AbstractInstrumentOptionsScreen(Component pTitle, AbstractInstrumentScreen instrumentScreen) {
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
    public void render(PoseStack stack, int pMouseX, int pMouseY, float pPartialTick) {
        if (isOverlay) {
            instrumentScreen.render(stack, Integer.MAX_VALUE, Integer.MAX_VALUE, pPartialTick);
            // Push the options screen infront
            stack.translate(0, 0, 1);
        }
        
        
        renderBackground(stack);
        drawCenteredString(stack, font, title, width/2, 15, Color.WHITE.getRGB());
        
        super.render(stack, pMouseX, pMouseY, pPartialTick);
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
        onSave();

        if (lastScreen != null)
            minecraft.setScreen(lastScreen);
        else
            super.onClose();
    }


    protected <T> TooltipSupplier<T> tooltip(final Function<T, Component> text) {
        return (value) -> minecraft.font.split(text.apply(value), 200);
    }


    /* ---------------- Save System --------------- */

    protected final HashMap<String, Runnable> appliedOptions = new HashMap<>();

    /**
     * Queues the given option to later be saved.
     * Most notably, a save occures when the client closes this screen.
     * @param optionKey A unique identifier of this option. If a duplicate entry
     * exists, it will be overwritten.
     * @param saveRunnable The runnable for saving the option
     */
    protected void queueToSave(final String optionKey, final Runnable saveRunnable) {
        if (appliedOptions.containsKey(optionKey))
            appliedOptions.replace(optionKey, saveRunnable);
        else
            appliedOptions.put(optionKey, saveRunnable);
    }

    protected void onSave() {
        for (final Runnable runnable : appliedOptions.values())
            runnable.run();

        ModClientConfigs.CONFIGS.save();
    }
    
}