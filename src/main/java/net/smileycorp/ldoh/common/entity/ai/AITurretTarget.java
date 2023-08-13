package net.smileycorp.ldoh.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.smileycorp.ldoh.common.entity.EntityAbstractTurret;
import net.smileycorp.ldoh.common.entity.EntityTurret;

public class AITurretTarget extends EntityAIBase {

	protected final EntityAbstractTurret turret;
	protected float distance = 100;
	protected EntityLivingBase target = null;

	public AITurretTarget(EntityAbstractTurret turret) {
		this.turret = turret;
	}

	@Override
	public boolean shouldExecute() {
		return !turret.hasTarget() && turret.isActive();
	}

	@Override
	public void updateTask() {
		int range = turret.getRange();
		for (EntityLivingBase entity : turret.world.getEntitiesWithinAABB(EntityLiving.class,
				new AxisAlignedBB(turret.posX - range, turret.posY - range, turret.posZ - range,
						turret.posX + range, turret.posY + range, turret.posZ + range), turret::canTarget)) {
			float distance = turret.getDistance(entity);
			if (turret.getEntitySenses().canSee(entity) && distance < this.distance) {
				target = entity;
				this.distance = distance;
			}
		}
		if (target != null) {
			turret.setTarget(target);
			distance = 100;
		}
	}

}
