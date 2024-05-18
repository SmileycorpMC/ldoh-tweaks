package net.smileycorp.ldoh.common.inventory;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.mrcrayfish.guns.init.ModGuns;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.item.LDOHItems;

public class InventoryTurretAmmo extends InventoryBasic {

    public InventoryTurretAmmo() {
        super(Constants.name("entity.turret"), false, 9);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return isAmmo(stack, null);
    }

    public int getAmmoSlot(EntityLivingBase target) {
        int slot = -1;
        for (int i = inventoryContents.size() - 1; i >= 0; i--) {
            if (isAmmo(inventoryContents.get(i), target)) return i;
            else if (slot < 0 && isAmmo(inventoryContents.get(i), null)) slot = i;
        }
        return slot;
    }

    public boolean isAmmo(ItemStack stack, EntityLivingBase target) {
        Item item = stack.getItem();
        if (target == null) return item == ModGuns.BASIC_AMMO || item == LDOHItems.INCENDIARY_AMMO || item == LDOHItems.AP_AMMO;
        return item == (target instanceof EntityParasiteBase ? LDOHItems.INCENDIARY_AMMO : target.getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue() > 1 ?
                LDOHItems.AP_AMMO : ModGuns.BASIC_AMMO);
    }

    public boolean hasAmmo() {
        return getAmmoSlot(null) > -1;
    }

    public NBTTagCompound writeToNBT() {
        return ItemStackHelper.saveAllItems(new NBTTagCompound(), inventoryContents);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        ItemStackHelper.loadAllItems(nbt, inventoryContents);
    }

    public ItemStack getAmmo(EntityLivingBase target) {
        int slot = getAmmoSlot(target);
        return slot < 0 ? ItemStack.EMPTY : getStackInSlot(slot);
    }

    public NonNullList<ItemStack> getItems() {
        return inventoryContents;
    }

}
