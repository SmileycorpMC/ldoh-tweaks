package net.smileycorp.ldoh.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.smileycorp.ldoh.common.entity.EntityTurret;

public class TileTurret extends TileEntity {

	protected EntityTurret entity = null;

	public void spawnEntity(EntityPlayer owner, EnumFacing facing, NBTTagCompound nbt) {
		entity = new EntityTurret(world);
		Vec3d pos = new Vec3d(this.pos.getX() + 0.5, this.pos.getY() + 0.25, this.pos.getZ() + 0.5);
		entity.setPosition(pos.x, pos.y, pos.z);
		NBTTagCompound entityData = nbt.hasKey("entity") ? (NBTTagCompound) nbt.getCompoundTag(("entity")) : new NBTTagCompound();
		entity.readFromTile(owner, this, entityData, facing);
		world.spawnEntity(entity);
	}

	public NBTTagCompound getDropNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (entity!=null) nbt.setTag("entity", entity.saveToTile());
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

}
