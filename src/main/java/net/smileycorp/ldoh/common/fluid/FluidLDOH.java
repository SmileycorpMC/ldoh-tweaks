package net.smileycorp.ldoh.common.fluid;

import net.minecraftforge.fluids.Fluid;
import net.smileycorp.ldoh.common.Constants;

public class FluidLDOH extends Fluid {
    
    public FluidLDOH(String name, int colour) {
        super(name, Constants.loc("blocks/fluid"), Constants.loc("blocks/fluid_flowing"));
        setColor(colour);
    }
    
}
