package net.smileycorp.hundreddayz.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.hordes.infection.HordesInfection;
import net.smileycorp.hundreddayz.common.ModContent;
import net.smileycorp.hundreddayz.common.ModDefinitions;

public class ItemSyringe extends Item implements IMetaItem {
	
	String name = "Syringe";
	String[] variants = {"Empty", "Blood", "Cure", "Contaminated"};
	
	public ItemSyringe() {
		setCreativeTab(ModContent.CREATIVE_TAB);
		setUnlocalizedName(ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
		setMaxStackSize(1);
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
		if(isInCreativeTab(tab)) {
			for (int i = 0; i < getMaxMeta(); i++) {
				items.add(new ItemStack(this, 1, i));
			}
		}
    }
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		EnumAction action = (stack.getMetadata()==0) ? EnumAction.BOW : EnumAction.NONE;
		return action;
    }
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		EnumActionResult action = EnumActionResult.PASS;
		if(world.isRemote) action = (stack.getMetadata()==0) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
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
		if(world.isRemote) action = (stack.getMetadata()==0) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
		if (stack.getMetadata() == 0) {
			player.setActiveHand(hand);
			action = EnumActionResult.SUCCESS;
		}
        return new ActionResult<ItemStack>(action, stack);
    }
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		World world = player.world;
		if (target.isPotionActive(HordesInfection.INFECTED) || stack.getMetadata() == 2) {
			if (!world.isRemote) {
				target.removePotionEffect(HordesInfection.INFECTED);
				target.heal(5);
				if (!player.capabilities.isCreativeMode) {
					stack.setItemDamage(3);
				}
			} else {
				for (int i = 1; i<=8; i++) {
					int a = Math.round(45*i);
					double rad = a * Math.PI/180;
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
	public int getMaxItemUseDuration(ItemStack stack) {
		int duration = (stack.getMetadata()==0) ? 20 : 0;
		return duration;
    }

    @Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
		if (stack.getMetadata() == 0 && entity instanceof EntityPlayer) {
			stack.setItemDamage(1);
			entity.attackEntityFrom(DamageSource.CACTUS, 0.5f);
    	}
		return stack;
    }
    
    @Override
	public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + variants[stack.getMetadata()];
    }
}
