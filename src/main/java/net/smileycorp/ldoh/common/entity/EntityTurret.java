package net.smileycorp.ldoh.common.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.entity.ai.AITurretTarget;
import net.smileycorp.ldoh.common.inventory.InventoryTurret;
import net.smileycorp.ldoh.common.tile.TileTurret;

public class EntityTurret extends EntityLiving {

	protected static final DataParameter<String> TEAM = EntityDataManager.<String>createKey(EntityTurret.class, DataSerializers.STRING);
	protected static final DataParameter<EnumFacing> FACING = EntityDataManager.<EnumFacing>createKey(EntityTurret.class, DataSerializers.FACING);

	protected Entity target = null;
	protected InventoryTurret inventory = new InventoryTurret();

	protected EntityPlayer owner = null;
	protected TileTurret tile = null;

	//read these values from the nbt and cache them to make sure it gets loaded properly
	protected UUID ownerUUID = null;
	protected BlockPos tilePos;

	public EntityTurret(World world) {
		super(world);
		setSize(0.5f, 0.5f);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(TEAM, "");
		dataManager.register(FACING, EnumFacing.UP);
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
		if (nbt.hasKey("inventory")) inventory.readFromNBT(nbt.getCompoundTag("inventory"));
		if (nbt.hasKey("owner")) ownerUUID = UUID.fromString(nbt.getString("owner"));
		if (nbt.hasKey("tilePos")) {
			NBTTagCompound pos =  nbt.getCompoundTag("tilePos");
			tilePos = new BlockPos(pos.getInteger("x"), pos.getInteger("y"), pos.getInteger("z"));
		}
		if (nbt.hasKey("facing")) {
			dataManager.set(FACING, EnumFacing.getFront(nbt.getInteger("facing")));
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setTag("inventory", inventory.writeToNBT());
		if (owner!=null) nbt.setString("owner", ownerUUID.toString());
		if (tilePos != null) {
			NBTTagCompound pos = new NBTTagCompound();
			pos.setInteger("x", tilePos.getX());
			pos.setInteger("y", tilePos.getY());
			pos.setInteger("z", tilePos.getZ());
			nbt.setTag("tilePos", pos);
		}
		nbt.setInteger("facing", dataManager.get(FACING).ordinal());
	}

	public void readFromTile(EntityPlayer owner, TileTurret tile, NBTTagCompound nbt, EnumFacing facing) {
		ownerUUID = owner.getUniqueID();
		this.owner = owner;
		if (owner.getTeam() != null) {
			dataManager.set(TEAM, owner.getTeam().getName());
		}
		tilePos = tile.getPos();
		dataManager.set(FACING, facing);
		if (nbt.hasKey("inventory")) inventory.readFromNBT(nbt.getCompoundTag("inventory"));
		if (nbt.hasKey("health")) setHealth(nbt.getFloat("health"));
	}

	public NBTTagCompound saveToTile() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("inventory", inventory.writeToNBT());
		nbt.setFloat("health", getHealth());
		return nbt;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public float getEyeHeight() {
		return 0.25f;
	}

	@Override
	protected void collideWithEntity(Entity entityIn){}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!world.isRemote && tile == null) {
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
			//data check owner and update team accordingly
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
			EnumFacing placement = getFacing();
			if (placement.getAxis() == Axis.X) {

			} else if (placement.getAxis() == Axis.Y) {

			} else if (placement.getAxis() == Axis.Z) {

			}
		}
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		if (tile != null &! world.isRemote) {
			BlockPos pos = tile.getPos();
			if ((getTeam() == null && player.getTeam() == null) ||
					getTeam().equals(player.getTeam())) {
				player.openGui(LDOHTweaks.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
				return EnumActionResult.SUCCESS;
			}
		}
        return super.applyPlayerInteraction(player, vec, hand);
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

	public EnumFacing getFacing() {
		return dataManager.get(FACING);
	}

	public InventoryTurret getInventory() {
		return inventory;
	}
}
