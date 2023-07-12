package com.cstav.genshinstrument.client.gui.screens.instrument.vintagelyre;

import com.cstav.genshinstrument.client.ClientUtil;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractGridInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteGridButton;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.label.NoteLabelSupplier;
import com.cstav.genshinstrument.sound.NoteSound;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class VintageNoteButton extends NoteGridButton {
    private static final float TEXTURE_MULTIPLIER = 1/1.3f;

    private final ResourceLocation thingyLocation = getResourceFromRoot("thing.png");

    public VintageNoteButton(int row, int column,
            NoteSound sound, NoteLabelSupplier labelSupplier, AbstractGridInstrumentScreen instrumentScreen) {
        super(row, column, sound, labelSupplier, instrumentScreen);
    }

    
    private boolean shouldRenderThingy() {
        return (row == 6) || (row == 2) ||
            ((row == 1) && (column == 0)) || ((row == 5) && (column == 0));
    }

    @Override
    public void renderButton(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderButton(poseStack, pMouseX, pMouseY, pPartialTick);

        if (!shouldRenderThingy())
            return;

        final int textureWidth = (int)(width * TEXTURE_MULTIPLIER),
            textureHeight = (int)(height * TEXTURE_MULTIPLIER);


        ClientUtil.displaySprite(thingyLocation);

        GuiComponent.blit(poseStack,
            x - 1, y - 5,
            isPlaying() ? textureWidth/2 : 0, 0,
            textureWidth/2,  textureHeight,
            textureWidth, textureHeight
        );
    }
    
}
