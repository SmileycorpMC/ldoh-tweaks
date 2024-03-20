package net.smileycorp.ldoh.common.fluid;

import net.minecraftforge.fluids.Fluid;
import net.smileycorp.ldoh.common.ModDefinitions;

public class LDOHFluids {
    
    public static final Fluid EXPERIENCE = new Fluid("experience", ModDefinitions.getResource("blocks/experience"), ModDefinitions.getResource("blocks/experience_flowing"));
    
    public static final Fluid MORPHINE = new FluidLDOH("morphine", 0xFFFFFF);
    
    public static final Fluid NECROTIC_BLOOD = new FluidLDOH("necrotic_blood", 0x180000);
    
    public static final Fluid ENRICHED_ANTIBODY_SERUM = new FluidLDOH("enriched_antibody_serum", 0xC39538);
    
}
