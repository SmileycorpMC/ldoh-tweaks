package net.smileycorp.ldoh.common.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BottlingRecipe {

    protected final ItemStack result;
    private final FluidStack input1;
    private final ItemStack input2;

    public BottlingRecipe(ItemStack result, FluidStack input1, ItemStack input2) {
        this.result = result;
        this.input1 = input1;
        this.input2 = input2;
    }

    public boolean matches(FluidStack input1, ItemStack input2) {
        if (input1 == null || this.input1 == null) return false;
        if (!(input1.isFluidEqual(this.input1) && input1.amount >= this.input1.amount)) return false;
        return input2.getItem() == this.input2.getItem() && input2.getCount() >= this.input2.getCount() && ItemStack.areItemStackTagsEqual(input2, this.input2);
    }

    public ItemStack getResult(FluidStack inputs) {
        return result.copy();
    }

    public void craft(FluidStack input1, ItemStack input2) {
        if (input1 != null && this.input1 != null) input1.amount -= this.input1.amount;
        if (input2 != null && this.input2 != null) input2.shrink(this.input2.getCount());
    }

}
