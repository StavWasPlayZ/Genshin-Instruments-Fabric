package com.cstav.genshinstrument;

import com.cstav.genshinstrument.event.ModifyItemRendersModelEvent;
import com.cstav.genshinstrument.event.ModifyItemRendersModelEvent.ModifyItemRendersModelEventArgs;
import com.cstav.genshinstrument.event.RegisterAdditionalModelsEvent;
import com.cstav.genshinstrument.event.RegisterAdditionalModelsEvent.RegisterAdditionalModelsEventArgs;
import com.cstav.genshinstrument.item.GIItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

@Environment(EnvType.CLIENT)
public class ModModels {

    private static final ModelResourceLocation
        NIGHTWIND_HORN_ITEM = mrl("nightwind_horn_item")
    ;

    public static void register() {
        ModifyItemRendersModelEvent.EVENT.register(ModModels::onItemRenders);
        RegisterAdditionalModelsEvent.EVENT.register(ModModels::onRegisterModels);
    }

    private static void onItemRenders(final ModifyItemRendersModelEventArgs args) {
        if (args.itemStack.is(GIItems.NIGHTWIND_HORN)) {

            if (
                args.context == ItemDisplayContext.GUI
                || args.context == ItemDisplayContext.FIXED
                || args.context == ItemDisplayContext.GROUND
            ) {
                args.setModel(args.itemModelShaper.getModelManager().getModel(NIGHTWIND_HORN_ITEM));
            }

        }
    }

    private static void onRegisterModels(final RegisterAdditionalModelsEventArgs args) {
        args.addModel(NIGHTWIND_HORN_ITEM);
    }


    private static ModelResourceLocation mrl(String path) {
        return new ModelResourceLocation(GInstrumentMod.loc(path), "inventory");
    }

}
