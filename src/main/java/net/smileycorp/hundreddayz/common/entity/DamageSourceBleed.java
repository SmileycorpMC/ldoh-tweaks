package net.smileycorp.hundreddayz.common.entity;

import net.minecraft.util.DamageSource;

public class DamageSourceBleed extends DamageSource {
	
	public DamageSourceBleed() {
		super("Bleed");
	}
	
	@Override
	public boolean isDamageAbsolute() {
        return true;
    }

}
