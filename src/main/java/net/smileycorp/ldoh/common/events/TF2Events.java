package net.smileycorp.ldoh.common.events;

import java.util.ArrayList;
import java.util.List;

import mariot7.xlfoodmod.init.ItemListxlfoodmod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
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
import net.smileycorp.followme.common.event.FollowUserEvent;
import net.smileycorp.hordes.common.event.HordeSpawnEntityEvent;
import net.smileycorp.hordes.common.event.InfectionDeathEvent;
import net.smileycorp.hordes.infection.HordesInfection;
import net.smileycorp.hordes.infection.InfectionRegister;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.ICuring;
import net.smileycorp.ldoh.common.capabilities.IFollowers;
import net.smileycorp.ldoh.common.capabilities.IHunger;
import net.smileycorp.ldoh.common.capabilities.ISpawnTracker;
import net.smileycorp.ldoh.common.capabilities.IVillageData;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.smileycorp.ldoh.common.entity.EntityTF2Zombie;
import net.smileycorp.ldoh.common.entity.ai.AIModifiedMedigun;
import net.smileycorp.ldoh.common.util.EnumTFClass;
import net.smileycorp.ldoh.common.util.ModUtils;
import net.tangotek.tektopia.VillageManager;
import rafradek.TF2weapons.entity.ai.EntityAINearestChecked;
import rafradek.TF2weapons.entity.ai.EntityAISpotTarget;
import rafradek.TF2weapons.entity.ai.EntityAIUseMedigun;
import rafradek.TF2weapons.entity.building.EntityBuilding;
import rafradek.TF2weapons.entity.building.EntitySentry;
import rafradek.TF2weapons.entity.mercenary.EntityMedic;
import rafradek.TF2weapons.entity.mercenary.EntitySpy;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;
import rafradek.TF2weapons.inventory.InventoryLoadout;
import rafradek.TF2weapons.item.ItemAmmo;
import rafradek.TF2weapons.item.ItemFromData;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;

public class TF2Events {

