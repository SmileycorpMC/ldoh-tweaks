package net.smileycorp.ldoh.common.util;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.World;

import java.util.UUID;

public class DayTimeSpeedModifier extends AttributeModifier {

    public static final UUID MODIFIER_UUID = UUID.fromString("3bff7d3a-a53d-4ba1-8f6b-ae867bbbda4b");

    protected final World world;

    public DayTimeSpeedModifier(World world) {
        super(MODIFIER_UUID, "daytime", 0.5, 2);
        this.world = world;
        setSaved(true);
    }

    @Override
    public double getAmount() {
        return world.isDaytime() ? -0.3 :  (world.getCurrentMoonPhaseFactor() - 0.5) * 0.1;
    }

}
