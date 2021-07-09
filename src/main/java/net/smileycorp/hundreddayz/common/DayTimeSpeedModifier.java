package net.smileycorp.hundreddayz.common;

import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.World;

public class DayTimeSpeedModifier extends AttributeModifier {
	
	protected final World world;

	public DayTimeSpeedModifier(World world) {
		super(new UUID(854337177, 567785239), "time speed modifier", 0.5, 0);
		this.world = world;
		setSaved(true);
	}
	
	@Override
	public double getAmount() {
		long time  = world.getTotalWorldTime() % 24000;
        return world.getWorldTime() < 239999 && time < 12000 ? -0.5 : 0;
    }

}
