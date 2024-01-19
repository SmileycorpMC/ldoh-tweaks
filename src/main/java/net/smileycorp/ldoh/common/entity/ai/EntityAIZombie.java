package net.smileycorp.ldoh.common.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIZombie extends EntityAIBase {

    protected final EntityZombie entity;
    protected final World world;
    protected Vec3d pos = null;
    protected final PathNavigate pather;
    protected int timeToRecalcPath;
    protected float waterCost;
    protected int attackTick;
    private int raiseArmTicks;

    public EntityAIZombie(EntityZombie entity) {
        super();
        timeToRecalcPath = entity.getEntityId() % 20;
        this.entity = entity;
        world = entity.world;
        pather = entity.getNavigator();
    }

    public void resetTask() {
        entity.setArmsRaised(false);
        pos = null;
    }

    public void startExecuting() {
        waterCost = entity.getPathPriority(PathNodeType.WATER);
        timeToRecalcPath = entity.world.rand.nextInt(5)+1;
    }

    public boolean shouldExecute() {
        if (pos != null && pos.distanceTo(entity.getPositionVector()) <= 0.1) return false;
        if (entity.getAttackTarget() != null) return true;
        return false;
    }

    public void updateTask() {
        if (--timeToRecalcPath <= 0) {
            if (entity.getAttackTarget() != null) pos = entity.getAttackTarget().getPositionVector();
            timeToRecalcPath = 20;
            pather.tryMoveToXYZ(pos.x, pos.y, pos.z, 1);
        }
        if (entity.getAttackTarget() != null) {
            attackTick = Math.max(this.attackTick - 1, 0);
            EntityLivingBase target = entity.getAttackTarget();
            entity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
            double distance = entity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            checkAndPerformAttack(target, distance);
        }
    }

    protected void checkAndPerformAttack(EntityLivingBase target, double distance) {
        double d0 = getAttackReachSqr(target);
        if (distance <= d0 && this.attackTick <= 0) {
            this.attackTick = 20;
            this.entity.swingArm(EnumHand.MAIN_HAND);
            this.entity.attackEntityAsMob(target);
        }
    }

    protected double getAttackReachSqr(EntityLivingBase target) {
        return entity.width * 2.0F * entity.width * 2.0F + target.width;
    }

}
