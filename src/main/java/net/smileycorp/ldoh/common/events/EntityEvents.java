package net.smileycorp.ldoh.common.events;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.hordes.common.event.HordeSpawnEntityEvent;
import net.smileycorp.ldoh.common.ModContent;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.IBreakBlocks;
import net.smileycorp.ldoh.common.capabilities.ISpawnTracker;
import net.smileycorp.ldoh.common.entity.EntityCrawlingHusk;
import net.smileycorp.ldoh.common.entity.EntityCrawlingZombie;
import net.smileycorp.ldoh.common.entity.EntityDummyHusk0;
import net.smileycorp.ldoh.common.entity.EntityDummyHusk1;
import net.smileycorp.ldoh.common.entity.EntityDummyHusk2;
import net.smileycorp.ldoh.common.entity.EntityDummyZombie0;
import net.smileycorp.ldoh.common.entity.EntityDummyZombie1;
import net.smileycorp.ldoh.common.entity.EntityDummyZombie2;
import net.smileycorp.ldoh.common.entity.EntityLDOHArchitect;
import net.smileycorp.ldoh.common.entity.EntityLDOHTradesman;
import net.smileycorp.ldoh.common.entity.EntityTFZombie;
import net.smileycorp.ldoh.common.entity.EntityZombieNurse;
import net.smileycorp.ldoh.common.util.IDummyZombie;
import net.smileycorp.ldoh.common.util.ModUtils;
import net.tangotek.tektopia.entities.EntityArchitect;
import net.tangotek.tektopia.entities.EntityNecromancer;
import net.tangotek.tektopia.entities.EntityTradesman;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import rafradek.TF2weapons.TF2weapons;
import rafradek.TF2weapons.entity.building.EntitySentry;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;

import com.Fishmod.mod_LavaCow.entities.EntitySludgeLord;
import com.Fishmod.mod_LavaCow.entities.flying.EntityVespa;
import com.animania.api.interfaces.IAnimaniaAnimal;
import com.dhanantry.scapeandrunparasites.entity.ai.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfVillager;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import com.legacy.wasteland.world.WastelandWorld;

public class EntityEvents {

