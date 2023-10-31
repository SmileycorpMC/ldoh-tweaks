package net.smileycorp.ldoh.common.item;

import com.mrcrayfish.guns.item.ItemAmmo;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;

import java.util.List;

public class ItemIncendiaryAmmo extends ItemAmmo {

    public ItemIncendiaryAmmo() {
        super(ModDefinitions.getResource("incendiary_ammo"));
        setCreativeTab(LDOHTweaks.CREATIVE_TAB);
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        for (int i = 0; i < 2; i++) tooltip.add(I18n.translateToLocal("tooltip.ldoh.incendiary_ammo" + i));
    }

}
