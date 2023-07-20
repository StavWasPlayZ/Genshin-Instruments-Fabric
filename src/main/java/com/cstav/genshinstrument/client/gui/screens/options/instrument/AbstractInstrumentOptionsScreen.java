package com.cstav.genshinstrument.client.gui.screens.options.instrument;

import java.awt.Color;
import java.util.HashMap;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.config.enumType.InstrumentChannelType;
import com.cstav.genshinstrument.client.config.enumType.label.NoteGridLabel;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.label.AbsGridLabels;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.label.INoteLabel;
import com.cstav.genshinstrument.client.gui.screens.options.widget.copied.GridWidget;
import com.cstav.genshinstrument.client.gui.screens.options.widget.copied.GridWidget.RowHelper;
import com.cstav.genshinstrument.client.gui.screens.options.widget.copied.SpacerWidget;
import com.cstav.genshinstrument.sound.NoteSound;
import com.ibm.icu.text.DecimalFormat;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance.TooltipSupplier;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public abstract class AbstractInstrumentOptionsScreen extends Screen {

    private static final String SOUND_CHANNEL_KEY = "button.genshinstrument.audioChannels",
        STOP_MUSIC_KEY = "button.genshinstrument.stop_music_on_play";

    public static final double PITCH_STEP = .05;


    protected final HashMap<String, Runnable> APPLIED_OPTIONS = new HashMap<>();
    
    /**
     * Queues the given option to later be saved,
     * such that when the client closes this screen - the given runnable will run.
     * @param optionKey A unique identifier of this option. If a duplicate entry
     * exists, it will be overwritten.
     * @param saveRunnable The runnable for saving the option
     */
    protected void queueToSave(final String optionKey, final Runnable saveRunnable) {
        if (APPLIED_OPTIONS.containsKey(optionKey))
            APPLIED_OPTIONS.replace(optionKey, saveRunnable);
        else
            APPLIED_OPTIONS.put(optionKey, saveRunnable);
    }

    

    protected int getHorzPadding() {
        return 4;
    }
    protected int getVertPadding() {
        return 2;
    }

    protected int getSmallButtonWidth() {
        return 190;
    }
    protected int getBigButtonWidth() {
        return (getSmallButtonWidth() + getHorzPadding()) * 2;
    }
    protected int getButtonHeight() {
        return 20;
    }

    public abstract INoteLabel[] getLabels();
    /**
     * @return The current note label for this instrument's notes
     */
    public abstract INoteLabel getCurrentLabel();
    

    protected final Screen lastScreen;
    protected final boolean isOverlay;
    private Runnable onCloseRunnable;

    protected final @Nullable INoteLabel[] labels;
    protected final @Nullable INoteLabel currLabel;
    
    protected final @Nullable AbstractInstrumentScreen instrumentScreen;

    public AbstractInstrumentOptionsScreen(@Nullable AbstractInstrumentScreen screen) {
        super(Component.translatable("button.genshinstrument.instrumentOptions"));
        
        this.isOverlay = true;
        this.instrumentScreen = screen;
        lastScreen = null;

        labels = getLabels();
        currLabel = getCurrentLabel();
    }
    public AbstractInstrumentOptionsScreen(final Screen lastScreen) {
        super(Component.translatable("button.genshinstrument.instrumentOptions"));

        this.isOverlay = false;
        
        this.instrumentScreen = null;
        this.lastScreen = lastScreen;

        // Default to NoteGridLabel's values
        labels = NoteGridLabel.values();
        currLabel = ModClientConfigs.GRID_LABEL_TYPE.get();
    }

    public void setOnCloseRunnable(final Runnable onCloseRunnable) {
        this.onCloseRunnable = onCloseRunnable;
    }

    @Override
    protected void init() {

        final GridWidget grid = new GridWidget();
        grid.defaultCellSetting()
            .padding(getHorzPadding(), getVertPadding())
            .alignVertically(.5f)
            .alignHorizontallyCenter();
        final RowHelper rowHelper = grid.createRowHelper(2);


        initOptionsGrid(grid, rowHelper);
        
        grid.pack();
        grid.setY(40);
        grid.setX((width - grid.getWidth()) / 2);
        grid.pack();

        FrameLayout.alignInRectangle(grid, 0, 0, width, height, 0.5f, 0);
        grid.visitWidgets(this::addRenderableWidget);

        grid.arrangeElements();
        grid.setY(40);


        final Button doneBtn = Button.builder(CommonComponents.GUI_DONE, (btn) -> onClose())
            .width(getSmallButtonWidth())
            .pos((width - getSmallButtonWidth())/2, Math.min(grid.getY() + grid.getHeight() + 50, height - getButtonHeight() - 15))
            .build();
        addRenderableWidget(doneBtn);
        
    }


    protected void initAudioSection(final GridLayout grid, final RowHelper rowHelper) {
        final CycleButton<InstrumentChannelType> instrumentChannel = CycleButton.<InstrumentChannelType>builder((soundType) ->
            Component.translatable(SOUND_CHANNEL_KEY +"."+ soundType.toString().toLowerCase())
        )
            .withValues(InstrumentChannelType.values())
            .withInitialValue(ModClientConfigs.CHANNEL_TYPE.get())

            .withTooltip(tooltip((soundType) -> switch (soundType) {
                case MIXED -> translatableArgs(SOUND_CHANNEL_KEY+".mixed.tooltip", NoteSound.STEREO_RANGE);
                case STEREO -> Component.translatable(SOUND_CHANNEL_KEY+".stereo.tooltip");
                default -> Component.empty();
            }))
            .create(0, 0,
                getBigButtonWidth(), 20, Component.translatable(SOUND_CHANNEL_KEY), this::onChannelTypeChanged);
        rowHelper.addChild(instrumentChannel, 2);

        final AbstractSliderButton pitchSlider = new AbstractSliderButton(0, 0, getSmallButtonWidth(), 20,
            CommonComponents.EMPTY,
            Mth.clampedMap(getPitch(), NoteSound.MIN_PITCH, NoteSound.MAX_PITCH, 0, 1)) {

            final DecimalFormat format = new DecimalFormat("0.00");
            {
                updateMessage();
            }


            @Override
            protected void updateMessage() {
                this.setMessage(
                    Component.translatable("button.genshinstrument.pitch").append(
                        ": " + format.format(getPitch())
                            + " ("+AbsGridLabels.getNoteName(getPitch(), AbstractInstrumentScreen.DEFAULT_NOTE_LAYOUT, 0)+")"
                    )
                );
            }
            
            @Override
            protected void applyValue() {
                onPitchChanged(this, divisibleBy5(Mth.clampedLerp(NoteSound.MIN_PITCH, NoteSound.MAX_PITCH, value)));
            }

            private static double divisibleBy5(double number) {
                if (number > (NoteSound.MAX_PITCH - .001))
                    return NoteSound.MAX_PITCH;

                final double factor = Math.round(1 / PITCH_STEP * 100) / 100;
                return (int)(number * factor) / factor;
            }
        };
        rowHelper.addChild(pitchSlider);

        final CycleButton<Boolean> stopMusic = CycleButton.booleanBuilder(CommonComponents.OPTION_ON, CommonComponents.OPTION_OFF)
            .withInitialValue(ModClientConfigs.STOP_MUSIC_ON_PLAY.get())
            .withTooltip(tooltip((val) -> Component.translatable(STOP_MUSIC_KEY+".tooltip", NoteSound.STOP_SOUND_DISTANCE)))
            .create(0, 0,
                getSmallButtonWidth(), getButtonHeight(),
                Component.translatable(STOP_MUSIC_KEY), this::onMusicStopChanged
            );
        rowHelper.addChild(stopMusic);
    }

    protected void initVisualsSection(final GridLayout grid, final RowHelper rowHelper) {

        final CycleButton<Boolean> emitRing = CycleButton.booleanBuilder(CommonComponents.OPTION_ON, CommonComponents.OPTION_OFF)
            .withInitialValue(ModClientConfigs.EMIT_RING_ANIMATION.get())
            .create(0, 0,
                getSmallButtonWidth(), getButtonHeight(),
                Component.translatable("button.genshinstrument.emit_ring"), this::onEmitRingChanged
            );
        rowHelper.addChild(emitRing);

        final CycleButton<Boolean> sharedInstrument = CycleButton.booleanBuilder(CommonComponents.OPTION_ON, CommonComponents.OPTION_OFF)
            .withInitialValue(ModClientConfigs.SHARED_INSTRUMENT.get())
            .withTooltip(tooltip((val) -> Component.translatable("button.genshinstrument.shared_instrument.tooltip")))
            .create(0, 0,
                getSmallButtonWidth(), getButtonHeight(),
                Component.translatable("button.genshinstrument.shared_instrument"), this::onSharedInstrumentChanged
            );
        rowHelper.addChild(sharedInstrument);

        final CycleButton<Boolean> accurateAccidentals = CycleButton.booleanBuilder(CommonComponents.OPTION_ON, CommonComponents.OPTION_OFF)
            .withInitialValue(ModClientConfigs.ACCURATE_ACCIDENTALS.get())
            .withTooltip((value) -> Tooltip.create(Component.translatable("button.genshinstrument.accurate_accidentals.tooltip")))
            .create(0, 0,
                getSmallButtonWidth(), getButtonHeight(),
                Component.translatable("button.genshinstrument.accurate_accidentals"), this::onAccurateAccidentalsChanged
            );
        rowHelper.addChild(accurateAccidentals);


        if (labels != null) {
            final CycleButton<INoteLabel> labelType = CycleButton.<INoteLabel>builder((label) -> Component.translatable(label.getKey()))
                .withValues(labels)
                .withInitialValue(currLabel)
                .create(0, 0,
                    getBigButtonWidth(), getButtonHeight(),
                    Component.translatable("button.genshinstrument.label"), this::onLabelChanged
                );
            rowHelper.addChild(labelType, 2);
        }
    }


    /**
     * Fills the settings grid with all the necessary widgets, buttons and such
     * @param grid The settings grid to add the widgets to
     * @param rowHelper A row helper for the specified {@code grid}
     */
    protected void initOptionsGrid(final GridLayout grid, final RowHelper rowHelper) {
        initAudioSection(grid, rowHelper);

        rowHelper.addChild(SpacerElement.height(15), 2);
        
        initVisualsSection(grid, rowHelper);
    }

    private <T> TooltipSupplier<T> tooltip(final Function<T, Component> text) {
        return (value) -> minecraft.font.split(text.apply(value), 200);
    }

    private float getPitch() {
        return (!isOverlay)
            ? ModClientConfigs.PITCH.get().floatValue()
            : instrumentScreen.getPitch();
    }


    // Change handlers
    protected void onLabelChanged(final CycleButton<INoteLabel> button, final INoteLabel label) {
        if (isOverlay)
            instrumentScreen.notesIterable().forEach((note) -> note.setLabelSupplier(label.getLabelSupplier()));

        queueToSave("note_label", () -> saveLabel(label));
    }
    protected void saveLabel(final INoteLabel newLabel) {
        if (newLabel instanceof NoteGridLabel)
            ModClientConfigs.GRID_LABEL_TYPE.set((NoteGridLabel)newLabel);
    }

    protected void onPitchChanged(final AbstractSliderButton slider, final double pitch) {
        if (isOverlay) {
            instrumentScreen.setPitch((float)pitch);
            instrumentScreen.notesIterable().forEach(NoteButton::updateNoteLabel);

            queueToSave("pitch", () -> savePitch(pitch));
        } else {
            ModClientConfigs.PITCH.set(pitch);
        }
    }
    protected void savePitch(final double newPitch) {
        ModClientConfigs.PITCH.set(newPitch);
    }

    // These values derive from the config directly, so just update them on-spot
    protected void onChannelTypeChanged(CycleButton<InstrumentChannelType> button, InstrumentChannelType type) {
        ModClientConfigs.CHANNEL_TYPE.set(type);
    }
    protected void onMusicStopChanged(final CycleButton<Boolean> button, final boolean value) {
        ModClientConfigs.STOP_MUSIC_ON_PLAY.set(value);
    }
    protected void onEmitRingChanged(final CycleButton<Boolean> button, final boolean value) {
        ModClientConfigs.EMIT_RING_ANIMATION.set(value);
    }
    protected void onSharedInstrumentChanged(final CycleButton<Boolean> button, final boolean value) {
        ModClientConfigs.SHARED_INSTRUMENT.set(value);
    }
    protected void onAccurateAccidentalsChanged(final CycleButton<Boolean> button, final boolean value) {
        ModClientConfigs.ACCURATE_ACCIDENTALS.set(value);

        if (isOverlay)
            instrumentScreen.notesIterable().forEach(NoteButton::updateNoteLabel);
    }



    @Override
    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(poseStack);
        
        drawCenteredString(poseStack, font, title, width/2, 20, Color.WHITE.getRGB());
        
        super.render(poseStack, pMouseX, pMouseY, pPartialTick);
    }


    @Override
    public void onClose() {
        if (!isOverlay) {
            if (lastScreen != null)
                Minecraft.getInstance().setScreen(lastScreen);
            else
                super.onClose();
        }
        
        onSave();

        if (onCloseRunnable != null)
            onCloseRunnable.run();
    }
    protected void onSave() {
        for (final Runnable runnable : APPLIED_OPTIONS.values())
            runnable.run();

        ModClientConfigs.CONFIGS.save();
    }


    /**
     * Tooltip is being annoying and not rpelacing my args.
     * So, fine, I'll do it myself.
     * @param key The translation key
     * @param arg The thing to replace with %s
     * @return What should've been return by {@link Component#translatable(String, Object...)}
     */
    private static MutableComponent translatableArgs(final String key, final Object arg) {
        return Component.literal(
            Component.translatable(key).getString().replace("%s", arg.toString())
        );
    }
}