	//capability manager
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		//track whether zombies or crawling zombies were loaded from world data or not
		if (!entity.hasCapability(ModContent.SPAWN_TRACKER, null) && (entity.getClass() == EntityZombie.class || entity instanceof IDummyZombie)) {
			event.addCapability(ModDefinitions.getResource("SpawnProvider"), new ISpawnTracker.Provider());
		}
		//lets entities break blocks if the capability is set to enabled
		if (!entity.hasCapability(ModContent.BLOCK_BREAKING, null) && entity instanceof EntityLiving) {
			event.addCapability(ModDefinitions.getResource("BlockBreaker"), new IBreakBlocks.Provider((EntityLiving) entity));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		Entity entity = event.getEntity();
		if (entity instanceof EntityNecromancer) event.setCanceled(true);
		if (!world.isRemote) {
			//replacing zombies with rare spawns
			if (entity.hasCapability(ModContent.SPAWN_TRACKER, null)) {
				if(!entity.getCapability(ModContent.SPAWN_TRACKER, null).isSpawned()) {
					if (entity.getClass() == EntityZombie.class) {
						EntityZombie zombie = (EntityZombie) entity;
						EntityMob newentity = null;
						Random rand = world.rand;
						//select random number first to allow for proper weighting
						int randInt = rand.nextInt(100);
						if (randInt < 3) {
							newentity = new EntityTFZombie(world);
						} else if (randInt == 3) {
							newentity = new EntityZombieNurse(world);
						}  else if (randInt < 15) {
							newentity = world.getBiome(entity.getPosition()) == WastelandWorld.apocalypse_desert ?
									new EntityCrawlingHusk(world) : new EntityCrawlingZombie(world);
						}
						//turns zombies into husks in a desert
						else if (world.getBiome(entity.getPosition()) == WastelandWorld.apocalypse_desert) {
							newentity = new EntityHusk(world);
						}
						//sets up new entity
						if (newentity != null) {
							newentity.renderYawOffset=zombie.renderYawOffset;
							newentity.setPosition(entity.posX, entity.posY, entity.posZ);
							if (zombie.isNoDespawnRequired()) newentity.enablePersistence();
							entity.setDead();
							newentity.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
							world.spawnEntity(newentity);
							event.setCanceled(true);
							ModUtils.setEntitySpeed((EntityMob) entity);
						} else {
							ModUtils.setEntitySpeed((EntityMob) entity);
							entity.getCapability(ModContent.SPAWN_TRACKER, null).setSpawned(true);
						}
						//replace crawling zombies with their husk counterpart in deserts
					} else if (entity.getClass() == EntityCrawlingZombie.class) {
						if (world.getBiome(entity.getPosition()) == WastelandWorld.apocalypse_desert) {
							EntityCrawlingZombie zombie = (EntityCrawlingZombie) entity;
							EntityCrawlingHusk newentity = new EntityCrawlingHusk(world);
							newentity.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
							newentity.renderYawOffset=zombie.renderYawOffset;
							newentity.setPosition(entity.posX, entity.posY, entity.posZ);
							if (zombie.isNoDespawnRequired()) newentity.enablePersistence();
							entity.setDead();
							world.spawnEntity(newentity);
							event.setCanceled(true);
							ModUtils.setEntitySpeed((EntityMob) entity);
						} else {
							ModUtils.setEntitySpeed((EntityMob) entity);
							entity.getCapability(ModContent.SPAWN_TRACKER, null).setSpawned(true);
						}
					}
					else if (entity.getClass() == EntityDummyZombie0.class) {
						if (world.getBiome(entity.getPosition()) == WastelandWorld.apocalypse_desert) {
							EntityDummyZombie0 zombie = (EntityDummyZombie0) entity;
							EntityDummyHusk0 newentity = new EntityDummyHusk0(world);
							newentity.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
							newentity.renderYawOffset=zombie.renderYawOffset;
							newentity.setPosition(entity.posX, entity.posY, entity.posZ);
							if (zombie.isNoDespawnRequired()) newentity.enablePersistence();
							entity.setDead();
							world.spawnEntity(newentity);
							event.setCanceled(true);
							ModUtils.setEntitySpeed((EntityMob) entity);
						} else {
							ModUtils.setEntitySpeed((EntityMob) entity);
							entity.getCapability(ModContent.SPAWN_TRACKER, null).setSpawned(true);
						}
					} else if (entity.getClass() == EntityDummyZombie1.class) {
						if (world.getBiome(entity.getPosition()) == WastelandWorld.apocalypse_desert) {
							EntityDummyZombie1 zombie = (EntityDummyZombie1) entity;
							EntityDummyHusk1 newentity = new EntityDummyHusk1(world);
							newentity.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
							newentity.renderYawOffset=zombie.renderYawOffset;
							newentity.setPosition(entity.posX, entity.posY, entity.posZ);
							if (zombie.isNoDespawnRequired()) newentity.enablePersistence();
							entity.setDead();
							world.spawnEntity(newentity);
							event.setCanceled(true);
							ModUtils.setEntitySpeed((EntityMob) entity);
						} else {
							ModUtils.setEntitySpeed((EntityMob) entity);
							entity.getCapability(ModContent.SPAWN_TRACKER, null).setSpawned(true);
						}
					} else if (entity.getClass() == EntityDummyZombie2.class) {
						if (world.getBiome(entity.getPosition()) == WastelandWorld.apocalypse_desert) {
							EntityDummyZombie2 zombie = (EntityDummyZombie2) entity;
							EntityDummyHusk2 newentity = new EntityDummyHusk2(world);
							newentity.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
							newentity.renderYawOffset=zombie.renderYawOffset;
							newentity.setPosition(entity.posX, entity.posY, entity.posZ);
							if (zombie.isNoDespawnRequired()) newentity.enablePersistence();
							entity.setDead();
							world.spawnEntity(newentity);
							event.setCanceled(true);
							ModUtils.setEntitySpeed((EntityMob) entity);
						} else {
							ModUtils.setEntitySpeed((EntityMob) entity);
							entity.getCapability(ModContent.SPAWN_TRACKER, null).setSpawned(true);
						}
					}
				}
			}
			if (entity instanceof EntityZombie) {
				//adds additional targeting to zombies to make them attack other mobs
				EntityZombie zombie = (EntityZombie) entity;
				zombie.targetTasks.addTask(3, new EntityAINearestAttackableTarget(zombie, EntityTF2Character.class, false));
				zombie.targetTasks.addTask(3, new EntityAINearestAttackableTarget(zombie, EntitySentry.class, false));
				zombie.targetTasks.addTask(3, new EntityAINearestAttackableTarget(zombie, IAnimaniaAnimal.class, false));
				zombie.targetTasks.addTask(3, new EntityAINearestAttackableTarget(zombie, EntityVillagerTek.class, false));
			}
			//kills xp orbs as they spawn because we dont need them
			else if (entity instanceof EntityXPOrb) {
				entity.setDead();
			}
			//makes the vespa hostile to the player and other mobs
			else if (entity instanceof EntityVespa) {
				EntityVespa vespa = (EntityVespa) entity;
				vespa.targetTasks.addTask(2, new EntityAINearestAttackableTarget(vespa, EntityPlayer.class, false));
				vespa.targetTasks.addTask(3, new EntityAINearestAttackableTarget(vespa, EntityTF2Character.class, false));
				vespa.targetTasks.addTask(3, new EntityAINearestAttackableTarget(vespa, IAnimaniaAnimal.class, false));
				vespa.targetTasks.addTask(3, new EntityAINearestAttackableTarget(vespa, EntityVillagerTek.class, false));
			}
			//makes the sludge lord hostile to the player
			else if (entity instanceof EntitySludgeLord) {
				EntitySludgeLord slord = (EntitySludgeLord) entity;
				slord.targetTasks.addTask(2, new EntityAINearestAttackableTarget(slord, EntityPlayer.class, false));
				slord.targetTasks.addTask(3, new EntityAINearestAttackableTarget(slord, EntityTF2Character.class, false));
			}
		}
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

	@SubscribeEvent
	public void hordeSpawn(HordeSpawnEntityEvent event) {
		Entity entity = event.entity;
		World world = entity.world;
		EntityPlayer player = event.getEntityPlayer();
		if (!world.isRemote) {
			//replace zombies with vespas if they are in a sky base
			if (player.getPosition().getY() - event.pos.getY() > 35) {
				event.entity = new EntityVespa(world);
				//give the vespas the ability to break blocks
				if (event.entity.hasCapability(ModContent.BLOCK_BREAKING, null)) {
					event.entity.getCapability(ModContent.BLOCK_BREAKING, null).enableBlockBreaking(true);
				}
				event.pos = new BlockPos(event.pos.getX(), player.posY, event.pos.getX());
			} else if (entity.getClass() == EntityZombie.class && event.getDay() <=50) {
				//turns zombies into a random variant based on rng and day
				Random rand = world.rand;
				int randInt = rand.nextInt(100);
				if (randInt < 3) {
					event.entity = new EntityTFZombie(world);
				} else if (randInt < 45 - (world.getWorldTime()/24000)) {
					event.entity = new EntityCrawlingZombie(world);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDeath(LivingDeathEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		if (!world.isRemote) {
			//replaces the tf2 character with an infected human if killed by a rupter
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

	@SubscribeEvent
	public void onDamage(LivingDamageEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		Entity attacker = event.getSource().getImmediateSource();
		World world = entity.world;
		if (!world.isRemote) {
			//adds 1/10 chance for bleed effect from husks
			if ((attacker instanceof EntityHusk) && world.rand.nextInt(10)==0) {
				entity.addPotionEffect(new PotionEffect(TF2weapons.bleeding, 70));
			}
		}
	}

	//adds items to zombie loot table
	@SubscribeEvent
	public void onLootTableLoad(LootTableLoadEvent event) {
		ResourceLocation loc = event.getName();
		if (loc == LootTableList.ENTITIES_ZOMBIE) {
			LootTable table  = event.getTable();
			LootEntryItem clothLoot = new LootEntryItem(ModContent.CLOTH_FABRIC, 1, 1, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(0, 2))}, new LootCondition[0], "cloth_fabric");
			table.addPool(new LootPool(new LootEntryItem[]{clothLoot}, new LootCondition[]{new KilledByPlayer(false),
					new RandomChanceWithLooting(1f, 0.5f)}, new RandomValueRange(1), new RandomValueRange(0), "cloth_fabric"));

			LootEntryItem gunpowder = new LootEntryItem(Items.GUNPOWDER, 1, 1, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1))}, new LootCondition[0], "gunpowder");
			table.addPool(new LootPool(new LootEntryItem[]{gunpowder}, new LootCondition[]{new RandomChanceWithLooting(0.1f, 0.05f)}, new RandomValueRange(1), new RandomValueRange(0), "gunpowder"));
			LootEntryItem eye = new LootEntryItem(Items.FERMENTED_SPIDER_EYE, 1, 1, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1))}, new LootCondition[0], "spider_eye");
			table.addPool(new LootPool(new LootEntryItem[]{eye}, new LootCondition[]{new RandomChanceWithLooting(0.1f, 0.05f)}, new RandomValueRange(1), new RandomValueRange(0), "spider_eye"));
		}
	}


	//make entities that can break blocks do so
	@SubscribeEvent
	public void livingTick(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		if (entity.hasCapability(ModContent.BLOCK_BREAKING, null)) {
			IBreakBlocks cap = entity.getCapability(ModContent.BLOCK_BREAKING, null);
			if (cap.canBreakBlocks()) {
				if (world.getWorldTime() % 10 == 0) {
					cap.tryBreakBlocks();
				}
			}
		}
	}
}
