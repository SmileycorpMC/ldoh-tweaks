package net.smileycorp.ldoh.common.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.ldoh.common.entity.ai.EntityTurretTarget;
import net.smileycorp.ldoh.common.tile.TileTurret;

public class EntityTurret extends EntityLiving {

	protected static final DataParameter<String> TEAM = EntityDataManager.<String>createKey(EntityTurret.class, DataSerializers.STRING);
	protected static final DataParameter<EnumFacing> PLACEMENT = EntityDataManager.<EnumFacing>createKey(EntityTurret.class, DataSerializers.FACING);
	protected static final DataParameter<Rotations> FACING = EntityDataManager.<Rotations>createKey(EntityTurret.class, DataSerializers.ROTATIONS);

	protected EntityPlayer owner = null;
	protected UUID ownerUUID = null;
	protected Entity target = null;
	protected BlockPos tilePos;

	protected NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);

	public EntityTurret(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {
		dataManager.register(TEAM, null);
		dataManager.register(PLACEMENT, EnumFacing.UP);
		dataManager.register(FACING, new Rotations(0, 1, 0));
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40);
		setHealth(40);
	}

	@Override
	protected void initEntityAI() {
		targetTasks.addTask(1, new EntityTurretTarget(this));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("inventory")) ItemStackHelper.loadAllItems(nbt.getCompoundTag("inventory"), inventory);
		if (nbt.hasKey("owner")) ownerUUID = UUID.fromString(nbt.getString("owner"));
		if (nbt.hasKey("tilePos")) {
			NBTTagCompound pos =  nbt.getCompoundTag("tilePos");
			tilePos = new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z"));
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setTag("inventory", ItemStackHelper.saveAllItems(new NBTTagCompound(), inventory));
		if (owner!=null) nbt.setString("owner", ownerUUID.toString());
		if (tilePos != null) {
			NBTTagCompound pos = new NBTTagCompound();
			pos.setInteger("x", tilePos.getX());
			pos.setInteger("y", tilePos.getY());
			pos.setInteger("z", tilePos.getZ());
			nbt.setTag("tilePos", pos);
		}
	}

	public void readFromTile(EntityPlayer owner, TileTurret tile, NBTTagCompound nbt, EnumFacing facing) {
		ownerUUID = owner.getUniqueID();
		this.owner = owner;
		if (owner.getTeam() != null) {
			dataManager.set(TEAM, owner.getTeam().getName());
		}
		tilePos = tile.getPos();
		if (nbt.hasKey("inventory")) ItemStackHelper.loadAllItems(nbt.getCompoundTag("inventory"), inventory);
		if (nbt.hasKey("health")) setHealth(nbt.getFloat("health"));
		dataManager.set(PLACEMENT, facing);
		Vec3i vec = facing.getDirectionVec();
		dataManager.set(FACING, new Rotations(vec.getX(), vec.getY(), vec.getZ()));
	}

	public NBTTagCompound saveToTile() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("inventory", ItemStackHelper.saveAllItems(new NBTTagCompound(), inventory));
		nbt.setFloat("health", getHealth());
		return nbt;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (ticksExisted%5 == 0) {
			if (owner == null || owner.isDead |! owner.isAddedToWorld()) {
				if (!world.isRemote) owner = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(ownerUUID);
			}
			if (owner != null) {
				if (owner.getTeam() != null &! owner.getTeam().isSameTeam(getTeam())) {
					dataManager.set(TEAM, owner.getTeam().getName());
				} else if (dataManager.get(TEAM) != null) {
					dataManager.set(TEAM, null);
				}
			}
		}
		if (hasTarget()) {
			Vec3d vec = DirectionUtils.getDirectionVec(this, target);
			dataManager.set(FACING, new Rotations((float)vec.x, (float)vec.y, (float)vec.z));
		}
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		if (tilePos!=null) {
			world.destroyBlock(tilePos, false);
			world.removeTileEntity(tilePos);
		}
	}

	public boolean hasTarget() {
		return target!=null && (!target.isDead && target.isAddedToWorld()) &!(getDistance(target) > 60);
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public Entity getTarget() {
		return target;
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	protected boolean canDespawn() {
		return true;
	}

	@Override
	public Team getTeam() {
		return world.getScoreboard().getTeam(dataManager.get(TEAM));
	}

	public EntityPlayer getOwner() {
		return owner;
	}

	public UUID getOwnerUUID() {
		return ownerUUID;
	}

	public EnumFacing getPlacement() {
		return dataManager.get(PLACEMENT);
	}

	public Rotations getFacing() {
		return dataManager.get(FACING);
	}

}
