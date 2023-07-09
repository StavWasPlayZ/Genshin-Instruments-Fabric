package com.cstav.genshinstrument.client.gui.screens.instrument.floralzither;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractGridInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.InstrumentThemeLoader;
import com.cstav.genshinstrument.client.gui.screens.options.instrument.AbstractInstrumentOptionsScreen;
import com.cstav.genshinstrument.client.gui.screens.options.instrument.FloralZitherOptionsScreen;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

@Environment(EnvType.CLIENT)
public class FloralZitherScreen extends AbstractGridInstrumentScreen {
    public static final String INSTRUMENT_ID = "floral_zither";
    
    public FloralZitherScreen(InteractionHand hand) {
        super(hand);
    }
    @Override
    public ResourceLocation getInstrumentId() {
        return new ResourceLocation(GInstrumentMod.MODID, INSTRUMENT_ID);
    }

    
    @Override
    public NoteSound[] getSounds() {
        return ((FloralZitherOptionsScreen)optionsScreen).getPerferredSoundType().soundArr().get();
    }

    @Override
    protected AbstractInstrumentOptionsScreen initInstrumentOptionsScreen() {
        return new FloralZitherOptionsScreen(this);
    }

    
    private static final InstrumentThemeLoader THEME_LOADER = initThemeLoader(GInstrumentMod.MODID, INSTRUMENT_ID);
    @Override
    public InstrumentThemeLoader getThemeLoader() {
        return THEME_LOADER;
    }
    
}