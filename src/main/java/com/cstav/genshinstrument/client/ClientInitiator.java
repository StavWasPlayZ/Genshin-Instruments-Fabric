package com.cstav.genshinstrument.client;

import java.util.List;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screens.instrument.drum.AratakisGreatAndGloriousDrumScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.floralzither.FloralZitherScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.InstrumentThemeLoader;
import com.cstav.genshinstrument.client.gui.screens.instrument.vintagelyre.VintageLyreScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.windsonglyre.WindsongLyreScreen;
import com.cstav.genshinstrument.event.ClientEvents;
import com.cstav.genshinstrument.event.ResourcesLoadedEvent;
import com.cstav.genshinstrument.networking.ModPacketHandler;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraftforge.fml.config.ModConfig;

public class ClientInitiator implements ClientModInitializer {

	private static final List<Class<?>> LOAD_ME = List.of(
		AratakisGreatAndGloriousDrumScreen.class, FloralZitherScreen.class, VintageLyreScreen.class,
		WindsongLyreScreen.class
	);

    
	@Override
	public void onInitializeClient() {
		ModPacketHandler.registerClientPackets();
		ForgeConfigRegistry.INSTANCE.register(GInstrumentMod.MODID, ModConfig.Type.CLIENT, ModClientConfigs.CONFIGS);
		
		ClientEvents.register();


		// Load necessary classes, as listed above
		for (final Class<?> loadMe : LOAD_ME) {
			
			try {
				Class.forName(loadMe.getName());
			} catch (ClassNotFoundException e) {
				GInstrumentMod.LOGGER.error("Failed to load class "+ loadMe.getSimpleName() +": class not found", e);
			}

		}
		
		ResourcesLoadedEvent.EVENT.register(InstrumentThemeLoader::onResourcesReload);
	}
    
}
