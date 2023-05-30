package net.smileycorp.ldoh.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.entity.EntityTurret;

public class ContainerTurret extends Container {

	protected EntityTurret turret;
	protected World world;
	protected InventoryTurret inv;
	protected InventoryPlayer playerInv;

	public ContainerTurret(EntityTurret turret, EntityPlayer player) {
		this.turret = turret;
		world = turret.world;
		inv = turret.getInventory();
		playerInv = player.inventory;
		//turret inventory
		for (int i = 0; i< inv.getSizeInventory(); i++) {
			addSlotToContainer(new AmmoSlot(turret.getInventory(), i, 8 + i*18, 83));
		}
		//player inventory
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 113 + i * 18));
			}
		}
		//hotbar
		for (int k = 0; k < 9; ++k) {
			addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 171));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return player.isCreative() || turret.isSameTeam(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index < inv.getSizeInventory()) {
				if (!mergeItemStack(itemstack1, inv.getSizeInventory(), inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(itemstack1, 0, inv.getSizeInventory(), false)) {
				return ItemStack.EMPTY;
			}
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

}
