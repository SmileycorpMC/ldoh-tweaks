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
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.util.ModUtils;

public class AITurretShoot extends EntityAIBase {

	protected static final float LENGTH = 0.5f;
	protected static final float SPEED = 5f;
	protected Gun fakegun = ((ItemGun) ModGuns.CHAIN_GUN).getModifiedGun(new ItemStack(ModGuns.CHAIN_GUN));
	protected int idleTimer = 0;

	protected final EntityTurret turret;

	public AITurretShoot(EntityTurret turret) {
		this.turret = turret;
	}

	@Override
	public boolean shouldExecute() {
		return turret.hasTarget() && turret.getCooldown() == 0 && (turret.hasAmmo() || turret.isEnemy());
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
							ItemStack ammo = turret.getAmmo(entity);
							ProjectileFactory factory = AmmoRegistry.getInstance().getFactory(ammo.getItem().getRegistryName());
							EntityProjectile bullet = factory.create(turret.world, turret, (ItemGun) ModGuns.CHAIN_GUN, fakegun);
							bullet.setPosition(pos.x, pos.y, pos.z);
							bullet.motionX = dir.x * SPEED;
							bullet.motionY = dir.y * SPEED;
							bullet.motionZ = dir.z * SPEED;
							turret.world.spawnEntity(bullet);
							ammo.shrink(1);
							turret.setCooldown(3);
							String sound = fakegun.sounds.getFire(fakegun);
							SoundEvent event = ModSounds.getSound(sound);
							if(event == null) event = SoundEvent.REGISTRY.getObject(new ResourceLocation(sound));
							if(event != null) turret.world.playSound(null, turret.getPosition(), event, SoundCategory.HOSTILE, 5.0F, 0.8F + turret.world.rand.nextFloat() * 0.2F);
							MessageBullet messageBullet = new MessageBullet(bullet.getEntityId(), bullet.posX, bullet.posY, bullet.posZ, bullet.motionX, bullet.motionY, bullet.motionZ, 0, 0);
							PacketHandler.INSTANCE.sendToAllAround(messageBullet, new NetworkRegistry.TargetPoint(turret.dimension, turret.posX, turret.posY, turret.posZ, GunConfig.SERVER.network.projectileTrackingRange));
							if (ray.entityHit != target &! ray.entityHit.isDead) {
								turret.setTarget((EntityLivingBase) ray.entityHit);
							}
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
