package net.smileycorp.ldoh.common.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class SiphoningRecipe {

    protected final FluidStack result;
    private final ItemStack container;
    private final ItemStack input;

    public SiphoningRecipe(FluidStack result, ItemStack container, ItemStack input) {
        this.result = result;
        this.input = input;
        this.container = container;
    }

    public boolean matches(ItemStack input) {
        return input.getItem() == this.input.getItem() && input.getCount() >= this.input.getCount() && ItemStack.areItemStackTagsEqual(input, this.input);
    }

    public FluidStack getResult(ItemStack inputs) {
        return result.copy();
    }

    public ItemStack getContainer(ItemStack inputs) {
        return container.copy();
    }

    public void craft(ItemStack input) {
        if (input == null || this.input == null) return;
        input.shrink(this.input.getCount());
    }

}
