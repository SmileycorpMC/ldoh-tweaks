package net.smileycorp.ldoh.common.entity.infphoenix;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityInfPhoenixNether extends EntityInfPhoenix {

    public EntityInfPhoenixNether(World world) {
        super(world);
        isImmuneToFire = true;
    }

    @Override
    protected void updateAITasks() {
        if (isWet()) attackEntityFrom(DamageSource.DROWN, 1.0F);
        super.updateAITasks();
    }

    @Override
    public String getVariant() {
        return "nether";
    }

    @Override
    protected boolean isImmune(DamageSource source) {
        return source.isFireDamage();
    }

    @Override
    protected int getIndex() {
        return 4;
    }

}
