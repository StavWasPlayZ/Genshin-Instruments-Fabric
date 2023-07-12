package com.cstav.genshinstrument.client.gui.screens.instrument.partial;

import java.awt.Color;
import java.util.Map;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.ClientUtil;
import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteGrid;
import com.cstav.genshinstrument.client.gui.screens.options.instrument.AbstractInstrumentOptionsScreen;
import com.cstav.genshinstrument.client.gui.screens.options.instrument.GridInstrumentOptionsScreen;
import com.cstav.genshinstrument.client.keyMaps.KeyMappings;
import com.mojang.blaze3d.platform.InputConstants.Key;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

@Environment(EnvType.CLIENT)
public abstract class AbstractGridInstrumentScreen extends AbstractInstrumentScreen {
    public static final int DEF_ROWS = 7, DEF_COLUMNS = 3,
        CLEF_WIDTH = 26, CLEF_HEIGHT = 52;

    protected AbstractWidget grid;


    public AbstractGridInstrumentScreen(InteractionHand hand) {
        super(hand);
    }

    public int columns() {
        return DEF_COLUMNS;
    }
    public int rows() {
        return DEF_ROWS;
    }

    
    // Abstract implementations
    /**
     * Initializes a new Note Grid to be paired with this instrument
     * @return The new Note Grid
     */
    public NoteGrid initNoteGrid() {
        return new NoteGrid(
            rows(), columns(), getSounds(), this
        );
    }

    public final NoteGrid noteGrid = initNoteGrid();
    
    private final Map<Key, NoteButton> noteMap = noteGrid.genKeyboardMap(KeyMappings.GRID_INSTRUMENT_MAPPINGS);
    @Override
    public Map<Key, NoteButton> noteMap() {
        return noteMap;
    }

    @Override
    protected AbstractInstrumentOptionsScreen initInstrumentOptionsScreen() {
        return new GridInstrumentOptionsScreen(this);
    }

    @Override
    public ResourceLocation getNotesLocation() {
        return new ResourceLocation(GInstrumentMod.MODID, getGlobalRootPath() + "grid_notes.png");
    }
    

    @Override
    protected void init() {
        grid = noteGrid.initNoteGridWidget(.9f, width, height);
        addRenderableWidget(grid);
        
        initOptionsButton(grid.y - 15);
        super.init();
    }


    @Override
    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        if (ModClientConfigs.RENDER_BACKGROUND.get()) {
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();

            ClientUtil.setShaderColor(Color.WHITE, .6f);
            renderInstrumentBackground(poseStack);
            ClientUtil.resetShaderColor();
        }
            
        super.render(poseStack, pMouseX, pMouseY, pPartialTick);
    }


    /**
     * Renders the background of this grid instrument.
     * This render method will only work for a 3-column instrument. Overwrite it
     * to customize the background.
     */
    protected void renderInstrumentBackground(final PoseStack poseStack) {
        if (columns() != 3)
            return;

        final int clefX = grid.x - NoteButton.getSize() + 8;

        for (int i = 0; i < columns(); i++) {
            renderClef(poseStack, i, clefX);
            renderStaff(poseStack, i);
        }
    }

    protected void renderClef(final PoseStack poseStack, final int index, final int x) {
        ClientUtil.displaySprite(getResourceFromGlob("background/clefs.png"));

        GuiComponent.blit(poseStack,
            x, grid.y + (NoteButton.getSize() + 16) * index,
            index * CLEF_WIDTH, 0,
            CLEF_WIDTH, CLEF_HEIGHT,
            CLEF_WIDTH*3, CLEF_HEIGHT
        );
    }

    protected void renderStaff(final PoseStack poseStack, final int index) {
        ClientUtil.displaySprite(getResourceFromGlob("background/staff.png"));

        GuiComponent.blit(poseStack,
            grid.x + 2, grid.y + 8 + ((NoteButton.getSize() + NoteGrid.PADDING_VERT + 6) * index),
            0, 0,
            grid.getWidth() - 5, NoteButton.getSize(),
            grid.getWidth() - 5, NoteButton.getSize()
        );
    }
    
}
