package net.smileycorp.ldoh.common.util;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.World;

import java.util.UUID;

public class DayCountSpeedModifier extends AttributeModifier {

    public static final UUID MODIFIER_UUID = UUID.fromString("fd1cdba3-d7b6-4eae-8e44-9342203358c5");

    protected final World world;

    public DayCountSpeedModifier(World world) {
        super(MODIFIER_UUID, "daycount", 0.5, 2);
        this.world = world;
        setSaved(true);
    }

    @Override
    public double getAmount() {
        return Math.min((1 + (world.getWorldTime() / 24000)) * 0.02, 2);
    }

}
