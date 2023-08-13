package net.smileycorp.ldoh.common.entity.ai;

import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.common.ProjectileFactory;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.item.AmmoRegistry;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageBullet;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.smileycorp.ldoh.common.entity.EntityAbstractTurret;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.util.ModUtils;

public class AITurretShoot extends EntityAIBase {

	protected static final float LENGTH = 0.5f;
	protected static final float SPEED = 5f;
	protected final Gun fakegun;
	protected int idleTimer = 0;

	protected final EntityAbstractTurret turret;

	public AITurretShoot(EntityAbstractTurret turret) {
		this.turret = turret;
		fakegun = ((ItemGun) ModGuns.CHAIN_GUN).getGun();
		fakegun.projectile.life = 60;
		fakegun.projectile.speed = turret.getProjectileSpeed();
	}

	@Override
	public boolean shouldExecute() {
		return turret.hasTarget() && turret.getCooldown() == 0 && (turret.hasAmmo() || turret.isEnemy()) && turret.isActive();
	}

	@Override
	public void updateTask() {
		Vec3d dir = turret.getLook(1f);
		Vec3d pos = turret.getPositionEyes(1f).addVector(dir.x*LENGTH, dir.y*LENGTH, dir.z*LENGTH);
		EntityLivingBase target = turret.getTarget();
		if (target.isEntityAlive()) {
			if (!(turret.getEntitySenses().canSee(target) && turret.getDistance(target) < 100)) {
				turret.setTarget(null);
				return;
			}
			RayTraceResult ray = ModUtils.rayTrace(turret.world, turret, turret.getDistance(target)+5);
			if (ray != null) {
				if (ray.typeOfHit == RayTraceResult.Type.ENTITY) {
					if (ray.entityHit instanceof EntityLivingBase) {
						EntityLivingBase entity = (EntityLivingBase) ray.entityHit;
						if (turret.canTarget(entity)) {
							turret.shoot(pos);
							idleTimer = 0;
							return;
						}
					}
				}
			}
			if (idleTimer >= 20) {
				turret.setTarget(null);
				idleTimer = 0;
			}
			idleTimer++;
		} else {
			turret.setTarget(null);
			return;
		}
	}

}
