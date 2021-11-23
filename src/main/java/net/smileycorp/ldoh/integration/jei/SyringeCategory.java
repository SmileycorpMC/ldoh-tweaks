package net.smileycorp.ldoh.integration.jei;

import java.awt.Color;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.integration.jei.SyringeCategory.Wrapper;

@SuppressWarnings("deprecation")
public class SyringeCategory implements IRecipeCategory<Wrapper> {

	public static final String ID = ModDefinitions.getName("syringes");

	private final IDrawable background;
	protected final IDrawableAnimated arrow;

	public static final ResourceLocation TEXTURE = ModDefinitions.getResource("textures/gui/jei/syringes.png");

	public SyringeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(TEXTURE, 0, 0, 128, 96);
		IDrawableStatic arrowDrawable = guiHelper.createDrawable(TEXTURE, 0, 96, 24, 17);
		arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 160, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public String getModName() {
		return ModDefinitions.MODID;
	}

	@Override
	public String getTitle() {
		return I18n.translateToLocal("jei.category.hundreddayz.Syringe").trim();
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {
		arrow.draw(minecraft, 52, 24);
	}

	@Override
	public String getUid() {
		return ID;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup items = recipeLayout.getItemStacks();
		items.init(0, true, 23, 24);
		items.init(1, false, 87, 24);

		recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
		recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).get(0));
	}

	public static class Wrapper implements IRecipeWrapper {

		@Override
		public void getIngredients(IIngredients ingredients) {
			ingredients.setInput(ItemStack.class, new ItemStack(LDOHItems.SYRINGE, 1, 0));
			ingredients.setOutput(ItemStack.class, new ItemStack(LDOHItems.SYRINGE, 1, 1));
		}

		@Override
		public void drawInfo(Minecraft minecraft, int width, int height, int mouseX, int mouseY) {
			String desc = I18n.translateToLocal("tooltip.hundreddayz.Syringe");
			minecraft.fontRenderer.drawString(desc, ((width/2)-(minecraft.fontRenderer.getStringWidth(desc)/2)), height+10, Color.RED.getRGB());
		}

	}

}
