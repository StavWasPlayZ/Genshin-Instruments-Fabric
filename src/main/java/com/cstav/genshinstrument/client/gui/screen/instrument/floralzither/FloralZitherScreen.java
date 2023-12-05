package com.cstav.genshinstrument.client.gui.screen.instrument.floralzither;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.InstrumentThemeLoader;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.notegrid.GridInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screen.options.instrument.FloralZitherOptionsScreen;
import com.cstav.genshinstrument.client.gui.screen.options.instrument.partial.InstrumentOptionsScreen;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class FloralZitherScreen extends GridInstrumentScreen {
    public static final ResourceLocation INSTRUMENT_ID = new ResourceLocation(GInstrumentMod.MODID, "floral_zither");

    @Override
    public ResourceLocation getInstrumentId() {
        return INSTRUMENT_ID;
    }

    
    @Override
    public NoteSound[] getInitSounds() {
        return ((FloralZitherOptionsScreen)optionsScreen).getPreferredSoundType().getSoundArr().get();
    }

    @Override
    protected InstrumentOptionsScreen initInstrumentOptionsScreen() {
        return new FloralZitherOptionsScreen(this);
    }

    
    private static final InstrumentThemeLoader THEME_LOADER = new InstrumentThemeLoader(INSTRUMENT_ID);
    @Override
    public InstrumentThemeLoader getThemeLoader() {
        return THEME_LOADER;
    }
    
}
