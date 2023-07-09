package com.cstav.genshinstrument.mixin.required;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cstav.genshinstrument.mixin.util.IEntityModData;
import com.cstav.genshinstrument.mixin.util.MixinConstants;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

@Mixin(Entity.class)
public abstract class EntityNBTInjector implements IEntityModData {
    
    private CompoundTag persistentData;

    @Override
    public CompoundTag getPersistentData() {
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
