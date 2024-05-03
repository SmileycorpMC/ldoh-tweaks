package net.smileycorp.ldoh.mixin;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.smileycorp.ldoh.common.util.ModUtils;
import net.smileycorp.ldoh.integration.tektopia.TektopiaUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends EntityLivingBase {

    public MixinEntityLiving(World world) {
        super(world);
    }

    @Inject(at = @At("HEAD"), method = "processInitialInteract(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Z", cancellable = true)
    public void processInitialInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> callback) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            //join entity team, used to make player join merc teams before opening their gui
            if (player.getTeam() == null) ModUtils.tryJoinTeam(player, this);
            //hack to bypass mixin restrictions
            //check if tektopia is installed because the pack no longer requires it
            if (Loader.isModLoaded("tektopia") && TektopiaUtils.isToken(stack)) {
                //use merc tokens transforming tek villagers code before handling other villager interactions
                stack.interactWithEntity(player, this, hand);
                callback.setReturnValue(true);
                return;
            }
            //pet bandages
            if (ModUtils.isPhoenix(this) && ModUtils.isPetBandage(stack) && getHealth() < getMaxHealth()) {
                float h = 2f;
                int meta = stack.getMetadata();
                if (meta == 2) h = 6;
                heal(h);
                if (meta == 1) addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 3600));
                else if (meta == 3) addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 3600));
                if(!player.isCreative()) stack.shrink(1);
                player.playSound(SoundEvents.BLOCK_GRASS_STEP, 1.0F, 1.0F);
                for (int i = 0; i < 5; ++i) {
                    double d0 = rand.nextGaussian() * 0.02D;
                    double d1 = rand.nextGaussian() * 0.02D;
                    double d2 = rand.nextGaussian() * 0.02D;
                   world.spawnParticle(EnumParticleTypes.TOTEM, posX + (double)(rand.nextFloat() * width * 2f) - width, posY + 1 + (rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2.0F) - (double)width, d0, d1, d2);
                }
                callback.setReturnValue(true);
            }
        }
    }

}
