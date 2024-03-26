package net.smileycorp.ldoh.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.hordes.infection.HordesInfection;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.Constants;

import java.util.List;

public class ItemSyringe extends Item implements IMetaItem {

    String name = "Syringe";
    String[] variants = {"Empty", "Blood", "Cure", "Contaminated"};

    public ItemSyringe() {
        setCreativeTab(LDOHTweaks.CREATIVE_TAB);
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        setMaxStackSize(16);
        setHasSubtypes(true);
    }

    @Override
    public String byMeta(int meta) {
        return variants[meta].toLowerCase();
    }

    @Override
    public int getMaxMeta() {
        return variants.length;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) for (int i = 0; i < getMaxMeta(); i++) items.add(new ItemStack(this, 1, i));
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        EnumAction action = (stack.getMetadata() == 0) ? EnumAction.BOW : EnumAction.NONE;
        return action;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        EnumActionResult action = EnumActionResult.PASS;
        if (world.isRemote) action = (stack.getMetadata() == 0) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
        if (stack.getMetadata() == 0) {
            player.setActiveHand(hand);
            action = EnumActionResult.SUCCESS;
        }
        return action;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        EnumActionResult action = EnumActionResult.PASS;
        if (world.isRemote) action = (stack.getMetadata() == 0) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
        if (stack.getMetadata() == 0) {
            player.setActiveHand(hand);
            action = EnumActionResult.SUCCESS;
        }
        return new ActionResult<>(action, stack);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        World world = player.world;
        if (target.isPotionActive(HordesInfection.INFECTED) && stack.getMetadata() == 2) {
            if (!world.isRemote) {
                target.removePotionEffect(HordesInfection.INFECTED);
                target.heal(5);
                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(1);
                    ItemStack container = new ItemStack(this, 1, 3);
                    if (!player.addItemStackToInventory(container)) player.dropItem(container, false);
                }
            } else {
                for (int i = 1; i <= 8; i++) {
                    int a = Math.round(45 * i);
                    double rad = a * Math.PI / 180;
                    double x = target.posX + Math.cos(rad);
                    double y = target.posY + 1.5f;
                    double z = target.posZ + Math.sin(rad);
                    world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, x, y, z, 0, 0.5f, 0);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        return stack.getMetadata() == 2 ? new ItemStack(LDOHItems.SYRINGE, 1, 3) : super.getContainerItem(stack);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        int duration = (stack.getMetadata() == 0) ? 20 : 0;
        return duration;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        if (stack.getMetadata() == 0 && entity instanceof EntityPlayer) {
            stack.shrink(1);
            ItemStack filled = new ItemStack(this, 1, 1);
            if (!((EntityPlayer)entity).addItemStackToInventory(filled)) ((EntityPlayer)entity).dropItem(filled, false);
            entity.attackEntityFrom(DamageSource.CACTUS, 0.5f);
        }
        return stack;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "." + variants[stack.getMetadata()];
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        if (stack.getMetadata() == 0) tooltip.add(I18n.translateToLocal("tooltip.ldoh.Syringe"));
        else if (stack.getMetadata() == 2) tooltip.add(I18n.translateToLocal("tooltip.ldoh.AntibodySerum"));
    }
}
