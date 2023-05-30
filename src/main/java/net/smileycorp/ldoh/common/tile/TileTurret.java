package net.smileycorp.ldoh.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.util.TurretUpgrade;

public class TileTurret extends TileEntity implements IInventory {

	protected EntityTurret entity = null;
	protected NBTTagCompound entity_nbt = null;

	public void spawnEntity(EntityPlayer owner, EnumFacing facing, NBTTagCompound nbt) {
		entity = new EntityTurret(world);
		Vec3d pos = new Vec3d(this.pos.getX() + 0.5, this.pos.getY() + 0.25, this.pos.getZ() + 0.5);
		entity.setPosition(pos.x, pos.y, pos.z);
		NBTTagCompound entityData = nbt.hasKey("entity") ? (NBTTagCompound) nbt.getCompoundTag(("entity")) : new NBTTagCompound();
		if (nbt.hasKey("isEnemy")) entityData.setBoolean("isEnemy", nbt.getBoolean("isEnemy"));
		entity.readFromTile(owner, this, entityData, facing);
		world.spawnEntity(entity);
	}

	public NBTTagCompound getDropNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (entity!=null) nbt.setTag("entity", entity.saveToItem());
		return nbt;
	}

	public void breakBlock() {
		if (entity!=null)entity.setDead();
	}

	public EntityPlayer getOwner() {
		return entity == null ? null : entity.getOwner();
	}

	public EntityTurret getEntity() {
		return entity;
	}

	public void setEntity(EntityTurret entity) {
		this.entity = entity;
	}

	@Override
	public String getName() {
		return "Sentry Turret";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public void clear() {
		if (isHopping()) entity.getInventory().clear();
	}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		return  isHopping() ? entity.getInventory().decrStackSize(slot, count) : ItemStack.EMPTY;
	}

	@Override
	public int getField(int field) {
		return isHopping() ? entity.getInventory().getField(field) : 0;
	}

	@Override
	public int getFieldCount() {
		return isHopping() ? entity.getInventory().getFieldCount() : 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return isHopping() ? entity.getInventory().getInventoryStackLimit() : 0;
	}

	@Override
	public int getSizeInventory() {
		return isHopping() ? entity.getInventory().getSizeInventory() : 0;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return isHopping() ? entity.getInventory().getStackInSlot(slot) : ItemStack.EMPTY;
	}

	@Override
	public boolean isEmpty() {
		return isHopping() ? entity.getInventory().isEmpty() : true;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return isHopping() ? entity.getInventory().isItemValidForSlot(slot, stack) : false;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return entity == null ? false : entity.getInventory().isUsableByPlayer(player);
	}

	@Override
	public void openInventory(EntityPlayer player) {
		if (entity!=null) entity.getInventory().openInventory(player);
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		return isHopping() ?  entity.getInventory().removeStackFromSlot(slot) : ItemStack.EMPTY;
	}

	@Override
	public void setField(int field, int value) {
		if (isHopping()) entity.getInventory().setField(field, value);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if (isHopping()) entity.getInventory().setInventorySlotContents(slot, stack);
	}

	public boolean isHopping() {
		return (entity != null && entity.hasUpgrade(TurretUpgrade.HOPPING));
	}

}
