package net.smileycorp.ldoh.common.entity;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.util.DataUtils;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.entity.ai.AITurretShoot;
import net.smileycorp.ldoh.common.entity.ai.AITurretTarget;
import net.smileycorp.ldoh.common.inventory.InventoryTurretAmmo;
import net.smileycorp.ldoh.common.inventory.InventoryTurretUpgrades;
import net.smileycorp.ldoh.common.item.ItemTurretUpgrade;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.tile.TileAbstractTurret;
import net.smileycorp.ldoh.common.util.ModUtils;
import net.smileycorp.ldoh.common.util.TurretUpgrade;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
public abstract class EntityAbstractTurret<T extends TileAbstractTurret<P>, P extends EntityAbstractTurret<T, P>> extends EntityLiving implements IEnemyMachine {

	protected static final DataParameter<String> TEAM = EntityDataManager.<String>createKey(EntityAbstractTurret.class, DataSerializers.STRING);
	protected static final DataParameter<Boolean> IS_ENEMY = EntityDataManager.<Boolean>createKey(EntityAbstractTurret.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<EnumFacing> FACING = EntityDataManager.<EnumFacing>createKey(EntityAbstractTurret.class, DataSerializers.FACING);
	protected static final DataParameter<Integer> COOLDOWN = EntityDataManager.<Integer>createKey(EntityAbstractTurret.class, DataSerializers.VARINT);
	protected static final DataParameter<Integer> TARGET = EntityDataManager.<Integer>createKey(EntityAbstractTurret.class, DataSerializers.VARINT);

	//read these values from the nbt and cache them to make sure it gets loaded / synced properly
	protected static final DataParameter<BlockPos> TILE_POS = EntityDataManager.<BlockPos>createKey(EntityAbstractTurret.class, DataSerializers.BLOCK_POS);
	protected static final DataParameter<String> OWNER_UUID = EntityDataManager.<String>createKey(EntityAbstractTurret.class, DataSerializers.STRING);

	public static final DataParameter<BlockPos> TURRET_UPGRADES = EntityDataManager.<BlockPos>createKey(EntityAbstractTurret.class, DataSerializers.BLOCK_POS);

	private final Class<T> tileClass;

	protected EntityLivingBase target = null;
	protected InventoryTurretAmmo inventory = new InventoryTurretAmmo();
	protected InventoryTurretUpgrades upgrades = new InventoryTurretUpgrades(this);

	protected EntityPlayer owner = null;
	protected T tile = null;
	protected String username = null;
	protected Vec3d turretPos;

	public EntityAbstractTurret(World world, Class<T> tileClass) {
		super(world);
		this.tileClass = tileClass;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(TEAM, "");
		dataManager.register(IS_ENEMY, false);
		dataManager.register(FACING, EnumFacing.UP);
		dataManager.register(COOLDOWN, 0);
		dataManager.register(TARGET, getEntityId());
		dataManager.register(TILE_POS, null);
		dataManager.register(OWNER_UUID, "");
		dataManager.register(TURRET_UPGRADES, new BlockPos(0, 0, 0));
	}

	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0);
		getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40);
		getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(255);
	}

	@Override
	protected void initEntityAI() {
		targetTasks.addTask(1, new AITurretTarget(this));
		tasks.addTask(1, new AITurretShoot(this));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("inventory")) inventory.readFromNBT(nbt.getCompoundTag("inventory"));
		if (nbt.hasKey("isEnemy")) dataManager.set(IS_ENEMY, nbt.getBoolean("isEnemy"));
		if (!isEnemy() && nbt.hasKey("owner")) dataManager.set(OWNER_UUID, nbt.getString("owner"));
		if (nbt.hasKey("tilePos")) {
			NBTTagCompound pos = nbt.getCompoundTag("tilePos");
			dataManager.set(TILE_POS, new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z")));
		}
		if (nbt.hasKey("facing")) {
			dataManager.set(FACING, EnumFacing.getFront(nbt.getInteger("facing")));
		}
		if (nbt.hasKey("cooldown")) dataManager.set(COOLDOWN, nbt.getInteger("cooldown"));
		if (nbt.hasKey("upgrades")) dataManager.set(TURRET_UPGRADES, ModUtils.arrayToPos(nbt.getIntArray("upgrades")));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setTag("inventory", inventory.writeToNBT());
		nbt.setBoolean("isEnemy", isEnemy());
		if (owner != null) nbt.setString("owner", dataManager.get(OWNER_UUID));
		BlockPos tilePos = dataManager.get(TILE_POS);
		if (tilePos != null) {
			NBTTagCompound pos = new NBTTagCompound();
			pos.setInteger("x", tilePos.getX());
			pos.setInteger("y", tilePos.getY());
			pos.setInteger("z", tilePos.getZ());
			nbt.setTag("tilePos", pos);
		}
		nbt.setInteger("facing", dataManager.get(FACING).ordinal());
		nbt.setInteger("cooldown", dataManager.get(COOLDOWN));
		int[] upgrades = ModUtils.posToArray(dataManager.get(TURRET_UPGRADES));
		for (int upgrade : upgrades) {
			if (!TurretUpgrade.isBlank(upgrade)) {
				nbt.setIntArray("upgrades", upgrades);
				return;
			}
		}
	}

	public void readFromTile(EntityPlayer owner, T tile, NBTTagCompound nbt, EnumFacing facing) {
		if (!isEnemy()) {
			dataManager.set(OWNER_UUID, owner.getUniqueID().toString());
			this.owner = owner;
			if (owner.getTeam() != null) {
				dataManager.set(TEAM, owner.getTeam().getName());
			}
		}
		dataManager.set(TILE_POS, tile.getPos().subtract(this.getPosition()));
		dataManager.set(FACING, facing);
		if (nbt.hasKey("inventory")) inventory.readFromNBT(nbt.getCompoundTag("inventory"));
		if (nbt.hasKey("upgrades")) dataManager.set(TURRET_UPGRADES, ModUtils.arrayToPos(nbt.getIntArray("upgrades")));
		if (nbt.hasKey("health")) setHealth(nbt.getFloat("health"));
	}

	public NBTTagCompound saveToItem() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (isEnemy()) nbt.setBoolean("isEnemy", isEnemy());
		nbt.setTag("inventory", inventory.writeToNBT());
		if (getHealth() < getMaxHealth()) nbt.setFloat("health", getHealth());
		int[] upgrades = ModUtils.posToArray(dataManager.get(TURRET_UPGRADES));
		for (int upgrade : upgrades) {
			if (!TurretUpgrade.isBlank(upgrade)) {
				nbt.setIntArray("upgrades", upgrades);
				return nbt;
			}
		}
		return nbt;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected void collideWithEntity(Entity entityIn){}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (tile == null) {
			BlockPos tilepos = dataManager.get(TILE_POS);
			TileEntity tile = world.getTileEntity(getPosition().add(tilepos));
			if (tile != null && tile.getClass() == tileClass) {
				this.tile = (T) tile;
				this.tile.setEntity((P) this);
				// update turrets placed before 0.4.6 to the new system
			} else if (world.getTileEntity(tilepos) != null && world.getTileEntity(tilepos).getClass() == tileClass) {
				tile = world.getTileEntity(tilepos);
				this.tile = (T) tile;
				this.tile.setEntity((P) this);
				dataManager.set(TILE_POS, tile.getPos().subtract(this.getPosition()));
			} else {
				world.removeEntity(this);
			}
		}
		if (ticksExisted%5 == 0 &! isEnemy()) {
			if (world.isRemote && username == null) {
				String uuidString = dataManager.get(OWNER_UUID);
				if (DataUtils.isValidUUID(uuidString)) {
					username = UsernameCache.getLastKnownUsername(UUID.fromString(uuidString));
				}
				if (username == null) username = uuidString;
			}
			if (owner == null || owner.isDead |! owner.isAddedToWorld()) {
				String uuidString = dataManager.get(OWNER_UUID);
				if (!world.isRemote && DataUtils.isValidUUID(uuidString)) owner = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(UUID.fromString(uuidString));

			}
			//data check owner and update team accordingly
			if (owner != null) {
				if (owner.getTeam() != null) {
					if(!owner.getTeam().isSameTeam(getTeam())) {
						dataManager.set(TEAM, owner.getTeam().getName());
					}
				} else if (dataManager.get(TEAM) != "") {
					dataManager.set(TEAM,"");
				}
			} else if (getTeam() == null) {
				dataManager.set(TEAM, "GREEN");
			}
		}
		if (getPositionVector() != turretPos) {
			posX = turretPos.x;
			posY = turretPos.y;
			posZ = turretPos.z;
		}
	}

	@Override
	public void onAddedToWorld() {
		super.onAddedToWorld();
		turretPos = getPositionVector();
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		if (tile != null &! world.isRemote && (isSameTeam(player) || player.isCreative())) {
			ItemStack stack = player.getHeldItem(hand);
			if (stack.getItem() == Items.IRON_INGOT && getHealth() < getMaxHealth()) {
				heal(4f);
				if (!player.isCreative()) stack.shrink(1);
				playSound(SoundEvents.BLOCK_ANVIL_USE, 0.8f, 1f);
				return EnumActionResult.PASS;
			} else if (stack.getItem() == LDOHItems.TURRET_UPGRADE &! ItemTurretUpgrade.isBlank(stack)) {
				if (applyUpgrade(stack) &! player.isCreative()) {
					stack.shrink(1);
					playSound(SoundEvents.BLOCK_ANVIL_USE, 0.8f, 1f);
					return EnumActionResult.PASS;
				}
			} else if (!player.isSneaking()) {
				BlockPos pos = tile.getPos();
				player.openGui(LDOHTweaks.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
				return EnumActionResult.SUCCESS;
			}
		}
		return super.applyPlayerInteraction(player, vec, hand);
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		BlockPos pos = dataManager.get(TILE_POS);
		if (pos != null) {
			BlockPos tilepos = getPosition().add(pos);
			world.destroyBlock(tilepos, false);
			world.removeTileEntity(tilepos);
		}
		if (world.isRemote) return;
		for (ItemStack stack : inventory.getItems()) entityDropItem(stack, 0.0f);
		for (TurretUpgrade upgrade : getInstalledUpgrades()) if (!isEnemy() || rand.nextInt(100) < 25) entityDropItem(upgrade.getItem(), 0.0f);
		if (isEnemy()) dropEnemyItems();
	}

	public abstract void dropEnemyItems();

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source != null) {
			if (source.getTrueSource() instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) source.getTrueSource();
				if (canTarget(entity)) setTarget(entity);
			} else if (source.getImmediateSource() instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) source.getImmediateSource();
				if (canTarget(entity)) setTarget(entity);
			}
		}
		return super.attackEntityFrom(source, amount);
	}

	public boolean hasTarget() {
		EntityLivingBase target = getTarget();
		if (target == null) return false;
		return !target.isDead && target.isAddedToWorld() && canTarget(target) && getEntitySenses().canSee(target);
	}

	public void setTarget(EntityLivingBase target) {
		this.target = target;
		dataManager.set(TARGET, target == null ? getEntityId() : target.getEntityId());
	}

	public EntityLivingBase getTarget() {
		if (world.isRemote) {
			int id = dataManager.get(TARGET);
			if (id != getEntityId()) {
				Entity target = world.getEntityByID(id);
				if (target instanceof EntityLivingBase) return (EntityLivingBase) target;
			}
			return null;
		} else return target;
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public boolean isInRangeToRenderDist(double distance) {
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 10.0D;
		if (Double.isNaN(d0)) {
			d0 = 1.0D;
		}
		d0 = d0 * 64.0D * getRenderDistanceWeight();
		return distance < d0 * d0;
	}

	@Override
	public boolean isEnemy() {
		return dataManager.get(IS_ENEMY);
	}

	@Override
	public Team getTeam() {
		if (isEnemy()) return null; //TODO: add green team (optional)
		String team = dataManager.get(TEAM);
		return team.isEmpty() ? null : world.getScoreboard().getTeam(team);
	}

	public boolean isSameTeam(Entity entity) {
		if (isEnemy()) {
			if (entity instanceof IEnemyMachine) return (((IEnemyMachine) entity).isEnemy());
			return false;
		}
		if (getTeam() == null) return entity.getTeam() == null;
		return getTeam().equals(entity.getTeam());
	}

	public boolean canTarget(EntityLivingBase entity) {
		if (entity == null) return false;
		if (getDistance(entity) > getRange()) return false;
		if (isEnemy()) {
			if (entity instanceof EntityPlayer) return ((EntityPlayer) entity).isSpectator() || ((EntityPlayer) entity).isCreative() ? false : true;
			if (entity instanceof EntityTF2Character && entity.getTeam() != null) {
				if (!entity.getTeam().getName().equals("GREEN")) return true;
			}
		}
		return ModUtils.canTarget(this, entity);
	}

	public EntityPlayer getOwner() {
		return isEnemy() ? null : owner;
	}

	public UUID getOwnerUUID() {
		if (isEnemy()) return null;
		String uuidString = dataManager.get(OWNER_UUID);
		return DataUtils.isValidUUID(uuidString) ? UUID.fromString(uuidString) : null;
	}

	public EnumFacing getFacing() {
		return dataManager.get(FACING);
	}

	public int getCooldown() {
		return dataManager.get(COOLDOWN);
	}

	public void setCooldown(int cooldown) {
		dataManager.set(COOLDOWN, cooldown);
	}

	public InventoryTurretAmmo getInventory() {
		return inventory;
	}

	public InventoryTurretUpgrades getUpgradeInventory() {
		return upgrades;
	}

	public boolean hasAmmo() {
		return inventory.hasAmmo();
	}

	public abstract ItemStack getAmmo(Entity target);

	@SideOnly(Side.CLIENT)
	public String getOwnerUsername() {
		return username;
	}

	public boolean isActive() {
		if (hasUpgrade(TurretUpgrade.REDSTONE_CONTROL)) {
			BlockPos tile_pos = dataManager.get(TILE_POS);
			if (tile_pos != null) return world.isBlockPowered(tile_pos);
		}
		return true;
	}

	public abstract int getFireRate();

	public abstract int getRange();

	public abstract int getProjectileSpeed();

	public List<TurretUpgrade> getInstalledUpgrades() {
		List<TurretUpgrade> upgrades = Lists.newArrayList();
		for (int i : ModUtils.posToArray(dataManager.get(TURRET_UPGRADES))) if (!TurretUpgrade.isBlank(i)) upgrades.add(TurretUpgrade.get(i));
		return upgrades;
	}

	public boolean hasUpgrade(TurretUpgrade upgrade) {
		for (int i : ModUtils.posToArray(dataManager.get(TURRET_UPGRADES))) if (TurretUpgrade.get(i) == upgrade) return true;
		return false;
	}

	public int getUpgradeCount(TurretUpgrade upgrade) {
		if (upgrade == null) return 0;
		List<TurretUpgrade> upgrades = getInstalledUpgrades();
		if (!upgrade.isStackable()) return upgrades.contains(upgrade) ? 1 :0;
		int count = 0;
		for (int i : ModUtils.posToArray(dataManager.get(TURRET_UPGRADES))) if (TurretUpgrade.get(i) == upgrade) count++;
		return count;
	}

	public boolean applyUpgrade(ItemStack stack) {
		TurretUpgrade upgrade = TurretUpgrade.get(stack.getMetadata());
		if (!canApplyUpgrade(upgrade)) return false;
		int[] array =  ModUtils.posToArray(dataManager.get(TURRET_UPGRADES));
		for (int i = 0; i < array.length; i++) {
			int id = array[i];
			if (TurretUpgrade.isBlank(id)) {
				array[i] = upgrade.ordinal();
				dataManager.set(TURRET_UPGRADES, ModUtils.arrayToPos(array));
				return true;
			}
		}
		return false;
	}

	public void updateUpgrades(TurretUpgrade... upgrades) {
		int[] array = new int[] {0, 0, 0};
		for (int i = 0; i < upgrades.length; i++) {
			if (i == array.length) break;
			TurretUpgrade upgrade = upgrades[i];
			if (upgrade == null || upgrade == TurretUpgrade.BLANK) continue;
			array[i] = upgrade.ordinal();
		}
		dataManager.set(TURRET_UPGRADES, ModUtils.arrayToPos(array));
	}

	public void updateUpgrades(int[] upgrades) {
		if (upgrades.length < 3) return;
		dataManager.set(TURRET_UPGRADES, ModUtils.arrayToPos(upgrades));
	}

	public boolean hasUpgrades() {
		for (int upgrade : ModUtils.posToArray(dataManager.get(TURRET_UPGRADES))) {
			if (!TurretUpgrade.isBlank(upgrade)) {
				return true;
			}
		}
		return false;
	}

	public abstract boolean canApplyUpgrade(TurretUpgrade upgrade);

	public abstract void shoot(Vec3d pos, EntityLivingBase entity);
}
