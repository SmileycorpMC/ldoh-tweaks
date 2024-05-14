package net.smileycorp.ldoh.mixin;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeHooks;
import net.smileycorp.ldoh.common.block.LDOHBlocks;
import net.smileycorp.ldoh.common.item.LDOHItems;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ContainerRepair.class)
public abstract class MixinContainerRepair extends Container {
    
    @Shadow @Final private IInventory inputSlots;
    
    @Shadow @Final private IInventory outputSlot;
    
    @Shadow private String repairedItemName;
    
    @Shadow @Final private EntityPlayer player;
    
    @Shadow public int maximumCost;
    
    @Inject(at = @At("HEAD"), method = "updateRepairOutput", cancellable = true)
    public void updateRepairOutput(CallbackInfo callback) {
        ItemStack stack1 = inputSlots.getStackInSlot(0);
        ItemStack stack2 = inputSlots.getStackInSlot(0);
        if (stack1.getItem() != Item.getItemFromBlock(LDOHBlocks.BARBED_WIRE)) return;
        callback.cancel();
        int j = stack1.getRepairCost() + (stack2.isEmpty() ? 0 : stack2.getRepairCost());
        if (!ForgeHooks.onAnvilChange((ContainerRepair) (Object)this, stack1, stack2, outputSlot, repairedItemName, j)) return;
        if (!(stack2.getItem() == Items.ENCHANTED_BOOK &! ItemEnchantedBook.getEnchantments(stack2).hasNoTags() ||
                (stack2.getItem() == Item.getItemFromBlock(LDOHBlocks.BARBED_WIRE) && stack2.getMetadata() == stack1.getMetadata()))) {
            outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
            return;
        }
        ItemStack output = stack1.copy();
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack1);
        Map<Enchantment, Integer> map2 = EnchantmentHelper.getEnchantments(stack2);
        int i = 0;
        int k = 0;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag = stack2.getItem() == Items.ENCHANTED_BOOK && !ItemEnchantedBook.getEnchantments(stack2).hasNoTags();
        for (Enchantment ench : map.keySet()) {
            if (ench == null) continue;
            int i2 = map.containsKey(ench) ? map.get(ench) : 0;
            int j2 = map2.get(ench);
            j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
            boolean flag1 = ench.canApply(stack1);
            for (Enchantment enchantment : map.keySet()) {
                if (enchantment != ench && !ench.isCompatibleWith(enchantment)) {
                    flag1 = false;
                    i++;
                }
            } if (!flag1) flag3 = true;
            else {
                flag2 = true;
                if (j2 > ench.getMaxLevel()) j2 = ench.getMaxLevel();
                map.put(ench, Integer.valueOf(j2));
                int k3 = 0;
                switch (ench.getRarity()) {
                    case COMMON:
                        k3 = 1;
                        break;
                    case UNCOMMON:
                        k3 = 2;
                        break;
                    case RARE:
                        k3 = 4;
                        break;
                    case VERY_RARE:
                        k3 = 8;
                }
                if (flag) k3 = Math.max(1, k3 / 2);
                i += k3 * j2;
                if (stack1.getCount() > 1) i *= (int) (1 + (0.05f) * (float) stack1.getCount());
            }
            if (flag3 && !flag2) {
                outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
                maximumCost = 0;
                return;
            }
        }
        if (StringUtils.isBlank(repairedItemName))
        {
            if (stack1.hasDisplayName()) {
                k = 1;
                i += k;
                output.clearCustomName();
            }
        }
        else if (!repairedItemName.equals(stack1.getDisplayName())) {
            k = 1;
            i += k;
            output.setStackDisplayName(repairedItemName);
        }
        if (flag && !output.getItem().isBookEnchantable(output, stack2)) output = ItemStack.EMPTY;
        maximumCost = j + i;
        if (i <= 0) output = ItemStack.EMPTY;
        if (k == i && k > 0 && maximumCost >= 40) maximumCost = 39;
        if (maximumCost >= 40 && !player.capabilities.isCreativeMode) output = ItemStack.EMPTY;
        if (!output.isEmpty()) {
            int k2 = output.getRepairCost();
            if (!stack2.isEmpty() && k2 < stack2.getRepairCost()) k2 = stack2.getRepairCost();
            if (k != i || k == 0) k2 = k2 * 2 + 1;
            output.setRepairCost(k2);
            EnchantmentHelper.setEnchantments(map, output);
        }
        outputSlot.setInventorySlotContents(0, output);
        detectAndSendChanges();
    }
    
}
