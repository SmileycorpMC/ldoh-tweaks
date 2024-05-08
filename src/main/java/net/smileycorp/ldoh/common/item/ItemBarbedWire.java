package net.smileycorp.ldoh.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.block.LDOHBlocks;
import net.smileycorp.ldoh.common.util.EnumBarbedWireMat;

import java.util.List;

public class ItemBarbedWire extends ItemBlock implements IMetaItem {

    private final String TOOLTIP = "tooltip." + Constants.MODID + ".BarbedWire";

    public ItemBarbedWire() {
        super(LDOHBlocks.BARBED_WIRE);
        String name = "Barbed_Wire";
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        setHasSubtypes(true);
    }

    @Override
    public String byMeta(int meta) {
        return EnumBarbedWireMat.byMeta(meta).getName();
    }

    @Override
    public int getMaxMeta() {
        return EnumBarbedWireMat.values().length;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + EnumBarbedWireMat.byMeta(stack.getMetadata()).getUnlocalisedName();
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        EnumBarbedWireMat mat = EnumBarbedWireMat.byMeta(stack.getMetadata());
        tooltip.add(I18n.translateToLocal(TOOLTIP));
        tooltip.add(I18n.translateToLocal(TOOLTIP + "." + mat.getUnlocalisedName()));
        if (!stack.hasTagCompound()) return;
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt.hasKey("durability")) tooltip.add(I18n.translateToLocal(TOOLTIP + ".durability")
                + " " + nbt.getInteger("durability") + "/" + mat.getDurability());
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return EnumBarbedWireMat.byMeta(stack.getMetadata()).getEnchantability();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.type.canEnchantItem(Items.IRON_SWORD);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getTagCompound() != null && stack.getTagCompound().hasKey("durability");
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (!showDurabilityBar(stack)) return 0;
        EnumBarbedWireMat mat = EnumBarbedWireMat.byMeta(stack.getMetadata());
        NBTTagCompound nbt = stack.getTagCompound();
        return 1 - ((double) nbt.getInteger("durability") / (double) mat.getDurability());
    }


}
