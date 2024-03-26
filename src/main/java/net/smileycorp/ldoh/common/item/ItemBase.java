package net.smileycorp.ldoh.common.item;

import net.minecraft.item.Item;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.Constants;

public class ItemBase extends Item {

    public ItemBase(String name) {
        setCreativeTab(LDOHTweaks.CREATIVE_TAB);
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
    }

}
