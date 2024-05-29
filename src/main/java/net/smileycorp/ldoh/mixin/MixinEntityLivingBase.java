package net.smileycorp.ldoh.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.smileycorp.ldoh.common.entity.projectile.EntityAPProjectile;
import net.smileycorp.ldoh.common.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {
    
    @Shadow public EnumHand swingingHand;
    
    public MixinEntityLivingBase(World world) {
        super(world);
    }

    @Shadow
    public float attackedAtYaw;

    @Shadow
    protected int idleTime;

    @Shadow
    public float limbSwingAmount;

    @Shadow
    protected float lastDamage;

    @Shadow
    protected int recentlyHit;

    @Shadow
    protected EntityPlayer attackingPlayer;

    @Shadow
    private DamageSource lastDamageSource;

    @Shadow
    private long lastDamageStamp;

    @Inject(at = @At("HEAD"), method = "attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", cancellable = true)
    public void attackEntityFrom(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callback) {
        //make mrcrafyish bullets ignore damage cooldown
        if (ModUtils.isProjectile(source)) {
            //mostly duplicate of vanilla behaviour but without the damage cooldown
            callback.cancel();
            if (!ForgeHooks.onLivingAttack((EntityLivingBase) (Entity) this, source, amount)) {
                callback.setReturnValue(false);
            } else if (this.isEntityInvulnerable(source)) {
                callback.setReturnValue(false);
            } else if (this.world.isRemote) {
                callback.setReturnValue(false);
            } else {
                this.idleTime = 0;
                if (this.getHealth() <= 0.0F) {
                    callback.setReturnValue(false);
                } else {
                    float f = amount;

                    boolean flag = false;
                    if (amount > 0.0F && this.canBlockDamageSource(source)) {
                        this.damageShield(amount);
                        amount = 0.0F;
                        if (!source.isProjectile()) {
                            Entity entity = source.getImmediateSource();
                            if (entity instanceof EntityLivingBase) {
                                this.blockUsingShield((EntityLivingBase) entity);
                            }
                        }

                        flag = true;
                    }

                    this.limbSwingAmount = 1.5F;
                    boolean flag1 = true;
                    this.lastDamage = amount;
                    this.damageEntity(source, amount);

                    this.attackedAtYaw = 0.0F;
                    Entity entity1 = source.getTrueSource();
                    if (entity1 != null) {
                        if (entity1 instanceof EntityLivingBase) {
                            this.setRevengeTarget((EntityLivingBase) entity1);
                        }

                        if (entity1 instanceof EntityPlayer) {
                            this.recentlyHit = 100;
                            this.attackingPlayer = (EntityPlayer) entity1;
                        } else if (entity1 instanceof EntityTameable) {
                            EntityTameable entitywolf = (EntityTameable) entity1;
                            if (entitywolf.isTamed()) {
                                this.recentlyHit = 100;
                                this.attackingPlayer = null;
                            }
                        }
                    }

                    if (flag1) {
                        if (flag) {
                            this.world.setEntityState(this, (byte) 29);
                            this.world.setEntityState(this, (byte) 2);
                        }

                        markVelocityChanged();

                        if (entity1 != null) {
                            double d1 = entity1.posX - this.posX;

                            double d0;
                            for (d0 = entity1.posZ - this.posZ; d1 * d1 + d0 * d0 < 1.0E-4; d0 = (Math.random() - Math.random()) * 0.01) {
                                d1 = (Math.random() - Math.random()) * 0.01;
                            }

                            this.attackedAtYaw = (float) (MathHelper.atan2(d0, d1) * 57.29577951308232 - (double) this.rotationYaw);
                            this.knockBack(entity1, 0.4F, d1, d0);
                        } else {
                            this.attackedAtYaw = (float) ((int) (Math.random() * 2.0) * 180);
                        }
                    }

                    if (this.getHealth() <= 0.0F) {
                        if (!this.checkTotemDeathProtection(source)) {
                            SoundEvent soundevent = this.getDeathSound();
                            if (flag1 && soundevent != null) {
                                this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
                            }

                            this.onDeath(source);
                        }
                    } else if (flag1) {
                        this.playHurtSound(source);
                    }

                    boolean flag2 = !flag || amount > 0.0F;
                    if (flag2) {
                        this.lastDamageSource = source;
                        this.lastDamageStamp = this.world.getTotalWorldTime();
                    }

                    if (((Entity) this) instanceof EntityPlayerMP) {
                        CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((EntityPlayerMP) (Entity) this, source, f, amount, flag);
                    }

                    if (entity1 instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((EntityPlayerMP) entity1, this, source, f, amount, flag);
                    }
                    callback.setReturnValue(flag2);
                }
            }
        }
    }
    
    @Inject(at = @At("HEAD"), method = "applyArmorCalculations", cancellable = true)
    public void applyArmorCalculations(DamageSource source, float damage, CallbackInfoReturnable<Float> callback) {
        if (source.getImmediateSource() instanceof EntityAPProjectile) callback.cancel();
    }

    @Shadow
    public abstract float getHealth();

    @Shadow
    protected abstract boolean canBlockDamageSource(DamageSource p_184583_1_);

    @Shadow
    protected abstract void damageShield(float p_184590_1_);

    @Shadow
    protected abstract void blockUsingShield(EntityLivingBase p_190629_1_);

    @Shadow
    public abstract void setRevengeTarget(EntityLivingBase p_70604_1_);

    @Shadow
    public abstract void knockBack(Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_);

    @Shadow
    protected abstract boolean checkTotemDeathProtection(DamageSource p_190628_1_);

    @Shadow
    public abstract void damageEntity(DamageSource p_70665_1_, float p_70665_2_);

    @Shadow
    protected abstract SoundEvent getDeathSound();

    @Shadow
    protected abstract float getSoundVolume();

    @Shadow
    protected abstract float getSoundPitch();

    @Shadow
    public abstract void onDeath(DamageSource p_70645_1_);

    @Shadow
    protected abstract void playHurtSound(DamageSource p_184581_1_);

}
