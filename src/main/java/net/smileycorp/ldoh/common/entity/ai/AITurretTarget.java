package net.smileycorp.ldoh.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.util.ModUtils;

public class AITurretTarget extends EntityAIBase {

	protected final EntityTurret turret;

	protected AxisAlignedBB targetArea;

	public AITurretTarget(EntityTurret turret) {
		this.turret = turret;
		targetArea = new AxisAlignedBB(turret.posX - 60, turret.posY - 60, turret.posZ - 60, turret.posX + 60, turret.posY - 60, turret.posZ - 60);
	}

	@Override
	public boolean shouldExecute() {
		return !turret.hasTarget();
	}

	@Override
	public void startExecuting() {
		for (EntityLivingBase entity : turret.world.getEntitiesWithinAABB(EntityLiving.class, targetArea, (e)->canTarget(e))) {
			turret.getEntitySenses().canSee(entity);
			turret.setTarget(entity);
			return;
		}
	}

	private boolean canTarget(EntityLivingBase entity) {
		if (turret.getDistance(entity)>60) return false;
		return ModUtils.canTarget(turret, entity);
	}

}
