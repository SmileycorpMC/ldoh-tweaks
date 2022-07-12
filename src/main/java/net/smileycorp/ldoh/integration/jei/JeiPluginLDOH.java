package net.smileycorp.ldoh.integration.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.smileycorp.ldoh.common.item.LDOHItems;

import com.google.common.collect.Lists;

@JEIPlugin
public class JeiPluginLDOH implements IModPlugin {

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registry.addRecipeCategories(new SyringeCategory(guiHelper));
	}

	@Override
	public void register(@Nonnull IModRegistry registry) {
		//syringes
		registry.handleRecipes(SyringeCategory.Wrapper.class, (r) -> r, SyringeCategory.ID);
		registry.addRecipes(Lists.newArrayList(new SyringeCategory.Wrapper()), SyringeCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(LDOHItems.SYRINGE), SyringeCategory.ID);
	}

}
