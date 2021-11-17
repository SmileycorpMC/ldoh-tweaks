package net.smileycorp.ldoh.integration.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.smileycorp.ldoh.common.item.LDOHItems;

import com.Fishmod.mod_LavaCow.init.FishItems;
import com.google.common.collect.Lists;

@JEIPlugin
public class JeiPluginLDOH implements IModPlugin {

	public static IJeiHelpers jeiHelpers;
	public static IIngredientRegistry ingredientRegistry = null;


	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registry.addRecipeCategories(new SyringeCategory(guiHelper));
		registry.addRecipeCategories(new IntestineCategory(guiHelper));
	}

	@Override
	public void register(@Nonnull IModRegistry registry) {
		ingredientRegistry = registry.getIngredientRegistry();
		registry.handleRecipes(SyringeCategory.Wrapper.class, (r) -> r, SyringeCategory.ID);
		registry.addRecipes(Lists.newArrayList(new SyringeCategory.Wrapper()), SyringeCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(LDOHItems.SYRINGE), SyringeCategory.ID);
		registry.handleRecipes(IntestineCategory.Wrapper.class, (r) -> r, IntestineCategory.ID);
		registry.addRecipes(Lists.newArrayList(new IntestineCategory.Wrapper()), IntestineCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(FishItems.INTESTINE), IntestineCategory.ID);
	}



}
