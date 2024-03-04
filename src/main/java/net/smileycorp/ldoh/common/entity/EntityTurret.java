package net.smileycorp.ldoh.common.entity;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.tile.TileTurret;
import net.smileycorp.ldoh.common.util.TurretUpgrade;
import rafradek.TF2weapons.TF2weapons;

@SuppressWarnings("deprecation")
public class EntityTurret extends EntityAbstractTurret<TileTurret, EntityTurret> {

	protected static final DataParameter<Float> SPIN = EntityDataManager.<Float>createKey(EntityTurret.class, DataSerializers.FLOAT);

	public EntityTurret(World world) {
		super(world, TileTurret.class);
		setSize(0.5f, 0.5f);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SPIN, 0f);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("spin")) dataManager.set(SPIN, nbt.getFloat("spin"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setFloat("spin", dataManager.get(SPIN));
	}

	@Override
	public float getEyeHeight() {
		return 0.25f;
	}

	@Override
	protected void collideWithEntity(Entity entityIn){}

	@Override
	public void onLivingUpdate() {
		if (hasTarget()) {
			EntityLivingBase target = getTarget();
			double x = target.posX + (target.width*0.5);
			double y = target.posY + (target.height * 0.75);
			double z = target.posZ + (target.width*0.5);
			if (hasUpgrade(TurretUpgrade.PREDICTIVE_TARGETING)) {
				double distance = Math.ceil(getDistance(target) / (float)getProjectileSpeed());
				x = x + target.motionX * distance;
				y = y + target.motionY * distance;
				z = z + target.motionZ * distance;
			}
			getLookHelper().setLookPosition(x, y, z, 10, 90);
		}
		if (getCooldown() > 0) {
			setCooldown(getCooldown()-1);
			setSpin(getSpin()+0.34906585f);
		}
		super.onLivingUpdate();
	}

	@Override
	public void dropEnemyItems() {
		entityDropItem(new ItemStack(ModGuns.BASIC_AMMO, rand.nextInt(15)+10), 0.0f);
		entityDropItem(new ItemStack(LDOHItems.INCENDIARY_AMMO, rand.nextInt(6)), 0.0f);
		entityDropItem(new ItemStack(LDOHItems.DIAMOND_NUGGET, rand.nextInt(3)+1), 0.0f);
		entityDropItem(new ItemStack(Items.QUARTZ, rand.nextInt(3)+1), 0.0f);
		if (rand.nextInt(100) < 50) entityDropItem(new ItemStack(TF2weapons.itemTF2, 1, 3), 0.0f);
		if (rand.nextInt(100) < 25) entityDropItem(new ItemStack(ModGuns.CHAIN_GUN), 0.0f);
	}

	@Override
	public void readFromTile(EntityPlayer owner, TileTurret tile, NBTTagCompound nbt, EnumFacing facing) {
		if (nbt.hasKey("isEnemy")) {
			dataManager.set(IS_ENEMY, nbt.getBoolean("isEnemy"));
			updateUpgrades(TurretUpgrade.AMMO_OPTIMIZATION, TurretUpgrade.BARREL_SPIN);
		}
		super.readFromTile(owner, tile, nbt, facing);
	}

	public int getCooldown() {
		return dataManager.get(COOLDOWN);
	}

	public void setCooldown(int cooldown) {
		dataManager.set(COOLDOWN, cooldown);
	}

	public float getSpin() {
		return dataManager.get(SPIN);
	}

	public void setSpin(float spin) {
		dataManager.set(SPIN, spin);
	}

	public ItemStack getAmmo(Entity target) {
		boolean optimize = hasUpgrade(TurretUpgrade.AMMO_OPTIMIZATION);
		return isEnemy() ? new ItemStack((target instanceof EntityParasiteBase && optimize) ?
				LDOHItems.INCENDIARY_AMMO : ModGuns.BASIC_AMMO) : inventory.getAmmo(optimize ? target : null);
	}

	public int getFireRate() {
		return 4 - getUpgradeCount(TurretUpgrade.BARREL_SPIN);
	}

	public int getRange () {
		return 30 + (20 * getUpgradeCount(TurretUpgrade.RANGE));
	}

	public int getProjectileSpeed() {
		return 10 * (1 + getUpgradeCount(TurretUpgrade.RIFLING));
	}

	@Override
	public String getName() {
		return isEnemy() ? I18n.translateToLocal("entity.hundreddayz.EnemyTurret.name") : super.getName();
	}

	@Override
	public boolean canApplyUpgrade(TurretUpgrade upgrade) {
		return true;
	}

	@Override
	public void shoot(Vec3d pos, EntityLivingBase entity) {
		Gun fakegun = ((ItemGun) ModGuns.CHAIN_GUN).getGun();
		fakegun.projectile.life = 60;
		fakegun.projectile.speed = getProjectileSpeed();
		ItemStack ammo = getAmmo(getTarget());
		ProjectileFactory factory = AmmoRegistry.getInstance().getFactory(ammo.getItem().getRegistryName());
		EntityProjectile bullet = factory.create(world, this, (ItemGun) ModGuns.CHAIN_GUN, fakegun);
		bullet.setPosition(pos.x, pos.y, pos.z);
		Vec3d dir = this.getLook(1f);
		bullet.motionX = dir.x * bullet.getProjectile().speed;
		bullet.motionY = dir.y * bullet.getProjectile().speed;
		bullet.motionZ = dir.z * bullet.getProjectile().speed;
		world.spawnEntity(bullet);
		ammo.shrink(1);
		setCooldown(getFireRate());
		String sound = fakegun.sounds.getFire(fakegun);
		SoundEvent event = ModSounds.getSound(sound);
		if(event == null) event = SoundEvent.REGISTRY.getObject(new ResourceLocation(sound));
		if(event != null) world.playSound(null, getPosition(), event, SoundCategory.HOSTILE, 5.0F, 0.8F + world.rand.nextFloat() * 0.2F);
		MessageBullet messageBullet = new MessageBullet(bullet.getEntityId(), bullet.posX, bullet.posY, bullet.posZ, bullet.motionX, bullet.motionY, bullet.motionZ, 0, 0);
		PacketHandler.INSTANCE.sendToAllAround(messageBullet, new NetworkRegistry.TargetPoint(dimension, posX, posY, posZ, GunConfig.SERVER.network.projectileTrackingRange));
		if (entity != target &! entity.isDead) {
			setTarget(entity);
		}
	}

}
