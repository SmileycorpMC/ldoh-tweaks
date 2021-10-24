package net.smileycorp.ldoh.common;

import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.World;

public class DayTimeSpeedModifier extends AttributeModifier {
	
	protected final World world;

	public DayTimeSpeedModifier(World world) {
		super(UUID.fromString("3bff7d3a-a53d-4ba1-8f6b-ae867bbbda4b"), "daytime", 0.5, 2);
		this.world = world;
		setSaved(true);
	}
	
	@Override
	public double getAmount() {
        return world.getTotalWorldTime() % 24000 < 12000 ? -0.3 : 0;
    }

}
