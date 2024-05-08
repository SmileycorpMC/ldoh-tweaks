package net.smileycorp.ldoh.integration.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.smileycorp.ldoh.common.item.LDOHItems;

import javax.annotation.Nonnull;

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
