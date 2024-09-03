package com.cstav.genshinstrument;

import com.cstav.genshinstrument.block.ModBlockEntities;
import com.cstav.genshinstrument.block.ModBlocks;
import com.cstav.genshinstrument.criteria.ModCriteria;
import com.cstav.genshinstrument.event.ServerEvents;
import com.cstav.genshinstrument.item.GIItems;
import com.cstav.genshinstrument.networking.GIPacketHandler;
import com.cstav.genshinstrument.networking.buttonidentifier.DrumNoteIdentifier;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteButtonIdentifiers;
import com.cstav.genshinstrument.networking.buttonidentifier.NoteGridButtonIdentifier;
import com.cstav.genshinstrument.sound.GISounds;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class of the Genshin Instruments mod
 *
 * @author StavWasPlayZ
 */
public class GInstrumentMod implements ModInitializer {
	public static final String MODID = "genshinstrument";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		GIPacketHandler.registerServerPackets();
		ServerEvents.register();
		NoteButtonIdentifiers.register(
			NoteGridButtonIdentifier.class,
			DrumNoteIdentifier.class
		);

		ModCriteria.register();
		
		GISounds.load();
		

		ModBlocks.load();
		ModBlockEntities.load();

		GICreativeModeTabs.load();
		GIItems.load();
	}
	
}