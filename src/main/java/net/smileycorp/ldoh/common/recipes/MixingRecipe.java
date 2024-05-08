package net.smileycorp.ldoh.common.recipes;

import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ArrayUtils;

public class MixingRecipe {

    protected final FluidStack result;
    private final FluidStack[] inputs;

    public MixingRecipe(FluidStack result, FluidStack... inputs) {
        this.result = result;
        if (inputs.length > 3) this.inputs = ArrayUtils.subarray(inputs, 0, 3);
        else this.inputs = inputs;
    }

    public boolean matches(FluidStack... inputs) {
        if (inputs.length != this.inputs.length) return false;
        for (int i = 0; i < inputs.length; i++) {
            FluidStack input = inputs[i];
            if (input == null) continue;
            if (!checkInputs(input)) return false;
        }
        return true;
    }

    private boolean checkInputs(FluidStack input) {
        for (int j = 0; j < inputs.length; j++) {
            FluidStack check = this.inputs[j];
            if (check == null) continue;
            if (input.isFluidEqual(check) && input.amount >= check.amount) return true;
        }
        return false;
    }

    public FluidStack getResult(FluidStack... inputs) {
        return result.copy();
    }

    public void craft(FluidStack... inputs) {
        if (inputs.length != this.inputs.length) return;
        for (int i = 0; i < inputs.length; i++) {
            FluidStack input = inputs[i];
            for (int j = 0; j < inputs.length; j++) {
                FluidStack check = this.inputs[j];
                if (check == null) continue;
                if (input.isFluidEqual(check)) input.amount -= check.amount;
            }
        }
    }

}
