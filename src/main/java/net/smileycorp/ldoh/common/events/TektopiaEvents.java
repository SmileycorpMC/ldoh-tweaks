package net.smileycorp.ldoh.common.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.hordes.common.event.InfectionDeathEvent;
import net.smileycorp.hordes.infection.HordesInfection;
import net.smileycorp.hordes.infection.InfectionRegister;
import net.smileycorp.ldoh.common.entity.EntityLDOHArchitect;
import net.smileycorp.ldoh.common.entity.EntityLDOHTradesman;
import net.tangotek.tektopia.entities.EntityArchitect;
import net.tangotek.tektopia.entities.EntityNecromancer;
import net.tangotek.tektopia.entities.EntityTradesman;
import net.tangotek.tektopia.entities.EntityVillagerTek;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfVillager;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;

public class TektopiaEvents {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		Entity entity = event.getEntity();
		if (entity instanceof EntityNecromancer) event.setCanceled(true);
		//replaces architect with our own version that has our modified trades
		if (entity instanceof EntityArchitect &! (entity instanceof EntityLDOHArchitect)) {
			EntityLDOHArchitect architect = new EntityLDOHArchitect(world, (EntityArchitect) entity);
			architect.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), (IEntityLivingData)null);
			world.spawnEntity(architect);
			event.setCanceled(true);
		}
		//replaces tradesman with our own version that has our modified trades
		else if (entity instanceof EntityTradesman &! (entity instanceof EntityLDOHTradesman)) {
			EntityLDOHTradesman tradesman = new EntityLDOHTradesman(world, (EntityTradesman) entity);
			tradesman.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), (IEntityLivingData)null);
			world.spawnEntity(tradesman);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDeath(LivingDeathEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		if (!world.isRemote) {
			//replaces the tektopia villager with an infected villager if killed by a rupter
			if (event.getSource().getTrueSource() instanceof EntityParasiteBase) {
				if (entity instanceof EntityVillagerTek) {
					EntityInfVillager newentity = new EntityInfVillager(world);
					newentity.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
					newentity.renderYawOffset=entity.renderYawOffset;
					newentity.setPosition(entity.posX, entity.posY, entity.posZ);
					entity.setDead();
					world.spawnEntity(newentity);
					SRPWorldData data = SRPWorldData.get(world);
					data.setCurrentV(data.getCurrentV() + 1);
					data.markDirty();
				}
			}
		}
	}

	//hooks into the hordes infection event
	@SubscribeEvent
	public void onInfect(InfectionDeathEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		if (entity instanceof EntityVillagerTek) {
			//spawns a tf2 zombie in the place of the dead merc
			EntityZombieVillager zombie = new EntityZombieVillager(world);
			world.spawnEntity(zombie);
			zombie.setPosition(entity.posX, entity.posY, entity.posZ);
			zombie.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
			entity.setDead();
		}
	}

	@SubscribeEvent
	public void onDamage(LivingDamageEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		Entity attacker = event.getSource().getImmediateSource();
		World world = entity.world;
		if (!world.isRemote) {
			if (InfectionRegister.canCauseInfection(attacker)) {
				//sets zombie damage to a fixed 1.5 hearts
				event.setAmount(3f);
				if (entity instanceof EntityVillagerTek) {
					//gives the infection effect
					entity.addPotionEffect(new PotionEffect(HordesInfection.INFECTED, 10000, 0));
				}
			}
		}
	}

}