	//capability manager
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		//gives tf2 mercs hunger
		if (!entity.hasCapability(LDOHCapabilities.HUNGER, null) && entity instanceof EntityTF2Character) {
			if (!((EntityTF2Character)entity).isRobot()) event.addCapability(ModDefinitions.getResource("Hunger"), new IHunger.Provider());
		}
		//give spies baguettes
		if (!entity.hasCapability(LDOHCapabilities.SPAWN_TRACKER, null) && entity instanceof EntitySpy) {
			if (!((EntitySpy)entity).isRobot())  event.addCapability(ModDefinitions.getResource("SpawnProvider"), new ISpawnTracker.Provider());
		}
		//give engineer buildings persistence
		if (!entity.hasCapability(LDOHCapabilities.SPAWN_TRACKER, null) && entity instanceof EntityBuilding) {
			event.addCapability(ModDefinitions.getResource("SpawnProvider"), new ISpawnTracker.Provider());
		}
		//give medics ability to cure
		if (!entity.hasCapability(LDOHCapabilities.CURING, null) && entity instanceof EntityMedic) {
			event.addCapability(ModDefinitions.getResource("Curing"), new ICuring.Provider());
		}
		//give mercs exhaustion/sleeping
		/*if (!entity.hasCapability(LDOHCapabilities.EXHAUSTION, null) && entity instanceof EntityTF2Character) {
			if (!((EntityTF2Character)entity).isRobot()) event.addCapability(ModDefinitions.getResource("Exhaustion"), new IExhaustion.Provider());
		}*/
		//give mercs home village
		if (!entity.hasCapability(LDOHCapabilities.VILLAGE_DATA, null) && entity instanceof EntityTF2Character) {
			event.addCapability(ModDefinitions.getResource("VillageData"), new IVillageData.Provider());
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

	@SubscribeEvent
	public void livingTick(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		//hunger tick
		if (entity.hasCapability(LDOHCapabilities.HUNGER, null) && entity instanceof EntityTF2Character) {
			if (!((EntityTF2Character) entity).isRobot()) entity.getCapability(LDOHCapabilities.HUNGER, null).onUpdate((EntityLiving) entity);
		}
		if (entity instanceof EntityTF2Character &! world.isRemote) {
			EntityTF2Character merc = (EntityTF2Character) entity;
			EnumTFClass tfClass = EnumTFClass.getClass(merc);
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
				//gifting weapons
				if (tfClass.canUseItem(stack)) {
					InventoryLoadout loadout = merc.loadout;
					for (int i = 0; i < 4; i++) {
						if (merc.loadout.getStackInSlot(i).isEmpty() && ItemFromData.isItemOfClassSlot(ItemFromData.getData(stack), i, tfClass.getClassName())) {
							loadout.insertItem(i, stack, false);
							item.setDead();
						}
					}
				}
				if (merc.hasCapability(LDOHCapabilities.HUNGER, null) &! merc.isRobot()) {
					stack = merc.getCapability(LDOHCapabilities.HUNGER, null).tryPickupFood(stack, merc);
				}
				if (merc.hasCapability(LDOHCapabilities.CURING, null)) {
					stack = merc.getCapability(LDOHCapabilities.CURING, null).tryPickupSyringe(stack, merc);
				}
				if (stack.getCount() == 0) item.setDead();
			}
			if (merc.isRobot() && merc.isPotionActive(SRPPotions.COTH_E)) {
				merc.removePotionEffect(SRPPotions.COTH_E);
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
					if(!((EntityTF2Character) entity).isRobot() &! entity.isPotionActive(HordesInfection.INFECTED)) {
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
		if (entity instanceof EntityTF2Character) {
			//check the entity isn't a robot
			if(!((EntityTF2Character) entity).isRobot()) {
				//spawns a tf2 zombie in the place of the dead merc
				EntityTF2Zombie zombie = new EntityTF2Zombie((EntityTF2Character)entity);
				world.spawnEntity(zombie);
				zombie.setPosition(entity.posX, entity.posY, entity.posZ);
				zombie.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
				entity.setDead();
				event.setResult(Result.DENY);
			}
		}
	}

	@SuppressWarnings({ })
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		Entity entity = event.getEntity();
		if (!world.isRemote) {
			if (entity instanceof EntitySentry) {
				EntitySentry sentry = (EntitySentry) entity;
				sentry.targetTasks.taskEntries.clear();
				sentry.targetTasks.addTask(2, new EntityAISpotTarget(sentry, EntityLivingBase.class, true, true,
						(e) -> ModUtils.canTarget(sentry, e), false, true));
				world.getScoreboard().addPlayerToTeam(sentry.getCachedUniqueIdString(), sentry.getOwner().getTeam().getName());
			}
			if (entity instanceof EntityTF2Character) {
				EntityTF2Character merc = (EntityTF2Character) entity;
				//makes tf2 mercs avoid zombies more
				//merc.tasks.addTask(1, new EntityAIStayInVillage(merc));
				merc.tasks.addTask(3, new EntityAIAvoidEntity<EntityMob>(merc, EntityMob.class, (e)->ModUtils.canTarget(merc, e), 5.0F, 0.6D, 0.6D));
				//redo targeting ai
				merc.targetTasks.taskEntries.clear();
				if (entity instanceof EntityMedic) {
					//medic heal targeting
					EntityAIBase ai = new EntityAINearestChecked(merc, EntityLivingBase.class, true, false, (e) -> ModUtils.shouldHeal(merc, e), false, true) {
						@Override
						public boolean shouldExecute() {
							return ModUtils.shouldHeal(merc, targetEntity);
						}
					};
					merc.targetTasks.addTask(1, ai);
					merc.targetTasks.addTask(2, new EntityAIHurtByTarget(merc, true));
					merc.targetTasks.addTask(3, new EntityAINearestChecked(merc, EntityLivingBase.class, true, false, (e) -> ModUtils.canTarget(merc, e), true, false));
				} else {
					merc.targetTasks.addTask(1, new EntityAIHurtByTarget(merc, true));
					merc.targetTasks.addTask(2, new EntityAINearestChecked(merc, EntityLivingBase.class, true, false, (e) -> ModUtils.canTarget(merc, e), true, false));
				}
				if (entity instanceof EntitySpy && entity.hasCapability(LDOHCapabilities.SPAWN_TRACKER, null)) {
					ISpawnTracker tracker = entity.getCapability(LDOHCapabilities.SPAWN_TRACKER, null);
					if (!tracker.isSpawned()) {
						if (entity.hasCapability(LDOHCapabilities.HUNGER, null)) {
							entity.getCapability(LDOHCapabilities.HUNGER, null).setFoodStack(new ItemStack(ItemListxlfoodmod.baguette, 8));
						}
						tracker.setSpawned(true);
					}
				} else if (entity instanceof EntityMedic) {
					List<EntityAIBase> tasks = new ArrayList<EntityAIBase>();
					for (EntityAITaskEntry task : ((EntityTF2Character) entity).tasks.taskEntries) {
						EntityAIBase ai = task.action;
						if (ai instanceof EntityAIUseMedigun &!(ai instanceof AIModifiedMedigun)) tasks.add(ai);
					}
					if (!tasks.isEmpty()) {
						for (EntityAIBase task : tasks)((EntityTF2Character) entity).tasks.removeTask(task);
						((EntityTF2Character) entity).tasks.addTask(3, new AIModifiedMedigun(merc));
					}
				}
				//sync capability data to clients
				if (merc.hasCapability(LDOHCapabilities.HUNGER, null)) merc.getCapability(LDOHCapabilities.HUNGER, null).syncClients(merc);
				if (merc.hasCapability(LDOHCapabilities.CURING, null)) merc.getCapability(LDOHCapabilities.CURING, null).syncClients(merc);
				//if (entity.hasCapability(LDOHCapabilities.EXHAUSTION, null)) entity.getCapability(LDOHCapabilities.EXHAUSTION, null).syncClients(merc);
				//fetch closest village to entities that were spawned in one
				if (entity.hasCapability(LDOHCapabilities.VILLAGE_DATA, null)) {
					IVillageData cap = entity.getCapability(LDOHCapabilities.VILLAGE_DATA, null);
					if (cap.shouldHaveVillage() &! cap.hasVillage()) {
						VillageManager villages = VillageManager.get(world);
						cap.setVillage(villages);
					}
				}

				//give persistence to tf2 buildings
			} else if (entity instanceof EntityBuilding) {
				if (entity instanceof EntitySpy && entity.hasCapability(LDOHCapabilities.SPAWN_TRACKER, null)) {
					ISpawnTracker tracker = entity.getCapability(LDOHCapabilities.SPAWN_TRACKER, null);
					if (!tracker.isSpawned()) {
						((EntityBuilding) entity).enablePersistence();
						tracker.setSpawned(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void playerTrack(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof EntityLiving && event.getEntityPlayer() instanceof EntityPlayerMP) {
			EntityLiving entity = (EntityLiving) event.getTarget();
			if (entity.hasCapability(LDOHCapabilities.HUNGER, null)) entity.getCapability(LDOHCapabilities.HUNGER, null).syncClient(entity, (EntityPlayerMP) event.getEntityPlayer());
			if (entity.hasCapability(LDOHCapabilities.CURING, null)) entity.getCapability(LDOHCapabilities.CURING, null).syncClient(entity, (EntityPlayerMP) event.getEntityPlayer());
			//if (entity.hasCapability(LDOHCapabilities.EXHAUSTION, null)) entity.getCapability(LDOHCapabilities.EXHAUSTION, null).syncClient(entity, (EntityPlayerMP) event.getEntityPlayer());
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

	@SubscribeEvent
	public void onFollow(FollowUserEvent event) {
		EntityLivingBase user = event.user;
		EntityLivingBase entity = event.getEntityLiving();
		if (user.hasCapability(LDOHCapabilities.FOLLOWERS, null) && entity instanceof EntityLiving) {
			IFollowers cap = user.getCapability(LDOHCapabilities.FOLLOWERS, null);
			if (cap.isFollowing((EntityLiving)entity)) {
				cap.stopFollowing((EntityLiving)entity);
				event.setCanceled(true);
			}
		}
	}
}
