package net.smileycorp.ldoh.common.events;

import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.hordes.common.event.HordeBuildSpawntableEvent;
import net.smileycorp.hordes.common.event.HordeEndEvent;
import net.smileycorp.hordes.common.event.HordeStartEvent;
import net.smileycorp.hordes.common.event.HordeStartWaveEvent;
import net.smileycorp.hordes.common.hordeevent.HordeSpawnEntry;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.Apocalypse;
import net.smileycorp.ldoh.common.capabilities.IMiniRaid;
import net.smileycorp.ldoh.common.capabilities.IUnburiedSpawner;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.smileycorp.ldoh.common.util.EnumTFClass;

import com.Fishmod.mod_LavaCow.entities.tameable.EntityUnburied;
import com.dhanantry.scapeandrunparasites.entity.ai.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityCrux;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityHeed;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityButhol;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityDorpa;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfCow;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityFlog;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityGanro;

public class SpawnerEvents {

	//capability manager
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		//spawner instance to spawn unburied in the caves around players
		if (!entity.hasCapability(LDOHCapabilities.UNBURIED_SPAWNER, null) && entity instanceof EntityPlayer &!(entity instanceof FakePlayer)) {
			event.addCapability(ModDefinitions.getResource("UnburiedSpawner"), new IUnburiedSpawner.Provider((EntityPlayer) entity));
		}
		//spawner instance for mini raid events
		if (!entity.hasCapability(LDOHCapabilities.MINI_RAID, null) && entity instanceof EntityPlayer &!(entity instanceof FakePlayer)) {
			event.addCapability(ModDefinitions.getResource("MiniRaid"), new IMiniRaid.Provider());
		}
	}

	//Player ticks
	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		if (event.phase == Phase.END) {
			EntityPlayer player = event.player;
			World world = player.world;
			Random rand = world.rand;
			if (!world.isRemote) {
				//spawn unburied when player is underground
				if (player.hasCapability(LDOHCapabilities.UNBURIED_SPAWNER, null) && player.ticksExisted % 60==0) {
					IUnburiedSpawner spawner = player.getCapability(LDOHCapabilities.UNBURIED_SPAWNER, null);
					int y = (int) Math.floor(player.getPosition().getY());
					Chunk chunk = world.getChunkFromBlockCoords(player.getPosition());
					if (spawner.canSpawnEntity()) {
						//make sure it's underground and try to prevent spawning in buildings
						if (y < Math.max(chunk.getLowestHeight(), 30) &! world.canBlockSeeSky(player.getPosition()) && rand.nextInt(Math.max(y, 25)) <= 18) {
							Vec3d dir = DirectionUtils.getRandomDirectionVecXZ(rand);
							BlockPos pos = new BlockPos(player.posX + dir.x*(rand.nextInt(5)+2), player.posY, player.posZ + dir.z*(rand.nextInt(5)+2));
							//check spawn location is valid
							if (!(world.isAirBlock(pos) && world.isAirBlock(pos.up()) && world.isBlockFullCube(pos.down())
									&& DirectionUtils.isBrightnessAllowed(world, pos, 7, 0))) {
								for (int j = -5; j <6; j++) {
									if (world.isAirBlock(pos.up(j)) && world.isAirBlock(pos.up(j+1)) && world.isBlockFullCube(pos.up(j-1))) {
										pos = pos.up(j);
										break;
									}
								}
							}
							//make sure area is dark, then spawn entity
							if (world.isAirBlock(pos) && world.isAirBlock(pos.up()) && world.isBlockFullCube(pos.down())
									&& DirectionUtils.isBrightnessAllowed(world, pos, 7, 0) &! world.canBlockSeeSky(pos)) {
								EntityUnburied entity = new EntityUnburied(world);
								entity.setPosition(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
								entity.onInitialSpawn(world.getDifficultyForLocation(pos), null);
								world.spawnEntity(entity);
								spawner.addEntity(entity);
							}
						}
					}
				}
				//Mini Raids
				if (player.hasCapability(LDOHCapabilities.MINI_RAID, null)) {
					IMiniRaid raid = player.getCapability(LDOHCapabilities.MINI_RAID, null);
					//spawn the raid if the time is right
					if (raid.shouldSpawnRaid(player)) raid.spawnRaid(player);
				}
			}
		}
	}

	//disables parasite spawns before set days
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onSpawn(LivingSpawnEvent.CheckSpawn event) {
		World world = event.getWorld();
		EntityLivingBase entity = event.getEntityLiving();
		if (!world.isRemote) {
			int day = (int) Math.floor(world.getWorldTime()/24000);
			if (entity instanceof EntityParasiteBase && (day < 50)) event.setResult(Result.DENY);
		}
	}

	@SubscribeEvent
	public void hordeStart(HordeStartEvent event) {
		int day = event.getDay();
		if (day > 100 && day%100 == 0) {
			if (event.getEntityPlayer().getTeam() != null) {
				if (event.getEntityPlayer().getTeam().getName().equals("RED") || event.getEntityPlayer().getTeam().getName().equals("BLU")) {
					event.setMessage("message.hundreddayz.TFHordeStart");
				}
			}
		}
	}

	@SubscribeEvent
	public void hordeStartWave(HordeStartWaveEvent event) {
		int day = event.getDay();
		if (day > 100 && day%100 == 0) {
			if (event.getEntityPlayer().getTeam() != null) {
				if (event.getEntityPlayer().getTeam().getName().equals("RED") || event.getEntityPlayer().getTeam().getName().equals("BLU")) {
					event.setSound(ModDefinitions.getResource("tf_enemy"));
				}
			}
		}
	}

	@SubscribeEvent
	public void hordeStart(HordeEndEvent event) {
		int day = event.getDay();
		if (day > 100 && day%100 == 0) {
			if (event.getEntityPlayer().getTeam() != null) {
				if (event.getEntityPlayer().getTeam().getName().equals("RED") || event.getEntityPlayer().getTeam().getName().equals("BLU")) {
					event.setMessage("message.hundreddayz.TFHordeEnd");
				}
			}
		}
	}

	@SubscribeEvent
	public void hordeBuildSpawntable(HordeBuildSpawntableEvent event) {
		int day = event.getDay();
		if (day > 100 && day % 100 == 0) {
			if (event.getEntityPlayer().getTeam() != null) {
				if (event.getEntityPlayer().getTeam().getName().equals("RED") || event.getEntityPlayer().getTeam().getName().equals("BLU")) {
					event.spawntable.clear();
					for (EnumTFClass tfclass : EnumTFClass.values()) if (tfclass != EnumTFClass.SPY) event.spawntable.addEntry(new HordeSpawnEntry(tfclass.getEntityClass()) , 1);
				}
			}
		} if (day > 100 && event.getEntityWorld().rand.nextInt(2) == 0) {
			event.spawntable.clear();
			event.spawntable.addEntry(new HordeSpawnEntry(EntityMudo.class), 75);
			event.spawntable.addEntry(new HordeSpawnEntry(EntityInfHuman.class), 25);
			event.spawntable.addEntry(new HordeSpawnEntry(EntityInfCow.class), 10);
			event.spawntable.addEntry(new HordeSpawnEntry(EntityButhol.class), 10);
			event.spawntable.addEntry(new HordeSpawnEntry(EntityFlog.class), 5);
			event.spawntable.addEntry(new HordeSpawnEntry(EntityDorpa.class), 1);
			event.spawntable.addEntry(new HordeSpawnEntry(EntityHeed.class), 1);
			event.spawntable.addEntry(new HordeSpawnEntry(EntityCrux.class), 1);
			event.spawntable.addEntry(new HordeSpawnEntry(EntityGanro.class), 1);
			for (Entry<Class<? extends EntityParasiteBase>, Integer> entry : Apocalypse.adaptedtable.getTable())
				event.spawntable.addEntry(new HordeSpawnEntry(entry.getKey()), 2);
		}
	}

}
