package net.smileycorp.ldoh.common.inventory;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.smileycorp.ldoh.common.ModDefinitions;

public class InventoryTurret extends InventoryBasic {

	public InventoryTurret() {
		super(ModDefinitions.getName("entity.turret"), false, 6);
	}

	public int getAmmoSlot() {
		for(int i = inventoryContents.size()-1; i > 0; i--) {
			if (isAmmo(inventoryContents.get(i))) return i;
		}
		return -1;
	}

	private boolean isAmmo(ItemStack stack) {
		Item item = stack.getItem();
		return false;
	}

	public boolean hasAmmo() {
		return getAmmoSlot() > -1;
	}

	public NBTTagCompound writeToNBT() {
		return ItemStackHelper.saveAllItems(new NBTTagCompound(), inventoryContents);
	}

	public void readFromNBT(NBTTagCompound nbt) {
		ItemStackHelper.loadAllItems(nbt, inventoryContents);
	}

}
