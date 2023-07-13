package net.smileycorp.ldoh.client.colour;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.smileycorp.ldoh.common.ModMobEntry;
import net.smileycorp.ldoh.common.item.ItemSpawner;

public class ItemEggColour implements IItemColor {

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		int colour = tintIndex == 1 ? 0x00000 : 0xFB40F9;
		int meta = stack.getMetadata();
		if (meta<ItemSpawner.entries.size()) {
			//get colours from the spawn egg entry
			ModMobEntry entry = ItemSpawner.entries.get(meta);
			if (tintIndex == 0) {
				colour = entry.getBackgroundColour();
			} else if (tintIndex == 1) {
				colour = entry.getForegroundColour();
			}
		}
		return colour;
	}

}
