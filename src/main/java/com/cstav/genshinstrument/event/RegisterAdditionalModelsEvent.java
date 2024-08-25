package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.event.RegisterAdditionalModelsEvent.RegisterAdditionalModelsEventArgs;
import com.cstav.genshinstrument.event.impl.EventArgs;
import com.cstav.genshinstrument.event.impl.ModEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.model.ModelResourceLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public interface RegisterAdditionalModelsEvent extends ModEvent<RegisterAdditionalModelsEventArgs> {

    Event<RegisterAdditionalModelsEvent> EVENT = EventFactory.createArrayBacked(RegisterAdditionalModelsEvent.class,
        (listeners) -> args -> ModEvent.handleEvent(listeners, args)
    );

    public static class RegisterAdditionalModelsEventArgs extends EventArgs {
        protected final Set<ModelResourceLocation> models = new HashSet<>();

        public void addModel(final ModelResourceLocation location) {
            models.add(location);
        }
        public Set<ModelResourceLocation> getModels() {
            return Collections.unmodifiableSet(models);
        }
    }

}
