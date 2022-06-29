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
		/*registry.addRecipeCategories(new IntestineCategory(guiHelper));
		registry.addRecipeCategories(new GunWorkbenchCategory(guiHelper));
		registry.addRecipeCategories(new OilMillCategory(guiHelper));
		registry.addRecipeCategories(new BlastFurnaceCategory(guiHelper));
		registry.addRecipeCategories(new BackmixReactorCategory(guiHelper));
		registry.addRecipeCategories(new SplitTankCategory(guiHelper));*/
	}

	@Override
	public void register(@Nonnull IModRegistry registry) {
		//ldoh recipes
		//syringes
		registry.handleRecipes(SyringeCategory.Wrapper.class, (r) -> r, SyringeCategory.ID);
		registry.addRecipes(Lists.newArrayList(new SyringeCategory.Wrapper()), SyringeCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(LDOHItems.SYRINGE), SyringeCategory.ID);

		/*fish's undead rising recipes
		//intestines loot table
		registry.handleRecipes(IntestineCategory.Wrapper.class, (r) -> r, IntestineCategory.ID);
		registry.addRecipes(Lists.newArrayList(new IntestineCategory.Wrapper()), IntestineCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(FishItems.INTESTINE), IntestineCategory.ID);

		//crayfish gun mod recipes
		//gun workbench
		registry.handleRecipes(GunWorkbenchCategory.Wrapper.class, (r) -> r, GunWorkbenchCategory.ID);
		registry.addRecipes(GunWorkbenchCategory.getRecipes(), GunWorkbenchCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(com.mrcrayfish.guns.init.ModBlocks.WORKBENCH), GunWorkbenchCategory.ID);

		//car mod recipes
		//oil mill
		registry.handleRecipes(CarMachineRecipeWrapper.class, (r) -> r, OilMillCategory.ID);
		registry.addRecipes(Lists.newArrayList(new CarMachineRecipeWrapper(new ItemStack(ModItems.CANOLA), new ItemStack(ModItems.RAPECAKE), new FluidStack(ModFluids.CANOLA_OIL, 100))), OilMillCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(de.maxhenkel.car.blocks.ModBlocks.OIL_MILL), OilMillCategory.ID);

		//blast furnace
		registry.handleRecipes(CarMachineRecipeWrapper.class, (r) -> r, BlastFurnaceCategory.ID);
		registry.addRecipes(Lists.newArrayList(new CarMachineRecipeWrapper(OreDictionary.getOres("logWood"), new ItemStack(Items.COAL, 1, 1), new FluidStack(ModFluids.METHANOL, 100))), BlastFurnaceCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(de.maxhenkel.car.blocks.ModBlocks.BLAST_FURNACE), BlastFurnaceCategory.ID);

		//backmix reactor
		registry.handleRecipes(BackmixReactorCategory.Wrapper.class, (r) -> r, BackmixReactorCategory.ID);
		registry.addRecipes(Lists.newArrayList(new BackmixReactorCategory.Wrapper()), BackmixReactorCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(de.maxhenkel.car.blocks.ModBlocks.BACKMIX_REACTOR), BackmixReactorCategory.ID);

		//split tank
		registry.handleRecipes(SplitTankCategory.Wrapper.class, (r) -> r, SplitTankCategory.ID);
		registry.addRecipes(Lists.newArrayList(new SplitTankCategory.Wrapper()), SplitTankCategory.ID);
		registry.addRecipeCatalyst(new ItemStack(de.maxhenkel.car.blocks.ModBlocks.SPLIT_TANK), SplitTankCategory.ID);*/
	}

}
