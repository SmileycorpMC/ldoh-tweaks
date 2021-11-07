package net.smileycorp.ldoh.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.smileycorp.ldoh.common.entity.EntityTurret;

public class EntityTurretTarget extends EntityAIBase {

	protected final EntityTurret turret;

	protected AxisAlignedBB targetArea;

	public EntityTurretTarget(EntityTurret turret) {
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
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isSpectator())
			if (turret.getTeam()!=null && entity.getTeam()!=null) {
				if (!turret.getTeam().isSameTeam(entity.getTeam())) return true;
			} else if (entity instanceof EntityMob) return true;
		return false;
	}

}
