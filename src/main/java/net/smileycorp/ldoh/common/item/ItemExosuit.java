package net.smileycorp.ldoh.common.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;
import net.smileycorp.ldoh.common.Constants;

import javax.annotation.Nonnull;

public class ItemExosuit extends ItemArmor {

    private static ArmorMaterial MATERIAL = EnumHelper.addArmorMaterial("exo", Constants.locName("textures/armor/exo.png"),
            1000, new int[] {3, 5, 5, 3}, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);

    public ItemExosuit(String name, EntityEquipmentSlot slot) {
        super(MATERIAL, 0, slot);
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0xC00000;
    }

    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        return;
    }

}
