package net.smileycorp.ldoh.common.events;

import mariot7.xlfoodmod.init.ItemListxlfoodmod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.smileycorp.hordes.common.event.HordeSpawnEntityEvent;
import net.smileycorp.hordes.common.event.InfectionDeathEvent;
import net.smileycorp.hordes.infection.HordesInfection;
import net.smileycorp.hordes.infection.InfectionRegister;
import net.smileycorp.ldoh.common.ModContent;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.IHunger;
import net.smileycorp.ldoh.common.capabilities.ISpawnTracker;
import net.smileycorp.ldoh.common.entity.EntityTFZombie;
import net.smileycorp.ldoh.common.util.ModUtils;
import net.tangotek.tektopia.entities.EntityNecromancer;
import rafradek.TF2weapons.entity.mercenary.EntitySpy;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;
import rafradek.TF2weapons.item.ItemAmmo;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;

public class TF2Events {

	//capability manager
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		//gives tf2 mercs hunger
		if (!entity.hasCapability(ModContent.HUNGER, null) && entity instanceof EntityTF2Character) {
			event.addCapability(ModDefinitions.getResource("Hunger"), new IHunger.Provider());
		}

		if (!entity.hasCapability(ModContent.SPAWN_TRACKER, null) && entity instanceof EntitySpy) {
			event.addCapability(ModDefinitions.getResource("SpawnProvider"), new ISpawnTracker.Provider());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDeath(LivingDeathEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		if (!world.isRemote) {
			//replaces the tf2 character with an infected human if killed by a rupter
			if (event.getSource().getTrueSource() instanceof EntityParasiteBase) {
				if (entity instanceof EntityTF2Character) {
					if (!((EntityTF2Character) entity).isRobot()) {
						EntityInfHuman newentity = new EntityInfHuman(world);
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
			//adds the player to a team after killing the opposite merc
			if (event.getSource().getTrueSource() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
				if (entity instanceof EntityTF2Character) {
					if (!((EntityTF2Character) entity).isRobot()) {
						if (player.getTeam() == null) {
							if (entity.getTeam() == world.getScoreboard().getTeam("RED")) {
								ModUtils.addPlayerToTeam(player, "BLU");
							} else if (entity.getTeam() == world.getScoreboard().getTeam("BLU")) {
								ModUtils.addPlayerToTeam(player, "RED");
							}
						}
					}
				}
			}
		}
	}

	//make entities that can break blocks do so
	@SubscribeEvent
	public void livingTick(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		if (entity.hasCapability(ModContent.HUNGER, null)) {
			entity.getCapability(ModContent.HUNGER, null).onUpdate((EntityLiving) entity);
		}
		if (entity instanceof EntityTF2Character &! world.isRemote) {
			EntityTF2Character merc = (EntityTF2Character) entity;
			//gifting ammo and food to tf2 characters
			for(EntityItem item : world.getEntitiesWithinAABB(EntityItem.class, merc.getEntityBoundingBox())) {
				ItemStack stack = item.getItem();
				if (stack.getItem() instanceof ItemAmmo) {
					//check entity ammo slots to see if it can pick the items up
					ItemStackHandler ammo = merc.refill;
					if (ammo != null) {
						ItemStack ammostack = ammo.getStackInSlot(0);
						if (ammostack==null || ammostack.isEmpty()) {
							ammo.setStackInSlot(0, stack);
							item.setDead();
						} else if (ammostack.getItem() == stack.getItem()) {
							int count = ammostack.getCount() + stack.getCount();
							if (count > 64) {
								ItemStack ret = ammostack.copy();
								ret.setCount(64);
								ammo.setStackInSlot(0, ret);
								stack.setCount(count - 64);
							}
							else {
								ItemStack ret = ammostack.copy();
								ret.setCount(count);
								ammo.setStackInSlot(0, ret);
							}
						}
					}
				}
				if (merc.hasCapability(ModContent.HUNGER, null)) {
					stack = merc.getCapability(ModContent.HUNGER, null).tryPickupFood(stack, merc);
				}
				if (stack.getCount() == 0) item.setDead();
			}
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
				if (entity instanceof EntityTF2Character) {
					//gives the infection effect to non-robots
					if(!((EntityTF2Character) entity).isRobot()) {
						entity.addPotionEffect(new PotionEffect(HordesInfection.INFECTED, 10000, 0));
					}
				}
			}
		}
	}

	//hooks into the hordes infection event
	@SubscribeEvent
	public void onInfect(InfectionDeathEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		//lets players kill tf2 mobs to stop infection
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			event.setCanceled(true);
			return;
		}
		if (entity instanceof EntityTF2Character) {
			//check the entity isn't a robot
			if(!((EntityTF2Character) entity).isRobot()) {
				//spawns a tf2 zombie in the place of the dead merc
				EntityTFZombie zombie = new EntityTFZombie((EntityTF2Character)entity);
				world.spawnEntity(zombie);
				zombie.setPosition(entity.posX, entity.posY, entity.posZ);
				zombie.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
				entity.setDead();
				event.setResult(Result.DENY);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		Entity entity = event.getEntity();
		if (entity instanceof EntityNecromancer) event.setCanceled(true);
		if (!world.isRemote) {
			//makes tf2 mercs avoid zombies more
			if (entity instanceof EntityTF2Character) {
				EntityTF2Character merc = (EntityTF2Character) entity;
				merc.tasks.addTask(3, new EntityAIAvoidEntity(merc, EntityMob.class, 5.0F, 0.6D, 0.6D));
				if (entity instanceof EntitySpy && entity.hasCapability(ModContent.SPAWN_TRACKER, null)) {
					ISpawnTracker tracker = entity.getCapability(ModContent.SPAWN_TRACKER, null);
					if (!tracker.isSpawned()) {
						if (entity.hasCapability(ModContent.HUNGER, null)) {
							entity.getCapability(ModContent.HUNGER, null).setFoodStack(new ItemStack(ItemListxlfoodmod.baguette, 8));
						}
						tracker.setSpawned(true);
					}
				}
				if (merc.hasCapability(ModContent.HUNGER, null)) merc.getCapability(ModContent.HUNGER, null).syncClients(merc);
			}
		}
	}

	@SubscribeEvent
	public void playerTrack(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof EntityLiving && event.getEntityPlayer() instanceof EntityPlayerMP) {
			EntityLiving entity = (EntityLiving) event.getTarget();
			if (entity.hasCapability(ModContent.HUNGER, null)) entity.getCapability(ModContent.HUNGER, null).syncClient(entity, (EntityPlayerMP) event.getEntityPlayer());
		}
	}

	@SubscribeEvent
	public void hordeSpawn(HordeSpawnEntityEvent event) {
		Entity entity = event.entity;
		World world = entity.world;
		EntityPlayer player = event.getEntityPlayer();
		if (!world.isRemote) {
			if (entity instanceof EntityTF2Character) {
				world.getScoreboard().addPlayerToTeam(entity.getCachedUniqueIdString(), player.getTeam().getName() == "RED" ? "BLU" : "RED");
				((EntityTF2Character) entity).setEntTeam(player.getTeam().getName() == "RED" ? 1 : 0);
			}
		}
	}
}
