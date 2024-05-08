package net.smileycorp.ldoh.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.item.LDOHItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityPlayerMP.class)
public abstract class MixinEntityPlayerMP extends EntityPlayer {

    public MixinEntityPlayerMP(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    @Inject(at = @At("HEAD"), method = "isEntityInvulnerable", cancellable = true)
    public void isEntityInvulnerable(DamageSource source, CallbackInfoReturnable<Boolean> callback) {
        if (source == DamageSource.FALL) {
            ItemStack boots = getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (boots.getItem() == LDOHItems.EXO_BOOTS) {
                boots.damageItem(1, this);
                callback.setReturnValue(false);
            }
        }
        if (source == DamageSource.DROWN || source == LDOHTweaks.TOXIC_GAS_DAMAGE) {
            ItemStack HELM = getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (HELM.getItem() == LDOHItems.NANO_HELM) {
                HELM.damageItem(1, this);
                callback.setReturnValue(false);
            }
        }
    }

}
