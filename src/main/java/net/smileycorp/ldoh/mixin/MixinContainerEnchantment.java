package net.smileycorp.ldoh.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.smileycorp.ldoh.common.block.LDOHBlocks;
import net.smileycorp.ldoh.common.inventory.EnchantmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ContainerEnchantment.class)
public abstract class MixinContainerEnchantment extends Container {
    
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/ContainerEnchantment;addSlotToContainer(Lnet/minecraft/inventory/Slot;)Lnet/minecraft/inventory/Slot;"), method = "<init>(Lnet/minecraft/entity/player/InventoryPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V")
    public Slot init$addSlotToContainer(ContainerEnchantment instance, Slot slot) {
        if (inventorySlots.isEmpty()) slot = new EnchantmentSlot(instance.tableInventory, 0, slot.xPos,  slot.yPos);
        return addSlotToContainer(slot);
    }
    
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;onEnchant(Lnet/minecraft/item/ItemStack;I)V"), method = "enchantItem")
    public void enchantItem$onEnchant(EntityPlayer instance, ItemStack stack, int cost) {
        if (stack.getItem() == Item.getItemFromBlock(LDOHBlocks.BARBED_WIRE) && stack.getCount() > 1)
            cost = cost * Math.min((int) (1 + (0.05f) * (float) stack.getCount()), 10);
        instance.onEnchant(stack, cost);
    }
    
}
