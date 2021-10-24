package net.smileycorp.ldoh.common.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemHat extends ItemBase {
	
	public ItemHat(String name) {
		super(name);
		setMaxStackSize(1);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
	}
	
	@Override
	public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot slot, Entity entity) {
        return slot == EntityEquipmentSlot.HEAD;
    }
	
	//copied from ItemArmor because it's the only common code we use
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        EntityEquipmentSlot entityequipmentslot = EntityEquipmentSlot.HEAD;
        ItemStack itemstack1 = player.getItemStackFromSlot(entityequipmentslot);

        if (itemstack1.isEmpty()) {
            player.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            itemstack.setCount(0);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
    }
	
}
