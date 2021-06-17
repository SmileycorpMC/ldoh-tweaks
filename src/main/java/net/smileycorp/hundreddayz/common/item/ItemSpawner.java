package net.smileycorp.hundreddayz.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.hundreddayz.common.ModContent;
import net.smileycorp.hundreddayz.common.ModDefinitions;
import net.smileycorp.hundreddayz.common.ModMobEntry;
import net.smileycorp.hundreddayz.common.entity.EntityTFZombie;
import net.smileycorp.hundreddayz.common.entity.EnumTFClass;

public class ItemSpawner extends Item implements IMetaItem {
	
	String name = "MobSpawner";
	
	public static List<ModMobEntry> entries = new ArrayList<ModMobEntry>();
	
	public ItemSpawner() {
		setCreativeTab(ModContent.CREATIVE_TAB);
		setUnlocalizedName(ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
		setHasSubtypes(true);
	}
	
	@Override
	public String byMeta(int meta) {
		return "normal";
	}
	
	@Override
	public int getMaxMeta() {
		return entries.size();
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
	public String getItemStackDisplayName(ItemStack stack) {
    	int meta = stack.getMetadata();
    	if (meta >= getMaxMeta()) meta = 0;
        return super.getItemStackDisplayName(stack) + " " + entries.get(meta).getLocalisedName();
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    	ItemStack itemstack = player.getHeldItem(hand);
    	if (world.isRemote) return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
    	ItemStack stack = player.getHeldItem(hand);
    	int meta = stack.getMetadata();
    	if (meta >= getMaxMeta()) meta = 0;
    	RayTraceResult raytrace = this.rayTrace(world, player, true);
    	BlockPos pos = raytrace.getBlockPos().offset(raytrace.sideHit);
    	spawnEntity(world, meta, pos);
    	if (!player.capabilities.isCreativeMode) stack.shrink(1);
    	return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
    
    private EntityLiving spawnEntity(World world, int meta, BlockPos pos) {
    	EntityLiving entity = entries.get(meta).getEntityToSpawn(world, pos);
    	entity.onInitialSpawn(world.getDifficultyForLocation(pos), null);
    	entity.setPosition(pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f);
    	world.spawnEntity(entity);
    	return entity;
	}
    
    static {
    	entries.add(new ModMobEntry(EntityTFZombie.class, "entity.hundreddayz.TFZombie.name" , 0x0000E2, 0xEF0000));
    	for (EnumTFClass tfclass : EnumTFClass.values()) {
    		entries.add(new ModMobEntry(tfclass, 0x436C34, 0xEF0000, "RED"));
    		entries.add(new ModMobEntry(tfclass, 0x436C34, 0x0000E2, "BLU"));
    	}
    	//entries.add(new ModMobEntry(EntityZombieNurse.class, "entity.hundreddayz.NurseZombie.name" , 0x436C34, 0xB5ABB4));
    }

	public static ItemStack getEggFor(EntityLiving entity) {
		for (ModMobEntry entry : entries) {
			if (entry.doesMatch(entity)) {
				return new ItemStack(ModContent.SPAWNER, 1, entries.indexOf(entry));
			}
		}
		return null;
	}
    
}
