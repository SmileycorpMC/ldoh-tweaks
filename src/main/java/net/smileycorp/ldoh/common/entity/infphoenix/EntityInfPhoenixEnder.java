package net.smileycorp.ldoh.common.entity.infphoenix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityCanMelt;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPInfected;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.init.SRPSounds;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import java.util.Iterator;
import java.util.List;

public class EntityInfPhoenixEnder extends EntityInfPhoenix {

    public EntityInfPhoenixEnder(World world) {
        super(world);
        ally = 0;
    }

    private double targetX;
    private double targetY;
    private double targetZ;
    private int ally;
    private EntityParasiteBase toTele;
    private int toTeleCool;
    private int spotCool;

    @Override
    protected void updateAITasks() {
        if (isWet()) attackEntityFrom(DamageSource.DROWN, 1.0F);
        super.updateAITasks();
        if (world.isRemote) {
            for(int i = 0; i < 2; ++i) {
                world.spawnParticle(EnumParticleTypes.PORTAL, posX + (rand.nextDouble() - 0.5) * (double)width, posY + rand.nextDouble() * (double)height - 0.25, posZ + (rand.nextDouble() - 0.5) * (double)width, (rand.nextDouble() - 0.5) * 2.0, -rand.nextDouble(), (rand.nextDouble() - 0.5) * 2.0, new int[0]);
            }
        } else if (getAttackTarget() != null && ticksExisted % 20 == 0 && getDistanceSq(getAttackTarget()) > 4.0 && rand.nextInt(SRPConfigMobs.infendermantelefreq) == 0 && !teleportAllies()) {
            teleportRandomly();
        }

        if (!world.isRemote) {
            if (ally > 0) {
                ++ally;
                teleportAlly();
            }

            if (spotCool >= 0) {
                --spotCool;
            }

            if (toTeleCool >= 0) {
                --toTeleCool;
            }

            if (srpTicks == 10 && isPotionActive(SRPPotions.RAGE_E)) {
                spotCool = 0;
                toTeleCool = 0;
            }
        }
    }

    protected boolean teleportAllies() {
        if (ally <= 0 && SRPConfigMobs.infendermanteleally && toTeleCool <= 0 && spotCool <= 0) {
            EntityLivingBase target = getAttackTarget();
            if (target == null) {
                return false;
            } else {
                AxisAlignedBB axisalignedbb = (new AxisAlignedBB(posX, posY, posZ, posX + 1.0, posY + 1.0, posZ + 1.0)).grow(64.0);
                List<EntityPInfected> moblist = world.getEntitiesWithinAABB(EntityPInfected.class, axisalignedbb);
                Iterator var4 = moblist.iterator();

                EntityPInfected entity;
                do {
                    do {
                        do {
                            do {
                                do {
                                    if (!var4.hasNext()) {
                                        var4 = moblist.iterator();

                                        EntityParasiteBase mob;
                                        do {
                                            if (!var4.hasNext()) {
                                                return false;
                                            }

                                            mob = (EntityParasiteBase) var4.next();
                                        } while (!(mob instanceof EntityMudo) || mob.getAttackTarget() != null || mob.getHealth() - SRPConfigMobs.infendermanTeleDamage < 2.0F || !teleportToEntity(mob, 1.0));

                                        setCoordTarget(target.posX, target.posY, target.posZ);
                                        toTele = mob;
                                        setWorkTask(false);
                                        ally = 1;
                                        return true;
                                    }

                                    entity = (EntityPInfected) var4.next();
                                } while (entity == this);
                            } while (entity.getAttackTarget() != null);
                        } while (entity.getParasiteType() > 15);
                    } while (entity instanceof EntityCanMelt && ((EntityCanMelt) entity).isMelting());
                } while (entity.getHealth() - SRPConfigMobs.infendermanTeleDamage < 2.0F || !teleportToEntity(entity, 1.0));

                setCoordTarget(target.posX, target.posY, target.posZ);
                toTele = entity;
                setWorkTask(false);
                ally = 1;
                return true;
            }
        } else {
            return false;
        }
    }

