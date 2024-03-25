package net.smileycorp.ldoh.common.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.ExperienceBucketFluidHandler;
import net.smileycorp.ldoh.common.fluid.LDOHFluids;
import net.smileycorp.unexperienced.ConfigHandler;
import net.smileycorp.unexperienced.Unexperienced;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBucketOfExperience extends UniversalBucket {
    
    public ItemBucketOfExperience() {
        String name = "Experience_Bucket";
        setCreativeTab(LDOHTweaks.CREATIVE_TAB);
        setUnlocalizedName(ModDefinitions.getName(name));
        setRegistryName(ModDefinitions.getResource(name));
    }
    
    @Override
    public void getSubItems(@Nullable CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (!isInCreativeTab(tab)) return;
        subItems.add(new ItemStack(this));
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ActionResult<ItemStack> result = super.onItemRightClick(world, player, hand);
        if (result.getType() == EnumActionResult.PASS) {
            player.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }
        return result;
    }
    
    
    @Nullable
    public FluidStack getFluid(@Nonnull ItemStack container) {
        return new FluidStack(LDOHFluids.EXPERIENCE, Fluid.BUCKET_VOLUME);
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            Unexperienced.addExperience(player, ConfigHandler.bottleExperience * 8);
            if (player instanceof EntityPlayerMP) CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)player, stack);
            if (stack.getCount()>1 &! player.isCreative()) player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
        }
        stack.shrink(1);
        return new ItemStack(Items.BUCKET);
    }
    
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(I18n.translateToLocal("tooltip.ldoh.ExpBottle"));
    }
    
    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, NBTTagCompound nbt) {
        return new ExperienceBucketFluidHandler(stack);
    }
    
}
