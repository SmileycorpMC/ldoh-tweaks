package net.smileycorp.ldoh.integration.tektopia.entities;

import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.tangotek.tektopia.ItemTagType;
import net.tangotek.tektopia.ModItems;
import net.tangotek.tektopia.entities.EntityArchitect;
import net.tangotek.tektopia.items.ItemStructureToken;

public class EntityLDOHArchitect extends EntityArchitect {
	
	public EntityLDOHArchitect(World world) {
		super(world);
	}
	
	public EntityLDOHArchitect(World world, EntityArchitect base) {
		super(world);
		copyLocationAndAnglesFrom(base);
		cloneFrom(base);
	}
	
	@Override
	protected void populateBuyingList() {
		if (buyingList == null && hasVillage()) {   
			buyingList = new MerchantRecipeList();
			for (ItemStructureToken token : ModItems.structureTokens) {
				if (token.getCost(village) > 0 &! (token == ModItems.structureBarracks || 
						token == ModItems.structureGuardPost || token == ModItems.structureMineshaft)) {
					ItemStack tokenItem = ModItems.createTaggedItem(token, ItemTagType.VILLAGER);
					ModItems.bindItemToVillage(tokenItem, village);
					buyingList.add(createMerchantRecipe(tokenItem, token.getCost(village)));
				} 
			}
		} 
	} 
	
}
