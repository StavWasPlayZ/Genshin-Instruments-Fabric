package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.event.impl.EventArgs;
import com.cstav.genshinstrument.event.impl.ModEvent;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ResourcesLoadedEvent extends ModEvent<EventArgs.Empty> {

    Event<ResourcesLoadedEvent> EVENT = EventFactory.createArrayBacked(ResourcesLoadedEvent.class,
        (listeners) -> args -> ModEvent.handleEvent(listeners, args)
    );
    
}
