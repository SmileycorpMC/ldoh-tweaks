package net.smileycorp.ldoh.common.item;

import javax.annotation.Nullable;

import net.minecraft.block.BlockDispenser;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.ldoh.client.entity.model.ModelItemHat;
import net.smileycorp.ldoh.common.ModContent;
import net.smileycorp.ldoh.common.ModDefinitions;

public class ItemHelmet extends ItemArmor {
	
	private final String name;
	
	public ItemHelmet(String name, int durability, int protection, float toughness) {
		super(EnumHelper.addArmorMaterial(name, name, durability, new int[]{0, 0, 0, protection}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, toughness), 0, EntityEquipmentSlot.HEAD);
		setCreativeTab(ModContent.CREATIVE_TAB);
		setUnlocalizedName(ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
		this.name = name;
	}
	
	@Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot, ModelBiped base) {
        return new ModelItemHat();
    }
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return ModDefinitions.modid + ":textures/model/"+name.toLowerCase()+".png";
    }
	
}
