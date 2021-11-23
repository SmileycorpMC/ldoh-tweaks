package net.smileycorp.ldoh.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.block.LDOHBlocks;
import net.smileycorp.ldoh.common.util.EnumBarbedWireMat;

public class ItemBarbedWire extends ItemBlock implements IMetaItem {
	
	private final String TOOLTIP = "tooltip." + ModDefinitions.MODID + ".BarbedWire";
	
	public ItemBarbedWire() {
		super(LDOHBlocks.BARBED_WIRE);
		String name = "Barbed_Wire";
		setUnlocalizedName(ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
	}
	
	@Override
	public String byMeta(int meta) {
		return EnumBarbedWireMat.byMeta(meta).getName();
	}
	
	@Override
	public int getMaxMeta() {
		return EnumBarbedWireMat.values().length;
	}
    
    @Override
	public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + EnumBarbedWireMat.byMeta(stack.getMetadata()).getUnlocalisedName();
    }
    
    @Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
	    tooltip.add(I18n.translateToLocal(TOOLTIP));
	    tooltip.add(I18n.translateToLocal(TOOLTIP + "." + EnumBarbedWireMat.byMeta(stack.getMetadata()).getUnlocalisedName()));
	}
}
