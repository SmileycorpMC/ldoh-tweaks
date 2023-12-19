package net.smileycorp.ldoh.common.entity.infphoenix;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityInfPhoenixCloud extends EntityInfPhoenix {

    public EntityInfPhoenixCloud(World world) {
        super(world);
    }

    @Override
    public String getName() {
        return "cloud";
    }

    @Override
    protected boolean isImmune(DamageSource source) {
        return source == DamageSource.DROWN;
    }

    @Override
    protected int getIndex() {
        return 0;
    }


}
