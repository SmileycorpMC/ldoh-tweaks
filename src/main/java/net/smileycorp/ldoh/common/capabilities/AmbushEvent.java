package net.smileycorp.ldoh.common.capabilities;

import chumbanotz.mutantbeasts.entity.mutant.MutantZombieEntity;
import com.Fishmod.mod_LavaCow.entities.*;
import com.Fishmod.mod_LavaCow.entities.flying.EntityPtera;
import com.Fishmod.mod_LavaCow.entities.flying.EntityVespa;
import com.Fishmod.mod_LavaCow.entities.tameable.EntitySalamander;
import com.Fishmod.mod_LavaCow.entities.tameable.EntityWeta;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.*;
import com.dhanantry.scapeandrunparasites.entity.monster.feral.EntityFerHuman;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.*;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.hordes.common.Hordes;
import net.smileycorp.hordes.hordeevent.HordeEventPacketHandler;
import net.smileycorp.hordes.hordeevent.HordeSoundMessage;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.entity.ai.AIAmbush;
import net.smileycorp.ldoh.common.entity.zombie.*;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;
import rafradek.TF2weapons.util.TF2Class;

import java.util.List;
import java.util.Random;

public class AmbushEvent implements IAmbushEvent {

    protected static final int[] times = {315000, 434000, 606000, 660000, 828000, 1094000, 1172000, 1373000, 1646000, 1756000, 1832000, 2130000, 2226000, 2310000, 2350000, 2400000, 2406000, 2412000};
    protected static final Type[] types = {Type.ALLY, Type.ALLY, Type.ZOMBIE, Type.ALLY, Type.ENEMY, Type.ZOMBIE, Type.ALLY, Type.ALLY, Type.PARASITE, Type.ZOMBIE, Type.ENEMY, Type.ALLY, Type.PARASITE, Type.ENEMY, Type.ZOMBIE, Type.PARASITE, Type.PARASITE, Type.PARASITE};

    protected int phase = 0;
    protected int cooldown = 0;

