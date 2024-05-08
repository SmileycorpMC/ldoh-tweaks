package net.smileycorp.ldoh.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;

public interface IDummyZombie {

    default Class<? extends Entity> getBase() {
        return EntityZombie.class;
    }

}