    private void teleportAlly() {
        if (ally >= 8) {
            boolean flag1;
            int lag1;
            if (toTele != null) {
                if (!toTele.isEntityAlive()) {
                    toTele = null;
                    ally = 0;
                    setWorkTask(true);
                    flag1 = false;

                    for (lag1 = 10; !flag1 && lag1 > 0; --lag1) {
                        flag1 = teleportToPos(targetX, targetY, targetZ, 8.0);
                    }

                    return;
                }

                flag1 = false;

                for (lag1 = 10; !flag1 && lag1 > 0; --lag1) {
                    flag1 = teleportToPos(targetX, targetY, targetZ, 8.0);
                    if (flag1) {
                        break;
                    }
                }

                if (flag1) {
                    boolean flag2 = false;
                    toTele.copyLocationAndAnglesFrom(this);
                    if (toTele.getParasiteIDRegister() != 59 && toTele.getParasiteIDRegister() != 69) {
                        toTele.func_70097_a(DamageSource.FALL, SRPConfigMobs.infendermanTeleDamage);
                    }

                    if (isBurning() && rand.nextInt(4) != 0) {
                        toTele.setFire(8);
                    }

                    world.playSound((EntityPlayer) null, toTele.prevPosX, toTele.prevPosY, toTele.prevPosZ, SRPSounds.INFECTEDENDERMAN_PORTAL, getSoundCategory(), 1.0F, 1.0F);
                    flag2 = true;
                    if (flag2) {
                        EntityLivingBase target = getAttackTarget();
                        if (target != null && target.isEntityAlive()) {
                            toTele.setAttackTarget(target);
                        }
                    }
                }
            } else {
                flag1 = false;

                for (lag1 = 10; !flag1 && lag1 > 0; --lag1) {
                    flag1 = teleportToPos(targetX, targetY, targetZ, 8.0);
                }
            }

            setWorkTask(true);
            ally = 0;
            toTele = null;
            toTeleCool = SRPConfigMobs.infendermanallyCool;
        }
    }

    protected boolean teleportRandomly() {
        if (spotCool > 0) {
            return false;
        } else {
            double d0 = posX + (rand.nextDouble() - 0.5) * 64.0;
            double d1 = posY + (double) (rand.nextInt(64) - 32);
            double d2 = posZ + (rand.nextDouble() - 0.5) * 64.0;
            return teleportTo(d0, d1, d2);
        }
    }

    protected boolean teleportToEntity(Entity in, double dis) {
        double d1 = in.posX + (rand.nextDouble() - 0.5) * dis;
        double d2 = in.posY + (double) (rand.nextInt(16) - 8) * dis;
        double d3 = in.posZ + (rand.nextDouble() - 0.5) * dis;
        return teleportTo(d1, d2, d3);
    }

    protected boolean teleportToPos(double x, double y, double z, double dis) {
        double d1 = x + (rand.nextDouble() - 0.5) * dis;
        double d2 = y + (double) (rand.nextInt(16) - 8) * dis;
        double d3 = z + (rand.nextDouble() - 0.5) * dis;
        return teleportTo(d1, d2, d3);
    }

    private boolean teleportTo(EntityParasiteBase entity, double x, double y, double z) {
        EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0.0F);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        } else {
            boolean flag = entity.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            if (flag) {
                entity.world.playSound((EntityPlayer) null, entity.prevPosX, entity.prevPosY, entity.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, entity.getSoundCategory(), 1.0F, 1.0F);
                entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
            }

            return flag;
        }
    }

    private boolean teleportTo(double x, double y, double z) {
        EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0.0F);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        } else {
            boolean flag = attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            if (flag) {
                world.playSound((EntityPlayer) null, prevPosX, prevPosY, prevPosZ, SRPSounds.INFECTEDENDERMAN_PORTAL, getSoundCategory(), 1.0F, 1.0F);
                playSound(SRPSounds.INFECTEDENDERMAN_PORTAL, 1.0F, 1.0F);
            }

            return flag;
        }
    }

    private void setCoordTarget(double x, double y, double z) {
        targetX = x;
        targetY = y;
        targetZ = z;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) return false;
        else if (source instanceof EntityDamageSourceIndirect) {
            for (int i = 0; i < 64; ++i) if (teleportRandomly()) return true;
            return false;
        } else {
            boolean flag = super.attackEntityFrom(source, amount);
            if (source.isUnblockable() && rand.nextInt(10) != 0) teleportRandomly();
            return flag;
        }
    }

    @Override
    public String getName() {
        return "end";
    }

    @Override
    protected boolean isImmune(DamageSource source) {
        return false;
    }

}
