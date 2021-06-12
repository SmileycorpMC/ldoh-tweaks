package net.smileycorp.hundreddayz.common.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.hundreddayz.common.ModContent;
import net.smileycorp.hundreddayz.common.ModDefinitions;
import net.smileycorp.hundreddayz.common.block.EnumBarbedWireMat;

public class ItemBarbedWire extends ItemBlock implements IMetaItem {
	
	public ItemBarbedWire() {
		super(ModContent.BARBED_WIRE);
		String name = "Barbed_Wire";
		setUnlocalizedName(ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
	}
	
	@Override
	public String byMeta(int meta) {
		return EnumBarbedWireMat.byMeta(meta%3).getName();
	}
	
	@Override
	public int getMaxMeta() {
		return EnumBarbedWireMat.values().length;
	}
    
    @Override
	public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + EnumBarbedWireMat.byMeta(stack.getMetadata()%3).getUnlocalisedName();
    }
    
   /* @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return stack.getItemDamage() / (double)EnumBarbedWireMat.byMeta(stack.getMetadata()%3).getDurability();
    }
    
    @Override
    public int getMaxDamage(ItemStack stack) {
        return EnumBarbedWireMat.DIAMOND.getDurability();
    }*/
}
