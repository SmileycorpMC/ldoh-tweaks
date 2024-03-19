package net.smileycorp.ldoh.common.fluid;

import net.minecraftforge.fluids.Fluid;
import net.smileycorp.ldoh.common.ModDefinitions;

public class LDOHFluids {
    
    public static final Fluid EXPERIENCE = new Fluid("experience", ModDefinitions.getResource("blocks/fluids/experience"), ModDefinitions.getResource("blocks/fluids/experience_flowing"));
    
    public static final Fluid MORPHINE = new Fluid("morphine", ModDefinitions.getResource("blocks/fluids/morphine"), ModDefinitions.getResource("blocks/fluids/morphine_flowing"));
    
    public static final Fluid NECROTIC_BLOOD = new Fluid("necrotic_blood", ModDefinitions.getResource("blocks/fluids/necrotic_blood"), ModDefinitions.getResource("blocks/fluids/necrotic_blood_flowing"));
    
    public static final Fluid ENRICHED_ANTIBODY_SERUM = new Fluid("enriched_antibody_serum", ModDefinitions.getResource("blocks/fluids/enriched_antibody_serum"), ModDefinitions.getResource("blocks/fluids/enriched_antibody_serum_flowing"));
    
}
