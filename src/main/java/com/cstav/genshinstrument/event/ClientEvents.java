package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.ByPlayer.ByPlayerArgs;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent.InstrumentPlayedEventArgs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class ClientEvents {
    
    public static void register() {
        InstrumentPlayedEvent.EVENT.register(ClientEvents::onInstrumentPlayed);
    }


    public static void onInstrumentPlayed(final InstrumentPlayedEventArgs args) {
        GInstrumentMod.LOGGER.info("Recieved play event from " + (args.isClientSide ? "client" : "server"));
        if (args instanceof ByPlayerArgs)
            GInstrumentMod.LOGGER.info("This event was fired by a player");
    }

}
