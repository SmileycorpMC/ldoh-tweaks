package net.smileycorp.ldoh.common.recipes;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.stream.Stream;

public class LDOHRecipeRegistry {

    private static final List<MixingRecipe> MIXING_RECIPES = Lists.newArrayList();

    private static final List<BottlingRecipe> BOTTLING_RECIPES = Lists.newArrayList();

    private static final List<SiphoningRecipe> SIPHONING_RECIPES = Lists.newArrayList();
    
    private static final List<MeltingRecipe> MELTING_RECIPES = Lists.newArrayList();

    public static void registerMixingRecipe(MixingRecipe recipe) {
        MIXING_RECIPES.add(recipe);
    }

    public static void registerBottlingRecipe(BottlingRecipe recipe) {
        BOTTLING_RECIPES.add(recipe);
    }

    public static void registerSiphoningRecipe(SiphoningRecipe recipe) {
        SIPHONING_RECIPES.add(recipe);
    }
    
    public static void registerMeltingRecipe(MeltingRecipe recipe) {
        MELTING_RECIPES.add(recipe);
    }

    public static MixingRecipe getMixingRecipe(FluidStack... inputs) {
        for (MixingRecipe recipe : MIXING_RECIPES) if (recipe.matches(inputs)) return recipe;
        return null;
    }

    public static BottlingRecipe getBottlingRecipe(FluidStack input1, ItemStack input2) {
        for (BottlingRecipe recipe : BOTTLING_RECIPES) if (recipe.matches(input1, input2)) return recipe;
        return null;
    }

    public static SiphoningRecipe getSiphoningRecipe(ItemStack input) {
        for (SiphoningRecipe recipe : SIPHONING_RECIPES) if (recipe.matches(input)) return recipe;
        return null;
    }
    
    public static MeltingRecipe getMeltingRecipe(ItemStack input) {
        for (MeltingRecipe recipe : MELTING_RECIPES) if (recipe.matches(input)) return recipe;
        return null;
    }

    public static boolean canMix(FluidStack... inputs) {
        return getMixingRecipe(inputs) != null;
    }

    public static boolean canBottle(FluidStack input1, ItemStack input2) {
        return getBottlingRecipe(input1, input2) != null;
    }

    public static boolean canSiphon(ItemStack input) {
        return getSiphoningRecipe(input) != null;
    }
    
    public static boolean canMelt(ItemStack input) {
        return getMeltingRecipe(input) != null;
    }

    public static Stream<MixingRecipe> getMixingRecipes() {
        return MIXING_RECIPES.stream();
    }

    public static Stream<BottlingRecipe> getBottlingRecipes() {
        return BOTTLING_RECIPES.stream();
    }

    public static Stream<SiphoningRecipe> getSiphoningRecipes() {
        return SIPHONING_RECIPES.stream();
    }
    
    public static Stream<MeltingRecipe> getMeltingRecipes() {
        return MELTING_RECIPES.stream();
    }

}
