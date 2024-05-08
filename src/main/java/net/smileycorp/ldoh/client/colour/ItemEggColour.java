package net.smileycorp.ldoh.client.colour;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.smileycorp.ldoh.common.ModMobEntry;
import net.smileycorp.ldoh.common.item.ItemSpawner;

public class ItemEggColour implements IItemColor {

    @Override
    //follow rough vanilla behaviour for colouring our spawn eggs
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        int meta = stack.getMetadata();
        if (meta < ItemSpawner.entries.size()) {
            //get colours from the spawn egg entry
            ModMobEntry entry = ItemSpawner.entries.get(meta);
            return tintIndex == 0 ? entry.getBackgroundColour() : entry.getForegroundColour();
        }
        return tintIndex == 1 ? 0x00000 : 0xFB40F9;
    }

}
