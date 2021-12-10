package net.smileycorp.ldoh.common.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemBlockTooltip extends ItemBlockLDOH {

	private final String tooltipName;
	private final int lines;

	public ItemBlockTooltip(Block block) {
		this(block, 1);
	}

	public ItemBlockTooltip(Block block, int lines) {
		super(block);
		tooltipName = "tooltip" + name;
		this.lines = lines;
	}

    @Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
	    if(lines==1) tooltip.add(I18n.translateToLocal(tooltipName));
	    else for (int i = 0; i < lines; i++) tooltip.add(I18n.translateToLocal(tooltipName + i));
	}
}
