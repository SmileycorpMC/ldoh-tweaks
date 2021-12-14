package net.smileycorp.ldoh.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.smileycorp.ldoh.common.entity.EntityTurret;

public class AITurretTarget extends EntityAIBase {

	protected final EntityTurret turret;

	public AITurretTarget(EntityTurret turret) {
		this.turret = turret;
	}

	@Override
	public boolean shouldExecute() {
		return !turret.hasTarget();
	}

	@Override
	public void updateTask() {
		for (int i = 0; i < 70; i+=10) {
			for (EntityLivingBase entity : turret.world.getEntitiesWithinAABB(EntityLiving.class,
					new AxisAlignedBB(turret.posX - i, turret.posY - i, turret.posZ - i, turret.posX + i, turret.posY + i, turret.posZ + i), (e) -> turret.canTarget(e))) {
				turret.getEntitySenses().canSee(entity);
				turret.setTarget(entity);
				return;
			}
		}
	}

}
