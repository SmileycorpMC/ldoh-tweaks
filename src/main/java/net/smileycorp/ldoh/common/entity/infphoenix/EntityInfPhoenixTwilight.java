package net.smileycorp.ldoh.common.entity.infphoenix;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityInfPhoenixTwilight extends EntityInfPhoenix {

    public EntityInfPhoenixTwilight(World world) {
        super(world);
    }

    @Override
    public String getVariant() {
        return "twilight";
    }

    @Override
    protected boolean isImmune(DamageSource source) {
        return source == DamageSource.DROWN;
    }

    @Override
    protected int getIndex() {
        return 6;
    }

}
