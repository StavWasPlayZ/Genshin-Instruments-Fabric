package com.cstav.genshinstrument.networking.packet.instrument.util;

import com.cstav.genshinstrument.client.gui.screen.instrument.InstrumentScreenRegistry;
import net.minecraft.resources.ResourceLocation;

public class ClientDistExec {

    public static void setScreenByInstrumentId(final ResourceLocation instrumentType) {
        InstrumentScreenRegistry.setScreenByID(instrumentType);
    }

}
