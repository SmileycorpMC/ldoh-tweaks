package net.smileycorp.ldoh.common.item;

import net.minecraft.item.ItemFood;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.LDOHTweaks;

public class ItemFoodLDOH extends ItemFood {

    public ItemFoodLDOH(String name, int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        setCreativeTab(LDOHTweaks.CREATIVE_TAB);
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
    }
}
