package net.smileycorp.ldoh.common.entity.infphoenix;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityInfPhoenixWater extends EntityInfPhoenix {

    public EntityInfPhoenixWater(World world) {
        super(world);
    }

    @Override
    public String getName() {
        return "water";
    }

    @Override
    protected boolean isImmune(DamageSource source) {
        return source == DamageSource.DROWN;
    }

    @Override
    protected int getIndex() {
        return 7;
    }

}
