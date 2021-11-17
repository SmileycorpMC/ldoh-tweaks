package net.smileycorp.ldoh.common.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.LDOHTweaks;

public class ItemBlockTooltip extends ItemBlock {
	
	private final String tooltipName;
	private final int lines;
	
	public ItemBlockTooltip(Block block) {
		this(block, 1);
	}
	
	public ItemBlockTooltip(Block block, int lines) {
		super(block);
		setCreativeTab(LDOHTweaks.CREATIVE_TAB);
		String name = block.getUnlocalizedName().substring(4);
		setUnlocalizedName(name);
		setRegistryName(block.getRegistryName());
		tooltipName = "tooltip" + name;
		this.lines = lines;
	}
    
    @Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
	    if(lines==1) tooltip.add(I18n.translateToLocal(tooltipName));
	    else for (int i = 0; i < lines; i++) tooltip.add(I18n.translateToLocal(tooltipName + i));
	}
}
