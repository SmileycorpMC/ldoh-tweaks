package net.smileycorp.ldoh.common.item;

import net.minecraft.item.Item;
import net.smileycorp.ldoh.common.ModContent;
import net.smileycorp.ldoh.common.ModDefinitions;

public class ItemBase extends Item {
	
	public ItemBase(String name) {
		setCreativeTab(ModContent.CREATIVE_TAB);
		setUnlocalizedName(ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
	}

}
