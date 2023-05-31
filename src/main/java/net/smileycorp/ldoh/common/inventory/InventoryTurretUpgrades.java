package net.smileycorp.ldoh.common.inventory;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.mrcrayfish.guns.init.ModGuns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.item.ItemTurretUpgrade;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.util.ModUtils;
import net.smileycorp.ldoh.common.util.TurretUpgrade;

public class InventoryTurretUpgrades implements IInventory {

  //TODO: test fixes, organize project imports

	protected final EntityTurret turret;

	public InventoryTurretUpgrades(EntityTurret turret) {
		this.turret = turret;
	}

	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	public boolean isEmpty() {
		return !turret.hasUpgrades();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		int[] upgrades = ModUtils.posToArray(turret.getDataManager().get(EntityTurret.TURRET_UPGRADES));
		return TurretUpgrade.isBlank(upgrades[slot]) ? ItemStack.EMPTY : TurretUpgrade.get(upgrades[slot]).getItem();
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (amount <= 0) return ItemStack.EMPTY;
		int[] upgrades = ModUtils.posToArray(turret.getDataManager().get(EntityTurret.TURRET_UPGRADES));
		if (TurretUpgrade.isBlank(upgrades[slot])) return ItemStack.EMPTY;
   int id = upgrades[slot];
   upgrades[slot] = 0;
		turret.updateUpgrades(upgrades);
		return TurretUpgrade.get(id).getItem();
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		return decrStackSize(slot, 1);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if (!isItemValidForSlot(slot, stack)) return;
		int[] upgrades = ModUtils.posToArray(turret.getDataManager().get(EntityTurret.TURRET_UPGRADES));
		upgrades[slot] = stack.getMetadata();
		turret.updateUpgrades(upgrades);
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void markDirty() {}

	@Override
	public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer entityPlayer) {}

	@Override
	public void closeInventory(EntityPlayer entityPlayer) {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot < 3 && stack != null && stack.getItem() == LDOHItems.TURRET_UPGRADE &! ItemTurretUpgrade.isBlank(stack);
	}

	@Override
	public int getField(int i) {
		return 0;
	}

	@Override
	public void setField(int i, int i1) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		turret.updateUpgrades(new int[]{0, 0, 0});
	}

	@Override
	public String getName() {
		return turret.getName();
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return turret.getDisplayName();
	}
}
