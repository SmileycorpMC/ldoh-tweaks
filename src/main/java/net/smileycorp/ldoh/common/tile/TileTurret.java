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

public class TileTurret extends TileAbstractTurret<EntityTurret> implements IInventory {

	@Override
	public EntityTurret createNewEntity() {
		return new EntityTurret(world);
	}

	public NBTTagCompound getDropNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		if (entity!=null) nbt.setTag("entity", entity.saveToItem());
		return nbt;
	}

	@Override
	public String getName() {
		return "Sentry Turret";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}


}
