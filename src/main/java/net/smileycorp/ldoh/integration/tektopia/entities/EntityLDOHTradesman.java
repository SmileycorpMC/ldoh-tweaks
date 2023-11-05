package net.smileycorp.ldoh.integration.tektopia.entities;

import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.smileycorp.ldoh.integration.tektopia.TektopiaUtils;
import net.tangotek.tektopia.ItemTagType;
import net.tangotek.tektopia.ModItems;
import net.tangotek.tektopia.entities.EntityTradesman;
import net.tangotek.tektopia.items.ItemProfessionToken;
import rafradek.TF2weapons.util.TF2Class;

public class EntityLDOHTradesman extends EntityTradesman {

    private static final int[] TOKEN_COSTS = {3, 5, 8, 6, 7, 13, 11, 9};

    public EntityLDOHTradesman(World world) {
        super(world);
    }

    public EntityLDOHTradesman(World world, EntityTradesman base) {
        super(world);
        copyLocationAndAnglesFrom(base);
        cloneFrom(base);
    }

    @Override
    protected void populateBuyingList() {
        if (buyingList == null && hasVillage()) {
            buyingList = new MerchantRecipeList();
            for (ItemProfessionToken token : ModItems.professionTokens.values()) {
                if (token == ModItems.itemGuard || token == ModItems.itemMiner || token == ModItems.itemCleric
                        || token == ModItems.itemDruid || token == ModItems.itemEnchanter) continue;
                if (token.getCost(village) > 0) {
                    ItemStack stack = ModItems.createTaggedItem(token, ItemTagType.VILLAGER);
                    ModItems.bindItemToVillage(stack, village);
                    buyingList.add(createMerchantRecipe(stack, token.getCost(village)));
                }
            }
            for (int i = 0; i < TF2Class.getClasses().size() - 1; i++) {
                ItemStack stack = ModItems.makeTaggedItem(new ItemStack(TektopiaUtils.TF2_PROF_TOKEN, 1, i), ItemTagType.VILLAGER);
                ModItems.bindItemToVillage(stack, village);
                buyingList.add(createMerchantRecipe(stack, TektopiaUtils.getCost(village, TOKEN_COSTS[i])));
            }
        }
    }

}
