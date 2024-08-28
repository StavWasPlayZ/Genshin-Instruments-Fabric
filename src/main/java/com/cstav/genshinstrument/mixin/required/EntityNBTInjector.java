package com.cstav.genshinstrument.mixin.required;

import com.cstav.genshinstrument.mixin.util.IEntityModData;
import com.cstav.genshinstrument.mixin.util.MixinConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityNBTInjector implements IEntityModData {
    
    @Unique
    private CompoundTag persistentData;

    @Unique
    @Override
    public CompoundTag genshinstrument$getPersistentData() {
        if (persistentData == null)
            persistentData = new CompoundTag();
        
        return persistentData;
    }


    @Inject(at = @At("HEAD"), method = "load")
    private void loadData(final CompoundTag compound, final CallbackInfo info) {
        if (compound.contains(MixinConstants.ROOT_CAP, CompoundTag.TAG_COMPOUND))
            persistentData = compound.getCompound(MixinConstants.ROOT_CAP);
    }

    @Inject(at = @At("HEAD"), method = "saveWithoutId")
    private void writeData(final CompoundTag compound, final CallbackInfoReturnable<CompoundTag> info) {
        if (persistentData != null)
            compound.put(MixinConstants.ROOT_CAP, persistentData);
    }

}
