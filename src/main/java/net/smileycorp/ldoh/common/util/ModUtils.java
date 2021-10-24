package net.smileycorp.ldoh.common.util;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.smileycorp.ldoh.common.DayTimeSpeedModifier;
import net.smileycorp.ldoh.common.ModContent;
import net.tangotek.tektopia.ItemTagType;
import net.tangotek.tektopia.ModItems;
import net.tangotek.tektopia.Village;
import net.tangotek.tektopia.entities.EntityArchitect;
import net.tangotek.tektopia.entities.EntityTradesman;
import net.tangotek.tektopia.items.ItemProfessionToken;
import net.tangotek.tektopia.items.ItemStructureToken;
import rafradek.TF2weapons.item.ItemWeapon;
import biomesoplenty.api.biome.BOPBiomes;

import com.legacy.wasteland.world.WastelandWorld;

public class ModUtils {
	
	public static final AttributeModifier WASTELAND_MODIFIER = new AttributeModifier(UUID.fromString("22f4fa64-de73-4b45-9bb2-aae297639594"), "wasteland", 0.5, 2);
	
	//removes unnececary nbt from tf2 weapons to prevent a crash
	public static ItemStack cleanTFWeapon(ItemStack stack) {
		if (stack.getItem() instanceof ItemWeapon) {
			NBTTagCompound nbt = stack.getTagCompound();
			NBTTagCompound newNbt = new NBTTagCompound();
			if (nbt.hasKey("Type")) newNbt.setString("Type", nbt.getString("Type"));
			if (nbt.hasKey("Attributes")) newNbt.setTag("Attributes", nbt.getCompoundTag("Attributes"));
			stack = new ItemStack(stack.getItem(), 1, stack.getMetadata());
			stack.setTagCompound(newNbt);
		}
		return stack;
	}

	//sets speed modifiers
	public static void setEntitySpeed(EntityMob entity) {
		World world = entity.world;
		Biome biome = world.getBiome(entity.getPosition());
		IAttributeInstance speed = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		if (biome == BOPBiomes.wasteland.get()) {
			speed.applyModifier(WASTELAND_MODIFIER);
		}
		if (world.getWorldTime()%24000 < 12000) speed.applyModifier(new DayTimeSpeedModifier(world));
	}
	
	//checks if a 64/64 area around the position consists of only regular wasteland
	public static boolean isOnlyWasteland(World world, int x, int z) {
		for (Biome biome : world.getBiomeProvider().getBiomes(null, x-32, z-32, 64, 64, false)) if (biome!= WastelandWorld.apocalypse) return false;
		return true;
	}
	
	//builds trades for tektopia architect and tradesman
	public static boolean buildTrades(EntityLivingBase entity) {
		if (entity instanceof EntityArchitect) {
			MerchantRecipeList trades = ((IMerchant) entity).getRecipes((EntityPlayer)null);
			if (trades != null) {
				trades.clear();
				Village village = ((EntityArchitect) entity).getVillage();
				for (ItemStructureToken token : ModItems.structureTokens) {
					if (token.getCost(village) > 0) {
						ItemStack tokenItem = ModItems.createTaggedItem(token, ItemTagType.VILLAGER);
						ModItems.bindItemToVillage(tokenItem, village);
						trades.add(createMerchantRecipe(tokenItem, token.getCost(village)));
					} 
				}
				return true;
			}
		} else if (entity instanceof EntityTradesman) {
			MerchantRecipeList trades = ((IMerchant) entity).getRecipes((EntityPlayer)null);
			if (trades != null) {
				trades.clear();
				Village village = ((EntityTradesman) entity).getVillage();
				for (ItemProfessionToken token : ModItems.professionTokens.values()) {
					if (token == ModItems.itemGuard || token == ModItems.itemMiner || token == ModItems.itemCleric 
							|| token == ModItems.itemDruid || token == ModItems.itemEnchanter) continue;
					if (token.getCost(village) > 0) {
						ItemStack stack = ModItems.createTaggedItem(token, ItemTagType.VILLAGER);
						ModItems.bindItemToVillage(stack, village);
						trades.add(createMerchantRecipe(stack, token.getCost(village)));
					} 
				}
				for (int i = 0; i < EnumTFClass.values().length; i++) {
					ItemStack stack = ModItems.makeTaggedItem(new ItemStack(ModContent.TF_PROF_TOKEN, 1, i), ItemTagType.VILLAGER);
					ModItems.bindItemToVillage(stack, village);
					trades.add(createMerchantRecipe(stack, getCost(village, 6)));
				}
				return true;
			}
		}
		return false;
	}
	
	//creates villager trade for item, for use with tektopia villagers
	public static MerchantRecipe createMerchantRecipe(ItemStack item, int cost) {
		if (cost <= 64)
			return new MerchantRecipe(new ItemStack(Items.EMERALD, cost), ItemStack.EMPTY, item, 0, 99999); 
		if (cost % 9 == 0) {
			return new MerchantRecipe(new ItemStack(Item.getItemFromBlock(Blocks.EMERALD_BLOCK), cost / 9), ItemStack.EMPTY, item, 0, 99999);
		}
		return new MerchantRecipe(new ItemStack(Item.getItemFromBlock(Blocks.EMERALD_BLOCK), cost / 9), new ItemStack(Items.EMERALD, cost % 9), item, 0, 99999);
	 }
	
	//gets the cost of an item for a particular tektopia village
	public  static int getCost(Village village, int baseCost) {
		float mult = Math.min((village.getTownData().getProfessionSales() / 5) * 0.2F, 10.0F);
		return (int)(baseCost * (1.0F + mult));
	}
	
	
}
