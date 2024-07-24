package com.cstav.genshinstrument;

import com.cstav.genshinstrument.event.ModifyItemRendersModelEvent;
import com.cstav.genshinstrument.event.ModifyItemRendersModelEvent.ModifyItemRendersModelEventArgs;
import com.cstav.genshinstrument.item.GIItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

@Environment(EnvType.CLIENT)
public class ModModels {

    private static final ModelResourceLocation TRIDENT_MODEL = ModelResourceLocation.vanilla("trident", "inventory");

    public static void register() {
        ModifyItemRendersModelEvent.EVENT.register(ModModels::onItemRenders);
    }

    private static void onItemRenders(final ModifyItemRendersModelEventArgs args) {
        if (args.itemStack.is(GIItems.NIGHTWIND_HORN)) {

            if (
                args.context == ItemDisplayContext.GUI
                || args.context == ItemDisplayContext.FIXED
                || args.context == ItemDisplayContext.GROUND
            ) {
                //TODO replace w/ actual model
                args.setModel(args.itemModelShaper.getModelManager().getModel(TRIDENT_MODEL));
            }

        }
    }

}
