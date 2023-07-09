package com.cstav.genshinstrument.client.gui.screens.instrument.windsonglyre;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractGridInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.InstrumentThemeLoader;
import com.cstav.genshinstrument.sound.ModSounds;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

@Environment(EnvType.CLIENT)
public class WindsongLyreScreen extends AbstractGridInstrumentScreen {
    public static final String INSTRUMENT_ID = "windsong_lyre";

    public WindsongLyreScreen(InteractionHand hand) {
        super(hand);
    }
    @Override
    public ResourceLocation getInstrumentId() {
        return new ResourceLocation(GInstrumentMod.MODID, INSTRUMENT_ID);
    }
    

    @Override
    public NoteSound[] getSounds() {
        return ModSounds.WINDSONG_LYRE_NOTE_SOUNDS;
    }


    private static final InstrumentThemeLoader THEME_LOADER = initThemeLoader(GInstrumentMod.MODID, INSTRUMENT_ID);
    @Override
    public InstrumentThemeLoader getThemeLoader() {
        return THEME_LOADER;
    }
    
}
