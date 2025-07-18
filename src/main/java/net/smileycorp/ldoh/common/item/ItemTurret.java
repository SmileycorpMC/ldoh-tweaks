package net.smileycorp.ldoh.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.block.LDOHBlocks;

import java.util.List;

@SuppressWarnings("deprecation")
public class ItemTurret extends ItemBlockTooltip {

    public ItemTurret() {
        super(LDOHBlocks.TURRET, 3);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        items.add(new ItemStack(this));
        ItemStack enemy = new ItemStack(this);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("isEnemy", true);
        enemy.setTagCompound(nbt);
        items.add(enemy);
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null) {
            if (nbt.hasKey("isEnemy")) {
                if (nbt.getBoolean("isEnemy")) {
                    tooltip.add(I18n.translateToLocal("tooltip.ldoh.EnemyTurret0"));
                    tooltip.add(I18n.translateToLocal("tooltip.ldoh.EnemyTurret1"));
                    return;
                }
            }
        }
        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null) {
            if (nbt.hasKey("isEnemy")) {
                if (nbt.getBoolean("isEnemy")) {
                    return I18n.translateToLocal("tile.ldoh.EnemyTurret.name");
                }
            }
        }
        return super.getItemStackDisplayName(stack);
    }

}
