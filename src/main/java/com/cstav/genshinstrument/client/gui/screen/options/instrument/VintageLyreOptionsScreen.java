package com.cstav.genshinstrument.client.gui.screen.options.instrument;

import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screen.instrument.vintagelyre.VintageLyreScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class VintageLyreOptionsScreen extends GridInstrumentOptionsScreen {
    private final static int SPACE_BEFORE = 20, SPACER_HEIGHT = 13;
    private int heightBefore;

    public VintageLyreOptionsScreen(final VintageLyreScreen screen) {
        super(screen);
    }


    @Override
    protected void initOptionsGrid(GridLayout grid, GridLayout.RowHelper rowHelper) {
        super.initOptionsGrid(grid, rowHelper);

        rowHelper.addChild(SpacerElement.height(SPACER_HEIGHT), 2);
        grid.arrangeElements();
        heightBefore = grid.getHeight();

        final CycleButton<Boolean> normalizeLyre = CycleButton.booleanBuilder(CommonComponents.OPTION_ON, CommonComponents.OPTION_OFF)
                .withInitialValue(ModClientConfigs.NORMALIZE_VINTAGE_LYRE.get())
                .withTooltip((value) -> Tooltip.create(Component.translatable("button.genshinstrument.normalize_vintage_lyre.tooltip")))
                .create(0, 0,
                    getBigButtonWidth(), getButtonHeight(),
                    Component.translatable("button.genshinstrument.normalize_vintage_lyre"), this::onNormalizeLyreChanged
                );

        rowHelper.addChild(normalizeLyre, 2);
    }

    private void onNormalizeLyreChanged(CycleButton<Boolean> button, Boolean value) {
        ModClientConfigs.NORMALIZE_VINTAGE_LYRE.set(value);
    }

    @Override
    public void render(GuiGraphics gui, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(gui, pMouseX, pMouseY, pPartialTick);

        gui.drawCenteredString(font,
            Component.translatable("label.genshinstrument.vintage_lyre_options"),
            width/2, heightBefore + SPACE_BEFORE
            , Color.WHITE.getRGB()
        );
    }
}
