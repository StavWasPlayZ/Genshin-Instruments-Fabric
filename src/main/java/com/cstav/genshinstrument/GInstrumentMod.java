package com.cstav.genshinstrument;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraftforge.fml.config.ModConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.event.ClientEvents;
import com.cstav.genshinstrument.item.ModItems;
import com.cstav.genshinstrument.networking.ModPacketHandler;
import com.cstav.genshinstrument.sound.ModSounds;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;

public class GInstrumentMod implements ModInitializer, ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("genshinstrument");
	public static final String MODID = "genshinstrument";

	@Override
	public void onInitialize() {
		ModSounds.regsiter();

		ModCreativeModeTabs.regsiter();
		ModItems.register();
	}

	@Override
	public void onInitializeClient() {
		ModPacketHandler.registerClientPackets();
		ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.CLIENT, ModClientConfigs.CONFIGS);
		
		ClientEvents.register();
	}
	
}