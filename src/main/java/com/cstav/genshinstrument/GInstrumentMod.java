package com.cstav.genshinstrument;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cstav.genshinstrument.item.ModItems;
import com.cstav.genshinstrument.networking.ModPacketHandler;
import com.cstav.genshinstrument.sound.ModSounds;

public class GInstrumentMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("genshinstrument");
	public static final String MODID = "genshinstrument";

	@Override
	public void onInitialize() {
		ModSounds.regsiter();
		ModPacketHandler.register();

		ModCreativeModeTabs.regsiter();
		ModItems.register();
	}
}