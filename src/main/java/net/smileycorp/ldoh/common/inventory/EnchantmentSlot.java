package net.smileycorp.ldoh.common.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.smileycorp.ldoh.common.block.LDOHBlocks;

public class EnchantmentSlot extends Slot {
    
    public EnchantmentSlot(IInventory inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack) {
        return stack.getItem() == Item.getItemFromBlock(LDOHBlocks.BARBED_WIRE) ? 64 : getSlotStackLimit();
    }
    
}
