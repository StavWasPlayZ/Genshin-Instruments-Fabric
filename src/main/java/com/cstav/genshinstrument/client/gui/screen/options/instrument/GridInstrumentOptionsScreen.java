package com.cstav.genshinstrument.client.gui.screen.options.instrument;

import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.config.enumType.label.NoteGridLabel;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.label.INoteLabel;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.notegrid.AbstractGridInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screen.options.instrument.partial.BaseInstrumentOptionsScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class GridInstrumentOptionsScreen extends BaseInstrumentOptionsScreen {

    public GridInstrumentOptionsScreen(final AbstractGridInstrumentScreen screen) {
        super(screen);
    }
    public GridInstrumentOptionsScreen(final Screen lastScreen) {
        super(lastScreen);
    }


    @Override
    public NoteGridLabel[] getLabels() {
        return NoteGridLabel.availableVals();
    }
    @Override
    public NoteGridLabel getCurrentLabel() {
        return ModClientConfigs.GRID_LABEL_TYPE.get();
    }

    @Override
    protected void saveLabel(final INoteLabel newLabel) {
        if (newLabel instanceof NoteGridLabel)
            ModClientConfigs.GRID_LABEL_TYPE.set((NoteGridLabel)newLabel);
    }


    @Override
    public boolean isPitchSliderEnabled() {
        return (instrumentScreen == null) ||
            !((AbstractGridInstrumentScreen)instrumentScreen).isSSTI();
    }
    
    
    @Override
    protected void initVisualsSection(GridLayout grid, RowHelper rowHelper) {
        final CycleButton<Boolean> renderBackground = CycleButton.booleanBuilder(CommonComponents.OPTION_ON, CommonComponents.OPTION_OFF)
            .withInitialValue(ModClientConfigs.RENDER_BACKGROUND.get())
            .create(0, 0,
                getSmallButtonWidth(), getButtonHeight(),
                Component.translatable("button.genshinstrument.render_background"), this::onRenderBackgroundChanged
            );
        rowHelper.addChild(renderBackground);

        super.initVisualsSection(grid, rowHelper);
    }

    protected void onRenderBackgroundChanged(final CycleButton<Boolean> button, final boolean value) {
        ModClientConfigs.RENDER_BACKGROUND.set(value);
    }
    
}