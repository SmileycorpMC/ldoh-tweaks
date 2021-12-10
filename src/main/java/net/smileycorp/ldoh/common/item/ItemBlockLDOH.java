package net.smileycorp.ldoh.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.smileycorp.ldoh.common.LDOHTweaks;

public class ItemBlockLDOH extends ItemBlock {

	protected final String name;

	public ItemBlockLDOH(Block block) {
		super(block);
		setCreativeTab(LDOHTweaks.CREATIVE_TAB);
		name = block.getUnlocalizedName().substring(4);
		setUnlocalizedName(name);
		setRegistryName(block.getRegistryName());
	}

}
