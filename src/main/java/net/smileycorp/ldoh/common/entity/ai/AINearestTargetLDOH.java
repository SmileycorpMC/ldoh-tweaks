package net.smileycorp.ldoh.common.entity.ai;

import com.Fishmod.mod_LavaCow.entities.IAggressive;
import com.Fishmod.mod_LavaCow.entities.tameable.EntityFishTameable;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.Loader;
import net.smileycorp.ldoh.integration.tektopia.TektopiaUtils;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;

import java.util.Collections;
import java.util.List;

public class AINearestTargetLDOH extends EntityAITarget {

    public AINearestTargetLDOH(EntityCreature entity) {
        super(entity, true);
    }

    @Override
    public boolean shouldExecute() {
        if (taskOwner.getAttackTarget() != null && taskOwner.getAttackTarget().isEntityAlive()) return false;
        if (taskOwner.getRNG().nextInt(10) != 0) return false;
        List<EntityLiving> list = taskOwner.world.getEntitiesWithinAABB(EntityLiving.class, this.getTargetableArea(this.getTargetDistance()), this::canTarget);
        if (list.isEmpty()) return false;
        Collections.sort(list, (e1, e2) -> {
            double d1 = taskOwner.getDistanceSq(e1);
            double d2 = taskOwner.getDistanceSq(e2);
            return d2 < d1 ? -1 : d2 > d1 ? 1 : 0;
        });
        target = list.get(0);
        return true;
    }

    public void startExecuting() {
        this.taskOwner.setAttackTarget(target);
        super.startExecuting();
    }

    protected double getTargetDistance() {
        IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }

    private AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    private boolean canTarget(EntityLiving entity) {
        if (entity == null || !entity.isEntityAlive()) return false;
        if (entity.isInvisible()) return false;
        if (entity instanceof EntityTF2Character || entity instanceof EntityParasiteBase) return true;
        if (Loader.isModLoaded("tektopia") && TektopiaUtils.isVillager(entity)) return true;
        if (entity instanceof IAggressive || entity instanceof IMob || entity instanceof EntityFishTameable) return false;
        return entity instanceof EntityAnimal;
    }


}
