package net.smileycorp.hundreddayz.client;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.smileycorp.hundreddayz.common.ModMobEntry;
import net.smileycorp.hundreddayz.common.item.ItemSpawner;

public class ItemEggColour implements IItemColor {

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		int colour = 0x00000;
		int meta = stack.getMetadata();
		if (meta>=ItemSpawner.entries.size());
		ModMobEntry entry = ItemSpawner.entries.get(meta);
		if (tintIndex == 0) {
			colour = entry.getBackgroundColour();
		} else if (tintIndex == 1) {
			colour = entry.getForegroundColour();
		}
		return colour;
	}

}
