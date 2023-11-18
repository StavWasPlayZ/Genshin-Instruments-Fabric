package com.cstav.genshinstrument.client;

import java.util.List;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screen.instrument.drum.AratakisGreatAndGloriousDrumScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.floralzither.FloralZitherScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.InstrumentThemeLoader;
import com.cstav.genshinstrument.client.gui.screen.instrument.vintagelyre.VintageLyreScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.windsonglyre.WindsongLyreScreen;
import com.cstav.genshinstrument.client.keyMaps.InstrumentKeyMappings;
import com.cstav.genshinstrument.event.ClientEvents;
import com.cstav.genshinstrument.event.ResourcesLoadedEvent;
import com.cstav.genshinstrument.item.ItemPoseModifier;
import com.cstav.genshinstrument.networking.IModPacket;
import com.cstav.genshinstrument.networking.ModPacketHandler;
import com.cstav.genshinstrument.util.CommonUtil;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

@Environment(EnvType.CLIENT)
public class ClientInitiator implements ClientModInitializer {

	private static final List<Class<?>> LOAD_ME = List.of(
		WindsongLyreScreen.class, VintageLyreScreen.class,
		FloralZitherScreen.class, AratakisGreatAndGloriousDrumScreen.class
	);

    
	@Override
	public void onInitializeClient() {
		registerClientPackets();
		ModLoadingContext.registerConfig(GInstrumentMod.MODID, ModConfig.Type.CLIENT, ModClientConfigs.CONFIGS);
		
		ClientEvents.register();
		ItemPoseModifier.register();

		
		InstrumentKeyMappings.load();

		CommonUtil.loadClasses(LOAD_ME);

		ResourcesLoadedEvent.EVENT.register(InstrumentThemeLoader::onResourcesReload);
	}
    
	private static void registerClientPackets() {
        for (final Class<IModPacket> packetClass : ModPacketHandler.S2C_PACKETS) {

            ClientPlayNetworking.registerGlobalReceiver(
                IModPacket.getChannelName(packetClass),
                (client, handler, buf, sender) ->
                    ModPacketHandler.handlePacket(client.player, sender, buf, packetClass, client::execute)
			);

        }
    }
}
