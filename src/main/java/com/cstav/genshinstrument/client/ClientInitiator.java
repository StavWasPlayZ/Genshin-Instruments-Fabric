package com.cstav.genshinstrument.client;

import java.util.List;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screens.instrument.drum.AratakisGreatAndGloriousDrumScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.floralzither.FloralZitherScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.InstrumentThemeLoader;
import com.cstav.genshinstrument.client.gui.screens.instrument.test.banjo.BanjoInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.vintagelyre.VintageLyreScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.windsonglyre.WindsongLyreScreen;
import com.cstav.genshinstrument.client.keyMaps.InstrumentKeyMappings;
import com.cstav.genshinstrument.event.ClientEvents;
import com.cstav.genshinstrument.event.ResourcesLoadedEvent;
import com.cstav.genshinstrument.item.ItemPoseModifier;
import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.networking.ModPacketHandler;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraftforge.fml.config.ModConfig;

@Environment(EnvType.CLIENT)
public class ClientInitiator implements ClientModInitializer {

	private static final List<Class<?>> LOAD_ME = List.of(
		WindsongLyreScreen.class, VintageLyreScreen.class,
		FloralZitherScreen.class, AratakisGreatAndGloriousDrumScreen.class,

		//TODO remove after tests
		BanjoInstrumentScreen.class
	);

    
	@Override
	public void onInitializeClient() {
		registerClientPackets();
		ForgeConfigRegistry.INSTANCE.register(GInstrumentMod.MODID, ModConfig.Type.CLIENT, ModClientConfigs.CONFIGS);
		
		ClientEvents.register();
		ItemPoseModifier.register();

		
		InstrumentKeyMappings.load();

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


	public static void registerClientPackets() {
        for (final Class<IModPacket> packetClass : ModPacketHandler.S2C_PACKETS) {

            ClientPlayNetworking.registerGlobalReceiver(
                IModPacket.getChannelName(packetClass),
                (client, handler, buf, sender) ->
                    ModPacketHandler.handlePacket(client.player, sender, buf, packetClass, client::execute)
                );

        }
    }
    
}
