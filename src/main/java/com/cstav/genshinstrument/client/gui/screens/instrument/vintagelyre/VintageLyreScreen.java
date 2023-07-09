package com.cstav.genshinstrument.client.gui.screens.instrument.vintagelyre;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractGridInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.InstrumentThemeLoader;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.note.NoteGrid;
import com.cstav.genshinstrument.sound.ModSounds;
import com.cstav.genshinstrument.sound.NoteSound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

@Environment(EnvType.CLIENT)
//NOTE: There just to make it load on mod setup
@EventBusSubscriber(bus = Bus.MOD, value = Dist.CLIENT)
public class VintageLyreScreen extends AbstractGridInstrumentScreen {
    public static final String INSTRUMENT_ID = "vintage_lyre";
    
    public VintageLyreScreen(InteractionHand hand) {
        super(hand);
    }
    @Override
    public ResourceLocation getInstrumentId() {
        return new ResourceLocation(GInstrumentMod.MODID, INSTRUMENT_ID);
    }


    @Override
    public NoteSound[] getSounds() {
        return ModSounds.VINTAGE_LYRE_NOTE_SOUNDS;
    }
    
    @Override
    public NoteGrid initNoteGrid() {
        return new VintageNoteGrid(
            rows(), columns(), getSounds(), this
        );
    }
    
    
    private static final InstrumentThemeLoader THEME_LOADER = initThemeLoader(GInstrumentMod.MODID, INSTRUMENT_ID);
    @Override
    public InstrumentThemeLoader getThemeLoader() {
        return THEME_LOADER;
    }
}
