package net.smileycorp.ldoh.common.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;

import com.mrcrayfish.guns.item.ItemAmmo;

public class ItemIncendiaryAmmo extends ItemAmmo {

	public ItemIncendiaryAmmo() {
		super(ModDefinitions.getResource("incendiary_ammo"));
		setCreativeTab(LDOHTweaks.CREATIVE_TAB);
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		for(int i = 0; i < 2; i++) tooltip.add(I18n.translateToLocal("tooltip.hundreddayz.incendiary_ammo" + i));
	}

}
