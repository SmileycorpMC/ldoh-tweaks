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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.smileycorp.ldoh.common.entity.ai.AITurretTarget;
import net.smileycorp.ldoh.common.tile.TileTurret;

public class EntityTurret extends EntityLiving {

	protected static final DataParameter<String> TEAM = EntityDataManager.<String>createKey(EntityTurret.class, DataSerializers.STRING);
	protected static final DataParameter<EnumFacing> PLACEMENT = EntityDataManager.<EnumFacing>createKey(EntityTurret.class, DataSerializers.FACING);

	protected EntityPlayer owner = null;
	protected UUID ownerUUID = null;
	protected Entity target = null;
	protected BlockPos tilePos;
	protected TileTurret tile = null;

	protected NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);

	public EntityTurret(World world) {
		super(world);
		setSize(0.5f, 0.5f);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(TEAM, "");
		dataManager.register(PLACEMENT, EnumFacing.UP);
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
		dataManager.set(PLACEMENT, facing);
		if (nbt.hasKey("inventory")) ItemStackHelper.loadAllItems(nbt.getCompoundTag("inventory"), inventory);
		if (nbt.hasKey("health")) setHealth(nbt.getFloat("health"));
	}

	public NBTTagCompound saveToTile() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("inventory", ItemStackHelper.saveAllItems(new NBTTagCompound(), inventory));
		nbt.setFloat("health", getHealth());
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
		if (tile == null &! world.isRemote) {
			TileEntity tile = world.getTileEntity(tilePos);
			if (tile instanceof TileTurret) {
				this.tile = (TileTurret) tile;
				((TileTurret) tile).setEntity(this);
			}
		}
		if (ticksExisted%5 == 0) {
			if (owner == null || owner.isDead |! owner.isAddedToWorld()) {
				if (!world.isRemote) owner = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(ownerUUID);
			}
			if (owner != null) {
				if (owner.getTeam() != null) {
					if(!owner.getTeam().isSameTeam(getTeam())) {
						dataManager.set(TEAM, owner.getTeam().getName());
					}
				} else if (dataManager.get(TEAM) != "") {
					dataManager.set(TEAM,"");
				}
			}
		}
		if (hasTarget()) {
			EnumFacing placement = getPlacement();
			if (placement.getAxis() == Axis.X) {

			} else if (placement.getAxis() == Axis.Y) {

			} else if (placement.getAxis() == Axis.Z) {

			}
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

}
