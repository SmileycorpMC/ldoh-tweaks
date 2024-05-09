package net.smileycorp.ldoh.common.fluid;

import net.minecraftforge.fluids.Fluid;
import net.smileycorp.ldoh.common.Constants;

public class LDOHFluids {
    
    public static final Fluid EXPERIENCE = new Fluid("experience", Constants.loc("blocks/experience"), Constants.loc("blocks/experience_flowing"));
    
    public static final Fluid MORPHINE = new FluidLDOH("morphine", 0xFFFFFF);
    
    public static final Fluid NECROTIC_BLOOD = new FluidLDOH("necrotic_blood", 0x180000);
    
    public static final Fluid ENRICHED_ANTIBODY_SERUM = new FluidLDOH("enriched_antibody_serum", 0xC39538);
    
    public static final Fluid CARAMEL = new FluidLDOH("caramel", 0xCE6412);
    
    public static final Fluid COLA = new FluidLDOH("cola", 0x87381D);
    
}
