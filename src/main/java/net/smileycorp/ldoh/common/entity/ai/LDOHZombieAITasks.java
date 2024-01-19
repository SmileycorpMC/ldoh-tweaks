package net.smileycorp.ldoh.common.entity.ai;

import funwayguy.epicsiegemod.ai.ESM_EntityAIDigging;
import funwayguy.epicsiegemod.ai.ESM_EntityAIGrief;
import funwayguy.epicsiegemod.ai.ESM_EntityAIPillarUp;
import funwayguy.epicsiegemod.ai.ESM_EntityAISwimming;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.smileycorp.ldoh.common.entity.EntityProfessionZombie;
import net.smileycorp.ldoh.common.entity.EntityTF2Zombie;
import net.smileycorp.ldoh.common.entity.EntityZombieFireman;

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
        //add tasks now, because we can't add them outside this class
        taskEntries.add(new EntityAITaskEntry(0, new ESM_EntityAISwimming(entity)));
        taskEntries.add(new EntityAITaskEntry(3, new EntityAIZombie(entity)));
        taskEntries.add(new EntityAITaskEntry(8, new EntityAIWanderAvoidWater(entity, 1.0D)));
        if (hasESMAI) taskEntries.add(new EntityAITaskEntry(6, new ESM_EntityAIGrief(entity)));
        //add special fireman tasks
        if (entity instanceof EntityZombieFireman) {
            taskEntries.add(new EntityAITaskEntry(1, new ESM_EntityAIDigging(entity)));
            taskEntries.add(new EntityAITaskEntry(6, new ESM_EntityAIGrief(entity)));
        }
    }

    //prevent other mods from overhauling our ai optimisations
    public void addTask(int priority, EntityAIBase task) {}

    public void onUpdateTasks() {
        checkAndAddTasks();
        //hopefully vanilla behaviour here won't be too slow to process, but 6 ai tasks may end up being too many
        super.onUpdateTasks();
    }

    //this might mess things up, but it eliminates our need to have 4 separate zombie entities
    private void checkAndAddTasks() {
        //check to make sure all zombie derivative entities don't get the esm ai
        if (!hasESMAI) return;
        if (phase < 1 && entity.world.getWorldTime() > 240000) {
            phase = 1;
            taskEntries.add(new EntityAITaskEntry(1, new ESM_EntityAIDigging(entity)));
        }
        if (phase < 2 && entity.world.getWorldTime() > 480000){
            phase = 2;
            taskEntries.add(new EntityAITaskEntry(2, new ESM_EntityAIPillarUp(entity)));
        }
    }

}
