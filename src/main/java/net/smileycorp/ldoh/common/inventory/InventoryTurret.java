package net.smileycorp.ldoh.common.inventory;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.smileycorp.ldoh.common.ModDefinitions;

public class InventoryTurret extends InventoryBasic {

	public InventoryTurret() {
		super(ModDefinitions.getName("entity.turret"), false, 6);
	}

	public NBTTagCompound writeToNBT() {
		return ItemStackHelper.saveAllItems(new NBTTagCompound(), inventoryContents);
	}

	public void readFromNBT(NBTTagCompound nbt) {
		ItemStackHelper.loadAllItems(nbt, inventoryContents);
	}

}
