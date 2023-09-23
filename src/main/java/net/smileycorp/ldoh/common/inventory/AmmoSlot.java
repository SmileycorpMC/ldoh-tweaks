package net.smileycorp.ldoh.common.inventory;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class AmmoSlot extends Slot {

	public AmmoSlot(InventoryTurretAmmo inventory, int index, int xPosition, int yPosition) {
		super(inventory, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return ((InventoryTurretAmmo) this.inventory).isAmmo(stack, null);
	}

}
