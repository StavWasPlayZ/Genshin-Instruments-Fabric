package com.cstav.genshinstrument.item;

import net.minecraft.item.Item;

public class InstrumentItem extends Item {

    public InstrumentItem(final Settings settings) {
        super(settings.maxCount(1));
    }
    
}
