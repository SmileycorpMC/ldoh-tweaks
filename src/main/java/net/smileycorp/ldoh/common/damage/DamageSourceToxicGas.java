package net.smileycorp.ldoh.common.damage;

import net.minecraft.util.DamageSource;

public class DamageSourceToxicGas extends DamageSource {

	public DamageSourceToxicGas() {
		super("ToxicGas");
	}
	
	@Override
	public boolean isDamageAbsolute() {
        return true;
    }

}
