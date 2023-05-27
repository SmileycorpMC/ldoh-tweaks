package net.smileycorp.ldoh.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.smileycorp.ldoh.common.entity.EntityTurret;

public class AITurretTarget extends EntityAIBase {

	protected final EntityTurret turret;
	protected float distance = 100;
	protected EntityLivingBase target = null;

	public AITurretTarget(EntityTurret turret) {
		this.turret = turret;
	}

	@Override
	public boolean shouldExecute() {
		return !turret.hasTarget();
	}

	@Override
	public void updateTask() {
		for (EntityLivingBase entity : turret.world.getEntitiesWithinAABB(EntityLiving.class,
				new AxisAlignedBB(turret.posX - 50, turret.posY - 50, turret.posZ - 50, turret.posX + 50, turret.posY + 50, turret.posZ + 50), turret::canTarget)) {
			float distance = turret.getDistance(entity);
			if (turret.getEntitySenses().canSee(entity) && distance < this.distance) {
				target = entity;
				this.distance = distance;
			}
		}
		if (target != null) {
			turret.setTarget(target);
			distance = 100;
			return;
		}
	}

}
