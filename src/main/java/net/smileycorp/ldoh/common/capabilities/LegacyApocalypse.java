package net.smileycorp.ldoh.common.capabilities;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.*;
import com.dhanantry.scapeandrunparasites.entity.monster.ancient.EntityOronco;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.nexus.EntityVenkrolSIV;
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

public class LegacyApocalypse implements IApocalypse {

    public static WeightedOutputs<Class<? extends EntityParasiteBase>> adaptedtable = init();

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

    private int timer = 0;
    private EntityPlayer player;
    private int wave = 0;
    private boolean started = false;

    public LegacyApocalypse(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("timer")) {
            timer = nbt.getInteger("timer");
        }
        if (nbt.hasKey("wave")) {
            wave = nbt.getInteger("wave");
        }
        if (nbt.hasKey("started")) {
            started = nbt.getBoolean("started");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("timer", timer);
        nbt.setInteger("wave", wave);
        nbt.setBoolean("started", started);
        return nbt;
    }

    @Override
    public void update(World world) {
        if (world.isRemote |! isActive(world)) return;
        if ((timer == 0)) {
            spawnWave(world);
            if (wave == 7) player.sendMessage(new TextComponentTranslation("message.ldoh.EventEnd"));
        }
        timer--;
    }

    @Override
    public boolean isActive(World world) {
        return player != null && wave > 0 && wave < 7;
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
    public void onBossHurt(IApocalypseBoss capability, float amount) {}

    @Override
    public boolean canStart(World world) {
        return !started && world.getWorldTime() > 2418000 && world.getWorldTime() % 24000 > 18000;
    }

    @Override
    public void startEvent() {
        if (player == null) return;
        wave = 1;
        player.sendMessage(new TextComponentTranslation("message.ldoh.WorldsEnd"));
        started = true;
    }

    @Override
    public void spawnWave(World world) {
        for (Class<? extends EntityParasiteBase> clazz : getSpawnsForWave(wave, world.rand)) {
            Vec3d vec = DirectionUtils.getRandomDirectionVecXZ(world.rand);
            BlockPos localpos = DirectionUtils.getClosestLoadedPos(world, player.getPosition(), vec, 75);
            EntityLightningBolt bolt = new EntityLightningBolt(world, localpos.getX(), localpos.getY(), localpos.getZ(), true);
            world.spawnEntity(bolt);
            try {
                EntityParasiteBase entity = clazz.getConstructor(World.class).newInstance(world);
                entity.onAddedToWorld();
                entity.setPosition(localpos.getX(), localpos.getY(), localpos.getZ());
                world.spawnEntity(entity);
                entity.targetTasks.taskEntries.clear();
                entity.targetTasks.addTask(1, new EntityAIHurtByTarget(entity, true, EntityParasiteBase.class));
                entity.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(entity, EntityPlayer.class, false));
                entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(150.0D);
            } catch (Exception e) {}
        }
        timer = 1000;
        wave++;
    }

    private List<Class<? extends EntityParasiteBase>> getSpawnsForWave(int wave, Random rand) {
        List<Class<? extends EntityParasiteBase>> spawnlist = new ArrayList<>();
        if (wave % 2 == 0) {
            spawnlist.add(EntityOronco.class);
            for (int i = 0; i < 3 + Math.round(wave * 0.3); i++) spawnlist.addAll(adaptedtable.getResults(rand));
            if (wave == 6) spawnlist.add(EntityOronco.class);
        } else if (wave % 2 == 1) for (int i = 0; i < 3 + Math.round(wave * 1.4); i++) spawnlist.add(EntityVenkrolSIV.class);
        return spawnlist;
    }

}
