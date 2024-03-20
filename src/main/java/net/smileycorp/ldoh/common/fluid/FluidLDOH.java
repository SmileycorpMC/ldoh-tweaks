package net.smileycorp.ldoh.common.fluid;

import net.minecraftforge.fluids.Fluid;
import net.smileycorp.ldoh.common.ModDefinitions;

public class FluidLDOH extends Fluid {
    
    public FluidLDOH(String name, int colour) {
        super(name, ModDefinitions.getResource("blocks/fluid"), ModDefinitions.getResource("blocks/fluid_flowing"));
        setColor(colour);
    }
    
}
