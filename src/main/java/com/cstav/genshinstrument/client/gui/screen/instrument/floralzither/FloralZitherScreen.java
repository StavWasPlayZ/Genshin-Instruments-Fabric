package com.cstav.genshinstrument.client.gui.screen.instrument.floralzither;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.InstrumentThemeLoader;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.notegrid.AbstractGridInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screen.options.instrument.FloralZitherOptionsScreen;
import com.cstav.genshinstrument.client.gui.screen.options.instrument.partial.BaseInstrumentOptionsScreen;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

@Environment(EnvType.CLIENT)
public class FloralZitherScreen extends AbstractGridInstrumentScreen {
    public static final ResourceLocation INSTRUMENT_ID = new ResourceLocation(GInstrumentMod.MODID, "floral_zither");
    
    public FloralZitherScreen(InteractionHand hand) {
        super(hand);
    }
    @Override
    public ResourceLocation getInstrumentId() {
        return INSTRUMENT_ID;
    }

    
    @Override
    public NoteSound[] getInitSounds() {
        return ((FloralZitherOptionsScreen)optionsScreen).getPreferredSoundType().getSoundArr().get();
    }

    @Override
    protected BaseInstrumentOptionsScreen initInstrumentOptionsScreen() {
        return new FloralZitherOptionsScreen(this);
    }

    
    private static final InstrumentThemeLoader THEME_LOADER = new InstrumentThemeLoader(INSTRUMENT_ID);
    @Override
    public InstrumentThemeLoader getThemeLoader() {
        return THEME_LOADER;
    }
    
}
