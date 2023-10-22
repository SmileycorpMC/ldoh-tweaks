package net.smileycorp.ldoh.common.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.util.ModUtils;

public class ItemBlockMeta<T extends Block & IBlockProperties> extends ItemBlock implements IMetaItem {

	protected final String name;
	protected final IBlockProperties props;

	public ItemBlockMeta(T block) {
		super(block);
		setCreativeTab(LDOHTweaks.CREATIVE_TAB);
		name = block.getUnlocalizedName().substring(4);
		setUnlocalizedName(name);
		setRegistryName(block.getRegistryName());
		setHasSubtypes(true);
		this.props = block;
	}

	public String byMeta(int meta) {
		return ModUtils.getPropertyString(block.getStateFromMeta(meta).getProperties());
	}

	public int getMaxMeta() {
		return props.getMaxMeta();
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(isInCreativeTab(tab)) {
			for (int i = 0; i < getMaxMeta(); i++) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}

}
