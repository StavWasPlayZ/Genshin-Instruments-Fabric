package com.cstav.genshinstrument.client;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.ModModels;
import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screen.instrument.InstrumentScreenRegistry;
import com.cstav.genshinstrument.client.gui.screen.instrument.djemdjemdrum.DjemDjemDrumScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.floralzither.FloralZitherScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.gloriousdrum.AratakisGreatAndGloriousDrumScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.nightwind_horn.NightwindHornScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.InstrumentScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.ukulele.UkuleleScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.vintagelyre.VintageLyreScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.windsonglyre.WindsongLyreScreen;
import com.cstav.genshinstrument.client.keyMaps.InstrumentKeyMappings;
import com.cstav.genshinstrument.event.ClientEvents;
import com.cstav.genshinstrument.item.ModItemPredicates;
import com.cstav.genshinstrument.networking.GIPacketHandler;
import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Map;
import java.util.function.Supplier;

public class ClientInitiator implements ClientModInitializer {

	private static final Map<ResourceLocation, Supplier<? extends InstrumentScreen>> INSTRUMENTS = Map.of(
		WindsongLyreScreen.INSTRUMENT_ID, WindsongLyreScreen::new,
		VintageLyreScreen.INSTRUMENT_ID, VintageLyreScreen::new,
		FloralZitherScreen.INSTRUMENT_ID, FloralZitherScreen::new,
		AratakisGreatAndGloriousDrumScreen.INSTRUMENT_ID, AratakisGreatAndGloriousDrumScreen::new,
		NightwindHornScreen.INSTRUMENT_ID, NightwindHornScreen::new,

		UkuleleScreen.INSTRUMENT_ID, UkuleleScreen::new,
		DjemDjemDrumScreen.INSTRUMENT_ID, DjemDjemDrumScreen::new
	);

    
	@Override
	public void onInitializeClient() {
		GIPacketHandler.registerClientPackets();
		ForgeConfigRegistry.INSTANCE.register(GInstrumentMod.MODID, ModConfig.Type.CLIENT, ModClientConfigs.CONFIGS);
		
		ClientEvents.register();

		ModItemPredicates.register();
		ModModels.register();

		InstrumentKeyMappings.load();

		InstrumentScreenRegistry.register(INSTRUMENTS);
	}
    
}
