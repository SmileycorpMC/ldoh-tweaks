package net.smileycorp.ldoh.common.entity.ai;

import funwayguy.epicsiegemod.ai.ESM_EntityAIDigging;
import funwayguy.epicsiegemod.ai.ESM_EntityAIGrief;
import funwayguy.epicsiegemod.ai.ESM_EntityAIPillarUp;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.smileycorp.ldoh.common.entity.zombie.EntityProfessionZombie;
import net.smileycorp.ldoh.common.entity.zombie.EntityTF2Zombie;
import net.smileycorp.ldoh.common.entity.zombie.EntityZombieFireman;

//hackery bypass half of the required ai tasks
public class LDOHZombieAITasks extends EntityAITasks {

    private final EntityZombie entity;
    private final boolean hasESMAI;
    private int phase = 0;

    public LDOHZombieAITasks(EntityZombie entity) {
        super(entity.world != null && entity.world.profiler != null ? entity.world.profiler : null);
        this.entity = entity;
        //check to see if the zombie can have epic siege ai tasks
        //don't want other zombie derivatives such as crawlers and firemen to be able to build/break when they aren't supposed to
        hasESMAI = entity.getClass() == EntityZombie.class || entity.getClass() == EntityZombieVillager.class
                || entity.getClass() == EntityHusk.class || entity.getClass() == EntityTF2Zombie.class
                || entity.getClass() == EntityProfessionZombie.class;
        if (hasESMAI) taskEntries.add(new EntityAITaskEntry(6, new ESM_EntityAIGrief(entity)));
        //add special fireman tasks
        if (entity instanceof EntityZombieFireman) {
            taskEntries.add(new EntityAITaskEntry(1, new ESM_EntityAIDigging(entity)));
            taskEntries.add(new EntityAITaskEntry(6, new ESM_EntityAIGrief(entity)));
        }
    }

    public void onUpdateTasks() {
        checkAndAddTasks();
        super.onUpdateTasks();
    }

    //this might mess things up, but it eliminates our need to have 4 separate zombie entities
    private void checkAndAddTasks() {
        //check to make sure all zombie derivative entities don't get the esm ai
        if (!hasESMAI) return;
        if (phase < 1 && entity.world.getWorldTime() > 254000) {
            phase = 1;
            taskEntries.add(new EntityAITaskEntry(1, new ESM_EntityAIDigging(entity)));
        }
        if (phase < 2 && entity.world.getWorldTime() > 474000){
            phase = 2;
            taskEntries.add(new EntityAITaskEntry(2, new ESM_EntityAIPillarUp(entity)));
        }
    }

}
