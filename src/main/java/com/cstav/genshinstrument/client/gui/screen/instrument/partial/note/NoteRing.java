package com.cstav.genshinstrument.client.gui.screen.instrument.partial.note;

import java.awt.Point;

import com.cstav.genshinstrument.client.gui.screen.instrument.partial.InstrumentScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.InstrumentThemeLoader;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.animation.RingAnimationController;
import com.cstav.genshinstrument.client.util.ClientUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;

@Environment(EnvType.CLIENT)
public class NoteRing {
    public static final String RING_GLOB_FILENAME = "note/ring.png";

    protected final RingAnimationController ringAnimation;

    public final NoteButton noteButton;
    public int size;
    public float alpha;
    protected boolean isForeign;

    public NoteRing(final NoteButton noteButton, final boolean isForeign) {
        this.noteButton = noteButton;
        ringAnimation = new RingAnimationController(.3, 40, this);
    }
    public void playAnim() {
        if (isForeign)
            ringAnimation.play(-.4f);
        else
            ringAnimation.play();
    }
    
    
    public void render(final GuiGraphics gui, final InstrumentThemeLoader themeLoader) {
        if (!ringAnimation.isPlaying())
            return;

        ringAnimation.update();

        final Point ringCenter = ClientUtil.getInitCenter(
            noteButton.getInitX(),
            noteButton.getInitY(),
            noteButton.instrumentScreen.getNoteSize(),
            size
        );


        ClientUtil.setShaderColor(themeLoader.noteRing(noteButton), alpha);

        gui.blit(InstrumentScreen.getInternalResourceFromGlob(RING_GLOB_FILENAME),
            ringCenter.x, ringCenter.y,
            0, 0,
            size, size,
            size, size
        );

        ClientUtil.resetShaderColor();
    }

    public boolean isPlaying() {
        return ringAnimation.isPlaying();
    }

}
