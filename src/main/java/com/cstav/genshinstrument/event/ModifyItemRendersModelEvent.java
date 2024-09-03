package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.event.ModifyItemRendersModelEvent.ModifyItemRendersModelEventArgs;
import com.cstav.genshinstrument.event.impl.Cancelable;
import com.cstav.genshinstrument.event.impl.EventArgs;
import com.cstav.genshinstrument.event.impl.ModEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public interface ModifyItemRendersModelEvent extends ModEvent<ModifyItemRendersModelEventArgs> {

    @Cancelable
    public static class ModifyItemRendersModelEventArgs extends EventArgs {
        private BakedModel model;
        public final ItemStack itemStack;
        public final ItemDisplayContext context;
        public final ItemModelShaper itemModelShaper;

        public BakedModel getModel() {
            return model;
        }

        /**
         * Replaces the model with the given model
         * and cancels this event (and any future replacements).
         * @param model The model to replace
         */
        public void setModel(BakedModel model) {
            this.model = model;
            cancel();
        }

        public ModifyItemRendersModelEventArgs(BakedModel model,
                                               ItemStack stack,
                                               ItemDisplayContext context,
                                               ItemModelShaper itemModelShaper) {
            this.model = model;
            this.itemStack = stack;
            this.context = context;
            this.itemModelShaper = itemModelShaper;
        }
    }

    Event<ModifyItemRendersModelEvent> EVENT = EventFactory.createArrayBacked(ModifyItemRendersModelEvent.class,
        (listeners) -> args -> ModEvent.handleEvent(listeners, args)
    );

}