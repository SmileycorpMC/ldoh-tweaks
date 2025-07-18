package net.smileycorp.ldoh.common.entity.infphoenix;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityInfPhoenixFire extends EntityInfPhoenix {

    public EntityInfPhoenixFire(World world) {
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
        return "fire";
    }

    @Override
    protected boolean isImmune(DamageSource source) {
        return source.isFireDamage();
    }

    @Override
    protected int getIndex() {
        return 3;
    }

    public boolean attackEntityAsMob(Entity target) {
        target.setFire(5);
        return super.attackEntityAsMob(target);
    }

}
