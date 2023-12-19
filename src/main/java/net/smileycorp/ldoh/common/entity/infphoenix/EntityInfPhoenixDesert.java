package net.smileycorp.ldoh.common.entity.infphoenix;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityInfPhoenixDesert extends EntityInfPhoenix {

    public EntityInfPhoenixDesert(World world) {
        super(world);
    }

    @Override
    protected void updateAITasks() {
        if (isWet()) attackEntityFrom(DamageSource.DROWN, 1.0F);
        super.updateAITasks();
    }

    @Override
    public String getName() {
        return "desert";
    }

    @Override
    protected boolean isImmune(DamageSource source) {
        return false;
    }

    @Override
    protected int getIndex() {
        return 1;
    }

}
