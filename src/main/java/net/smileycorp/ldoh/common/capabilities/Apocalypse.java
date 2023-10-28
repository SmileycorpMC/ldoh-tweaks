package net.smileycorp.ldoh.common.capabilities;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.*;
import com.dhanantry.scapeandrunparasites.entity.monster.ancient.EntityOronco;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.nexus.EntityVenkrolSIII;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityButhol;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.recipe.WeightedOutputs;
import net.smileycorp.atlas.api.util.DirectionUtils;

import java.util.*;

public class Apocalypse implements IApocalypse {

    public static WeightedOutputs<Class<? extends EntityParasiteBase>> adaptedtable = init();

    protected EntityParasiteBase boss;
    protected int phase = 0;
    private EntityPlayer player;

    protected boolean hasStarted;

    protected float multiplier = 0;

    private static WeightedOutputs<Class<? extends EntityParasiteBase>> init() {
        Map<Class<? extends EntityParasiteBase>, Integer> adaptedmap = new HashMap<>();
        adaptedmap.put(EntityShycoAdapted.class, 1);
        adaptedmap.put(EntityCanraAdapted.class, 1);
        adaptedmap.put(EntityNoglaAdapted.class, 1);
        adaptedmap.put(EntityHullAdapted.class, 1);
        adaptedmap.put(EntityEmanaAdapted.class, 1);
        adaptedmap.put(EntityBanoAdapted.class, 1);
        adaptedmap.put(EntityRanracAdapted.class, 1);
        return new WeightedOutputs<>(adaptedmap);
    }

    public Apocalypse(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("boss") && player != null) {
            Entity entity = player.world.getEntityByID(nbt.getInteger("boss"));
            if (entity instanceof EntityParasiteBase) {
                boss = (EntityParasiteBase) entity;
                multiplier = boss.getMaxHealth() / 8f;
                if (boss.hasCapability(LDOHCapabilities.APOCALYPSE_BOSS, null))
                    boss.getCapability(LDOHCapabilities.APOCALYPSE_BOSS, null).setPlayer(player);

            }
        }
        if (nbt.hasKey("phase")) {
            phase = nbt.getInteger("phase");
        }
        if (nbt.hasKey("hasStarted")) {
            hasStarted = nbt.getBoolean("hasStarted");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (boss != null && boss.isEntityAlive()) nbt.setInteger("boss", boss.getEntityId());
        if (phase > 0) nbt.setInteger("phase", phase);
        nbt.setBoolean("hasStarted", hasStarted);
        return nbt;
    }

    @Override
    public void update(World world) {
        if (!world.isRemote && isActive(world)) {
            if (player.ticksExisted % 60 == 0) {
                Vec3d vec = DirectionUtils.getRandomDirectionVecXZ(world.rand);
                BlockPos localpos = DirectionUtils.getClosestLoadedPos(world, player.getPosition(), vec, 75);
                EntityLightningBolt bolt = new EntityLightningBolt(world, localpos.getX(), localpos.getY(), localpos.getZ(), true);
                world.spawnEntity(bolt);
                spawnWave(world);
            }
        }
    }

    @Override
    public boolean isActive(World world) {
        return player != null && boss != null;
    }

    @Override
    public EntityPlayer getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public boolean canStart(World world) {
        return world.getWorldTime() > 2418000 && world.getWorldTime() % 24000 > 18000 && phase == 0 && hasStarted == false;
    }

    @Override
    public void startEvent() {
        if (player != null) {
            hasStarted = true;
            player.world.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
            player.sendMessage(new TextComponentTranslation("message.hundreddayz.WorldsEnd"));
            for (int i = 0; i < 3; i++) spawnEntity(player.world, new EntityVenkrolSIII(player.world));
            boss = spawnEntity(player.world, new EntityOronco(player.world));
            multiplier = boss.getMaxHealth() / 8f;
            if (boss.hasCapability(LDOHCapabilities.APOCALYPSE_BOSS, null))
                boss.getCapability(LDOHCapabilities.APOCALYPSE_BOSS, null).setPlayer(player);
        }
    }

    @Override
    public void spawnWave(World world) {
        for (Class<? extends EntityParasiteBase> clazz : getSpawnsForWave(world.rand)) {
            Vec3d vec = DirectionUtils.getRandomDirectionVecXZ(world.rand);
            BlockPos localpos = DirectionUtils.getClosestLoadedPos(world, player.getPosition(), vec, 65);
            EntityLightningBolt bolt = new EntityLightningBolt(world, localpos.getX(), localpos.getY(), localpos.getZ(), true);
            world.spawnEntity(bolt);
            try {
                spawnEntity(world, clazz.getConstructor(World.class).newInstance(world));
            } catch (Exception e) {
            }
        }
    }

    private EntityParasiteBase spawnEntity(World world, EntityParasiteBase entity) {
        Vec3d vec = DirectionUtils.getRandomDirectionVecXZ(world.rand);
        BlockPos localpos = DirectionUtils.getClosestLoadedPos(world, player.getPosition(), vec, 75);
        entity.onAddedToWorld();
        entity.setPosition(localpos.getX(), localpos.getY(), localpos.getZ());
        entity.enablePersistence();
        world.spawnEntity(entity);
        entity.targetTasks.taskEntries.clear();
        entity.targetTasks.addTask(1, new EntityAIHurtByTarget(entity, true, new Class[]{EntityParasiteBase.class}));
        entity.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(entity, EntityPlayer.class, false));
        entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(150.0D);
        return entity;
    }

    private List<Class<? extends EntityParasiteBase>> getSpawnsForWave(Random rand) {
        List<Class<? extends EntityParasiteBase>> spawnlist = new ArrayList<>();
        if (rand.nextInt(5) < 2)
            for (int i = 0; i < rand.nextInt(3) + 1; i++) spawnlist.addAll(adaptedtable.getResults(rand));
        else for (int i = 0; i < rand.nextInt(7) + 3; i++) spawnlist.add(EntityButhol.class);
        return spawnlist;
    }

    public void onBossHurt(IApocalypseBoss capability, float amount) {
        if (boss.isEntityAlive()) {
            int newPhase = (int) Math.floor((boss.getMaxHealth() - boss.getHealth() - amount) / multiplier);
            if (phase < newPhase) {
                boss.world.setWorldTime(boss.world.getWorldTime() + (750 * (newPhase - phase)));
                phase = newPhase;
            }
        } else {
            boss.world.setWorldTime(boss.world.getWorldTime() + (750 * (8 - phase)));
            phase = 8;
            boss.world.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
            player.sendMessage(new TextComponentTranslation("message.hundreddayz.EventEnd"));
            boss = null;
            capability.setPlayer(null);
        }
    }
}
