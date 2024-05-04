package net.smileycorp.ldoh.mixin;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.Potion;
import net.smileycorp.ldoh.common.item.LDOHItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isPotionActive(Lnet/minecraft/potion/Potion;)Z"), method = "updateLightmap")
    public boolean updateLightMap$hasPotionEffect(EntityPlayerSP instance, Potion potion) {
        if (potion == MobEffects.NIGHT_VISION && instance.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == LDOHItems.NANO_HELM) return true;
        return instance.isPotionActive(potion);
    }
    
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isPotionActive(Lnet/minecraft/potion/Potion;)Z"), method = "updateFogColor")
    public boolean updateLightMap$hasPotionEffect(EntityLivingBase instance, Potion potion) {
        if (potion == MobEffects.NIGHT_VISION && instance.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == LDOHItems.NANO_HELM) return true;
        return instance.isPotionActive(potion);
    }
    
    @Inject(at = @At(value = "HEAD"), method = "getNightVisionBrightness", cancellable = true)
    public void getNightVisionBrightness(EntityLivingBase entity, float pt, CallbackInfoReturnable<Float> callback) {
        if (entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == LDOHItems.NANO_HELM) callback.setReturnValue(1f);
    }

}
