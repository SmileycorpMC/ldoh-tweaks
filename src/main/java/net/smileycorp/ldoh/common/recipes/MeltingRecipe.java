package net.smileycorp.ldoh.common.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class MeltingRecipe {

    protected final FluidStack result;
    private final ItemStack input;

    public MeltingRecipe(FluidStack result, ItemStack input) {
        this.result = result;
        this.input = input;
    }

    public boolean matches(ItemStack input) {
        return input.getItem() == this.input.getItem() && input.getCount() >= this.input.getCount() && (this.input.getMetadata() ==
                input.getMetadata() || this.input.getMetadata() == OreDictionary.WILDCARD_VALUE) &&
                (!this.input.hasTagCompound() || ItemStack.areItemStackTagsEqual(input, this.input));
    }

    public FluidStack getResult(ItemStack inputs) {
        return result.copy();
    }

    public void craft(ItemStack input) {
        if (input == null || this.input == null) return;
        input.shrink(this.input.getCount());
    }

}