    protected boolean postGame;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("phase", phase);
        nbt.setInteger("cooldown", cooldown);
        nbt.setBoolean("postGame", postGame);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("phase")) phase = nbt.getInteger("phase");
        if (nbt.hasKey("cooldown")) cooldown = nbt.getInteger("cooldown");
        if (nbt.hasKey("postGame")) postGame = nbt.getBoolean("postGame");
    }

    @Override
    public void enablePostgame() {
        postGame = true;
        cooldown = new Random().nextInt(48000) + 6000;
    }

    @Override
    public boolean shouldSpawn(EntityPlayer player) {
        if (cooldown > 0) cooldown--;
        if (player == null || (phase >= types.length &! postGame)) return false;
        if (postGame) return cooldown <= 0;
        if (player.world.getWorldTime() >= times[phase] && cooldown <= 0) return true;
        return false;
    }

    @Override
    public void spawnAmbush(EntityPlayer player) {
        if (player == null) return;
        World world = player.world;
        if (world.isRemote) return;
        Type type = getType(player);
        if (type != Type.NONE) spawnAmbush(player, type, phase);
        else System.out.println("Raid type is none, " + player.getName() + " doesn't have a team, aborting raid.");
        phase++;
        cooldown = postGame ? world.rand.nextInt(48000) + 6000 : 6000;
    }

    @Override
    public void spawnAmbush(EntityPlayer player, Type type, int phase) {
        if (player == null) return;
        World world = player.world;
        if (world.isRemote) return;
        Random rand = world.rand;
        Vec3d dir = DirectionUtils.getRandomDirectionVecXZ(rand);
        BlockPos pos = DirectionUtils.getClosestLoadedPos(world, player.getPosition(), dir, 50);
        for (EntityLiving entity : buildList(world, player, type, phase)) {
            double x = pos.getX() + (rand.nextFloat() * 2) - 1;
            double z = pos.getZ() + (rand.nextFloat() * 2) - 1;
            int y = world.getHeight((int) x, (int) z);
            entity.setPosition(x + rand.nextFloat(), y, z + rand.nextFloat());
            entity.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
            entity.enablePersistence();
            world.spawnEntity(entity);
            if (type == Type.ALLY) entity.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 140));
            else if (type == Type.ENEMY) entity.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 100));
            entity.tasks.addTask(1, new AIAmbush(entity, player));
        }
        HordeEventPacketHandler.NETWORK_INSTANCE.sendTo(new HordeSoundMessage(dir, getSound(type)), (EntityPlayerMP) player);
    }


    private Type getType(EntityPlayer player) {
        if (postGame) return Type.randomType(player, player.world.rand);
        Type type = types[phase];
        if (type == Type.ALLY || type == Type.ENEMY) {
            if (player.getTeam() == null) return type == Type.ALLY ? Type.NONE : phase < 8 ? Type.ZOMBIE : Type.PARASITE;
            else if (!(player.getTeam().getName().equals("RED") || player.getTeam().getName().equals("BLU")))
                return type == Type.ALLY ? Type.NONE : phase < 8 ? Type.ZOMBIE : Type.PARASITE;
        }
        return type;
    }

    private List<EntityLiving> buildList(World world, EntityPlayer player, Type type, int phase) {
        Random rand = world.rand;
        boolean hardMode = postGame || phase > 12;
        List<EntityLiving> spawnlist = Lists.newArrayList();
        switch (type) {
            case ALLY:
                for (int i = 0; i < (phase + 1) * 2.5; i++)
                    try {
                        EntityTF2Character entity = TF2Class.getRandomClass(c -> c != TF2Class.SPY).createEntity(world);
                        world.getScoreboard().addPlayerToTeam(entity.getCachedUniqueIdString(), player.getTeam().getName());
                        entity.setEntTeam(player.getTeam().getName().equals("RED") ? 0 : 1);
                        spawnlist.add(entity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                break;
            case ENEMY:
                for (int i = 0; i < (phase + 1) * 2.5; i++)
                    try {
                        EntityTF2Character entity = TF2Class.getRandomClass(c -> c != TF2Class.SPY).createEntity(world);
                        world.getScoreboard().addPlayerToTeam(entity.getCachedUniqueIdString(), player.getTeam().getName().equals("RED") ? "BLU" : "RED");
                        entity.setEntTeam(player.getTeam().getName().equals("RED") ? 1 : 0);
                        spawnlist.add(entity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                break;
            case PARASITE:
                for (int i = 0; i < (phase + 1) * 0.5; i++) {
                    int r = rand.nextInt(7);
                    if (r == 0) spawnlist.add(hardMode ? new EntityShycoAdapted(world) : new EntityShyco(world));
                    else if (r == 1) spawnlist.add(hardMode ? new EntityCanraAdapted(world) : new EntityCanra(world));
                    else if (r == 2) spawnlist.add(hardMode ? new EntityNoglaAdapted(world) : new EntityNogla(world));
                    else if (r == 3) spawnlist.add(hardMode ? new EntityHullAdapted(world) : new EntityHull(world));
                    else if (r == 4) spawnlist.add(hardMode ? new EntityEmanaAdapted(world) : new EntityEmana(world));
                    else if (r == 5) spawnlist.add(hardMode ? new EntityBanoAdapted(world) : new EntityBano(world));
                    else if (r == 6) spawnlist.add(hardMode ? new EntityRanracAdapted(world) : new EntityRanrac(world));
                }
                for (int i = 0; i < phase * 1.5; i++) spawnlist.add(hardMode ? new EntityFerHuman(world) : new EntityInfHuman(world));
                break;
            case ZOMBIE:
                if (postGame) for (int i = 0; i < (phase + 1); i++) spawnlist.add(new EntityZombieNurse(world));
                else {
                    spawnlist.add(new EntityZombieNurse(world));
                    if (hardMode) spawnlist.add(new EntityZombieNurse(world));
                    for (int i = 0; i < (phase + 1); i++) {
                        int r = rand.nextInt(3);
                        if (r == 0) spawnlist.add(new EntitySwatZombie(world));
                        else if (r == 1) spawnlist.add(new EntityZombieMechanic(world));
                        else if (r == 2) spawnlist.add(new EntityZombieTechnician(world));
                    }
                }
                for (int i = 0; i < (phase + 1) * 2.5; i++) {
                    if (hardMode) {
                        int r = rand.nextInt(9);
                        if (r == 0) spawnlist.add(new MutantZombieEntity(world));
                        else if (r == 1) spawnlist.add(new EntityZombieMushroom(world));
                        else if (r == 2) spawnlist.add(new EntityFoglet(world));
                        else if (r == 3) spawnlist.add(new EntityUndeadSwine(world));
                        else if (r == 5) spawnlist.add(new EntityZombieFireman(world));
                        else if (r == 6) spawnlist.add(new EntityReaver(world));
                        else spawnlist.add(new EntityZombie(world));
                    } else spawnlist.add(new EntityZombie(world));
                };
                break;
            case MUTANT:
                for (int i = 0; i < (phase + 1) * 2.5; i++) {
                    int r = rand.nextInt(15);
                    if (r < 2) spawnlist.add(new EntityVespa(world));
                    else if (r < 6) spawnlist.add(new EntityPtera(world));
                    else if (r == 7) spawnlist.add(new EntitySalamander(world));
                    else if (r == 8) spawnlist.add(new EntitySludgeLord(world));
                    else if (r == 9) spawnlist.add(new EntityBoneWorm(world));
                    else if (r == 10) spawnlist.add(new EntityWendigo(world));
                    else if (r == 11) spawnlist.add(new EntityBanshee(world));
                    else if (r == 12) spawnlist.add(new EntityAvaton(world));
                    else spawnlist.add(new EntityWeta(world));
                }
                break;
            case NONE:
                break;
        }
        return spawnlist;
    }

    private ResourceLocation getSound(Type type) {
        if (type == Type.ALLY) return Constants.TF_ALLY_SOUND;
        if (type == Type.ENEMY) return Constants.TF_ENEMY_SOUND;
        return Hordes.HORDE_SOUND;
    }

}