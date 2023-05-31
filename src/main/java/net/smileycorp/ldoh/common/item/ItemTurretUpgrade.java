package net.smileycorp.ldoh.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.util.TurretUpgrade;

import java.util.List;

public class ItemTurretUpgrade extends Item implements IMetaItem {

	String name = "Turret_Upgrade";

	public ItemTurretUpgrade() {
		setCreativeTab(LDOHTweaks.CREATIVE_TAB);
		setUnlocalizedName(ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	public static boolean isBlank(ItemStack stack) {
		return TurretUpgrade.isBlank(stack.getMetadata());
	}

	@Override
	public String byMeta(int meta) {
		return meta < getMaxMeta() ? TurretUpgrade.values()[meta].getName() : TurretUpgrade.BLANK.getName();
	}

	@Override
	public int getMaxMeta() {
		return TurretUpgrade.values().length;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(isInCreativeTab(tab)) {
			for (int i = 0; i < getMaxMeta(); i++) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return isBlank(stack) ? "item.hundreddayz.BlankTurretUpgrade" : super.getUnlocalizedName();
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		if (isBlank(stack)) return;
		final String key = new StringBuilder("tooltip.hundreddayz.TurretUpgrade.").append(byMeta(stack.getMetadata())).toString();
		tooltip.add(I18n.translateToLocal(key+".name"));
		tooltip.add(I18n.translateToLocal(key+".description"));
	}

}
