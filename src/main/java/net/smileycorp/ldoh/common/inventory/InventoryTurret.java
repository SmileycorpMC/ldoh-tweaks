package net.smileycorp.ldoh.common.inventory;

import com.mrcrayfish.guns.init.ModGuns;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.item.LDOHItems;

public class InventoryTurret extends InventoryBasic {

	public InventoryTurret() {
		super(ModDefinitions.getName("entity.turret"), false, 9);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return isAmmo(stack);
	}

	public int getAmmoSlot() {
		for(int i = inventoryContents.size()-1; i >= 0; i--) {
			if (isAmmo(inventoryContents.get(i))) return i;
		}
		return -1;
	}

	public boolean isAmmo(ItemStack stack) {
		Item item = stack.getItem();
		return item == ModGuns.BASIC_AMMO || item == LDOHItems.INCENDIARY_AMMO;
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

	public ItemStack getAmmo() {
		int slot = getAmmoSlot();
		return slot < 0 ? ItemStack.EMPTY : getStackInSlot(slot);
	}

	public NonNullList<ItemStack> getItems() {
		return inventoryContents;
	}

}
