package net.smileycorp.ldoh.common;

import ivorius.reccomplex.events.StructureGenerationEvent;

import java.util.Random;

import mcjty.lostcities.dimensions.world.LostCityChunkGenerator;
import mcjty.lostcities.dimensions.world.lost.BuildingInfo;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;
import net.minecraftforge.event.world.WorldEvent.CreateSpawnPosition;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.smileycorp.atlas.api.SimpleStringMessage;
import net.smileycorp.atlas.api.util.DataUtils;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.hordes.common.Hordes;
import net.smileycorp.hordes.common.event.HordeBuildSpawntableEvent;
import net.smileycorp.hordes.common.event.HordeSpawnEntityEvent;
import net.smileycorp.hordes.common.event.InfectionDeathEvent;
import net.smileycorp.hordes.common.hordeevent.IHordeSpawn;
import net.smileycorp.hordes.infection.HordesInfection;
import net.smileycorp.ldoh.common.capabilities.IBreakBlocks;
import net.smileycorp.ldoh.common.capabilities.IHunger;
import net.smileycorp.ldoh.common.capabilities.IMiniRaid;
import net.smileycorp.ldoh.common.capabilities.ISpawnTracker;
import net.smileycorp.ldoh.common.capabilities.IUnburiedSpawner;
import net.smileycorp.ldoh.common.entity.EntityCrawlingHusk;
import net.smileycorp.ldoh.common.entity.EntityCrawlingZombie;
import net.smileycorp.ldoh.common.entity.EntityDumbZombie;
import net.smileycorp.ldoh.common.entity.EntityLDOHArchitect;
import net.smileycorp.ldoh.common.entity.EntityLDOHTradesman;
import net.smileycorp.ldoh.common.entity.EntityTFZombie;
import net.smileycorp.ldoh.common.entity.EntityZombieNurse;
import net.smileycorp.ldoh.common.network.CommonPacketHandler;
import net.smileycorp.ldoh.common.util.EnumTFClass;
import net.smileycorp.ldoh.common.util.ModUtils;
import net.smileycorp.ldoh.common.world.WorldGenSafehouse;
import net.tangotek.tektopia.entities.EntityArchitect;
import net.tangotek.tektopia.entities.EntityNecromancer;
import net.tangotek.tektopia.entities.EntityTradesman;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import rafradek.TF2weapons.TF2weapons;
import rafradek.TF2weapons.entity.building.EntitySentry;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;
import rafradek.TF2weapons.item.ItemAmmo;

import com.Fishmod.mod_LavaCow.entities.EntitySludgeLord;
import com.Fishmod.mod_LavaCow.entities.flying.EntityVespa;
import com.Fishmod.mod_LavaCow.entities.tameable.EntityUnburied;
import com.animania.api.interfaces.IAnimaniaAnimal;
import com.dhanantry.scapeandrunparasites.entity.ai.EntityPInfected;
import com.dhanantry.scapeandrunparasites.entity.ai.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityLodo;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityRathol;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.legacy.wasteland.world.WastelandWorld;

@EventBusSubscriber(modid=ModDefinitions.modid)
public class EventListener {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDeath(LivingDeathEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		if (!world.isRemote) {
			//replaces the tf2 character with an infected human if killed by a rupter
			if (event.getSource().getTrueSource() instanceof EntityMudo) {
				if (entity instanceof EntityTF2Character) {
					if (!((EntityTF2Character) entity).isRobot()) {
						EntityInfHuman newentity = new EntityInfHuman(world);
						newentity.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
						newentity.renderYawOffset=entity.renderYawOffset;
						newentity.setPosition(entity.posX, entity.posY, entity.posZ);
						entity.setDead();
						world.spawnEntity(newentity);
					}
				}
			}
			//adds the player to a team after killing the opposite npc
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		Entity entity = event.getEntity();
		if (entity instanceof EntityNecromancer) event.setCanceled(true);
		if (!world.isRemote) {
			//replacing zombies with rare spawns
			if (entity.getClass() == EntityZombie.class) {
				if (entity.hasCapability(ModContent.SPAWN_TRACKER, null)) {
					//uses capability to determine if this entity is loaded from world data or not
					if(!entity.getCapability(ModContent.SPAWN_TRACKER, null).isSpawned()) {
						if (entity.hasCapability(Hordes.HORDESPAWN, null)) {
							IHordeSpawn cap = entity.getCapability(Hordes.HORDESPAWN, null);
							if (cap.isHordeSpawned()) {
								if (DataUtils.isValidUUID(cap.getPlayerUUID())) {
									entity.getCapability(ModContent.SPAWN_TRACKER, null).setSpawned(true);
									return;
								}
							}
						}
						int day = (int) Math.floor(world.getWorldTime()/24000);
						EntityZombie zombie = (EntityZombie) entity;
						EntityMob newentity = null;
						//turns zombies into inf humans between day 50 and 100
						if (day>=50 && day <=100) {
							newentity = new EntityInfHuman(world);
							//turns zombies into a random variant based on rng
						} else {
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
						}
						//sets up new entity
						if (newentity != null) {
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
				//replace crawling zombies with their husk counterpart in deserts
			} else if (entity.getClass() == EntityCrawlingZombie.class) {
				if (entity.hasCapability(ModContent.SPAWN_TRACKER, null)) {
					if(!entity.getCapability(ModContent.SPAWN_TRACKER, null).isSpawned()) {
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
			//makes tf2 npcs avoid zombies more
			else if (entity instanceof EntityTF2Character) {
				EntityTF2Character npc = (EntityTF2Character) entity;
				npc.tasks.addTask(3, new EntityAIAvoidEntity(npc, EntityMob.class, 5.0F, 0.6D, 0.6D));
			}
			//replaces architect with our own version that has our modified trades
			else if (entity instanceof EntityArchitect &! (entity instanceof EntityLDOHArchitect)) {
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
	}

	//randomly prevent picking up lava
	@SubscribeEvent
	public void fillBucket(FillBucketEvent event) {
		World world = event.getWorld();
		RayTraceResult ray = event.getTarget();
		if (ray != null) {
			if (ray.typeOfHit == Type.BLOCK) {
				if (world.getBlockState(ray.getBlockPos()).getMaterial() == Material.LAVA) {
					//50% chance to break the bucket
					if (world.rand.nextInt(2) == 0) {
						event.getEmptyBucket().shrink(1);
						event.setCanceled(true);
						if (!world.isRemote)  {
							ITextComponent text = new TextComponentTranslation(ModDefinitions.lavaPickupMessage);
							text.setStyle(new Style().setColor(TextFormatting.RED).setBold(true));
							event.getEntityPlayer().sendMessage(text);
						}
					}
				}
			}
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
			} else if (entity.getClass() == EntityZombie.class && world.getTotalWorldTime()/24000 <=50) {
				//turns zombies into a random variant based on rng and day
				Random rand = world.rand;
				int randInt = rand.nextInt(100);
				if (randInt < 3) {
					event.entity = new EntityTFZombie(world);
				} else if (randInt < 45 - (world.getWorldTime()/24000)) {
					event.entity = new EntityCrawlingZombie(world);
				} else if (randInt < 75 - (world.getWorldTime()/24000) &! (entity instanceof EntityHusk)) {
					//sets the entity as a dub zombie, which doesn't recieve epic seige mod ai and is slightly slower
					event.entity = new EntityDumbZombie(world);
				}
			} else if (entity instanceof EntityTF2Character) {
				world.getScoreboard().addPlayerToTeam(entity.getCachedUniqueIdString(), player.getTeam().getName() == "RED" ? "BLU" : "RED");
				((EntityTF2Character) entity).setEntTeam(player.getTeam().getName() == "RED" ? 1 : 0);
			}
		}
	}

	@SubscribeEvent
	public void hordeBuildSpawntable(HordeBuildSpawntableEvent event) {
		int day = (int) Math.floor(event.getEntityWorld().getTotalWorldTime()/24000);
		if (day > 100 && day%100 == 0) {
			if (event.getEntityPlayer().getTeam() != null) {
				for (EnumTFClass tfclass : EnumTFClass.values()) event.spawntable.addEntry(tfclass.getEntityClass(), 1);
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
			if (entity instanceof EntityParasiteBase) {
				if (entity.getClass() == EntityLodo.class) {
					if (day < 30) event.setResult(Result.DENY);
				}
				else if (entity.getClass() == EntityMudo.class) {
					if (day < 50) event.setResult(Result.DENY);
				}
				else if (entity instanceof EntityPInfected) {
					if (day < 60) event.setResult(Result.DENY);
				}
				else if (entity.getClass() == EntityRathol.class) {
					if(day < 70) event.setResult(Result.DENY);
				}
				else if (day < 90) event.setResult(Result.DENY);
			}
		}
	}

	@SubscribeEvent
	public void onDamage(LivingDamageEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		Entity attacker = event.getSource().getImmediateSource();
		World world = entity.world;
		if (!world.isRemote) {
			if (attacker instanceof EntityZombie) {
				//sets zombie damage to a fixed 1.5 hearts
				event.setAmount(3f);
				if (entity instanceof EntityTF2Character) {
					//gives the infection effect to non-robots
					if(!((EntityTF2Character) entity).isRobot()) {
						entity.addPotionEffect(new PotionEffect(HordesInfection.INFECTED, 10000, 0));
					}
				}
			}
			//adds 1/10 chance for bleed effect from husks
			if ((attacker instanceof EntityHusk || attacker instanceof EntityCrawlingHusk) && world.rand.nextInt(10)==0) {
				entity.addPotionEffect(new PotionEffect(TF2weapons.bleeding, 70));
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
				//spawns a tf2 zombie in the place of the dead npc
				EntityTFZombie zombie = new EntityTFZombie((EntityTF2Character)entity);
				world.spawnEntity(zombie);
				zombie.setPosition(entity.posX, entity.posY, entity.posZ);
				zombie.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
				entity.setDead();
				event.setResult(Result.DENY);
			}
		}
	}

	//capability manager
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		//track whether zombies or crawling zombies were loaded from world data or not
		if (!entity.hasCapability(ModContent.SPAWN_TRACKER, null) && (entity.getClass() == EntityZombie.class || entity.getClass() == EntityCrawlingZombie.class)) {
			event.addCapability(ModDefinitions.getResource("SpawnProvider"), new ISpawnTracker.Provider());
		}
		//lets entities break blocks if the capability is set to enabled
		if (!entity.hasCapability(ModContent.BLOCK_BREAKING, null) && entity instanceof EntityLiving) {
			event.addCapability(ModDefinitions.getResource("BlockBreaker"), new IBreakBlocks.Provider((EntityLiving) entity));
		}
		//spawner instance to spawn unburied in the caves around players
		if (!entity.hasCapability(ModContent.UNBURIED_SPAWNER, null) && entity instanceof EntityPlayer &!(entity instanceof FakePlayer)) {
			event.addCapability(ModDefinitions.getResource("UnburiedSpawner"), new IUnburiedSpawner.Provider((EntityPlayer) entity));
		}
		//spawner instance for mini raid events
		if (!entity.hasCapability(ModContent.MINI_RAID, null) && entity instanceof EntityPlayer &!(entity instanceof FakePlayer)) {
			event.addCapability(ModDefinitions.getResource("MiniRaid"), new IMiniRaid.Provider((EntityPlayer) entity));
		}
		//gives tf2 npcs hunger
		if (!entity.hasCapability(ModContent.HUNGER, null) && entity instanceof EntityTF2Character) {
			event.addCapability(ModDefinitions.getResource("Hunger"), new IHunger.Provider());
		}
	}

	//adds items to zombie loot table
	@SubscribeEvent
	public void onLootTableLoad(LootTableLoadEvent event) {
		ResourceLocation loc = event.getName();
		if (loc.equals(LootTableList.ENTITIES_ZOMBIE)) {
			LootEntryItem clothLoot = new LootEntryItem(ModContent.CLOTH_FABRIC, 1, 1, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(0, 2))}, new LootCondition[0], "cloth_fabric");
			event.getTable().addPool(new LootPool(new LootEntryItem[]{clothLoot}, new LootCondition[]{new KilledByPlayer(false),
					new RandomChanceWithLooting(1f, 0.5f)}, new RandomValueRange(1), new RandomValueRange(0), "cloth_fabric"));

			LootEntryItem gunpowder = new LootEntryItem(Items.GUNPOWDER, 1, 1, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1))}, new LootCondition[0], "gunpowder");
			event.getTable().addPool(new LootPool(new LootEntryItem[]{gunpowder}, new LootCondition[]{new RandomChanceWithLooting(0.1f, 0.05f)}, new RandomValueRange(1), new RandomValueRange(0), "gunpowder"));
			LootEntryItem eye = new LootEntryItem(Items.FERMENTED_SPIDER_EYE, 1, 1, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1))}, new LootCondition[0], "spider_eye");
			event.getTable().addPool(new LootPool(new LootEntryItem[]{eye}, new LootCondition[]{new RandomChanceWithLooting(0.1f, 0.05f)}, new RandomValueRange(1), new RandomValueRange(0), "spider_eye"));
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
		if (entity.hasCapability(ModContent.HUNGER, null)) {
			entity.getCapability(ModContent.HUNGER, null).onUpdate((EntityLiving) entity);
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
				//toxic gas
				if (player.getPosition().getY()<30) {
					ItemStack helm = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
					if (player.ticksExisted%35==0 && !player.isCreative()) {
						//check if player has a gas mask and damage it instead, check damage to prevent it from fully breaking
						if (helm.getItem() == ModContent.GAS_MASK && helm.getMetadata() < ModContent.GAS_MASK.getMaxDamage()) {
							helm.damageItem(1, player);
						} else {
							//deal damage if not wearing it and display message
							player.attackEntityFrom(ModContent.TOXIC_GAS_DAMAGE, 1);
							if (player instanceof EntityPlayerMP) {
								CommonPacketHandler.NETWORK_INSTANCE.sendTo(new SimpleStringMessage(ModDefinitions.gasMessage), (EntityPlayerMP) player);
							}
						}
					}
				}
				//spawn unburied when player is underground
				if (player.hasCapability(ModContent.UNBURIED_SPAWNER, null) && player.ticksExisted % 60==0) {
					IUnburiedSpawner spawner = player.getCapability(ModContent.UNBURIED_SPAWNER, null);
					int y = (int) Math.floor(player.getPosition().getY());
					Chunk chunk = world.getChunkFromBlockCoords(player.getPosition());
					if (spawner.canSpawnEntity()) {
						//make sure it's underground and try to prevent spawning in buildings
						if (y < Math.max(chunk.getLowestHeight(), 30) &! world.canBlockSeeSky(player.getPosition()) && rand.nextInt(Math.max(y, 20)) <= 15) {
							Vec3d dir = DirectionUtils.getRandomDirectionVecXZ(rand);
							BlockPos pos = new BlockPos(player.posX + dir.x*(rand.nextInt(5)+2), player.posY, player.posZ + dir.z*(rand.nextInt(5)+5));
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
				//lava bucket breaking
				if (player.inventory.hasItemStack(new ItemStack(Items.LAVA_BUCKET))) {
					//20% chance every second
					if (player.ticksExisted%20 == 0 && world.rand.nextInt(5)==0) {
						//place lava and destroy bucket
						world.setBlockState(player.getPosition(), Blocks.LAVA.getDefaultState());
						if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == Items.LAVA_BUCKET) player.getHeldItem(EnumHand.OFF_HAND).shrink(1);
						else for (ItemStack stack : player.inventory.mainInventory) {
							if (stack.getItem() == Items.LAVA_BUCKET) stack.shrink(1);
							break;
						}
						ITextComponent text = new TextComponentTranslation(ModDefinitions.lavaBreakMessage);
						text.setStyle(new Style().setColor(TextFormatting.RED).setBold(true));
						player.sendMessage(text);
					}
				}
				//gifting ammo to tf2 characters
				for(EntityTF2Character entity : world.getEntitiesWithinAABB(EntityTF2Character.class, player.getEntityBoundingBox().grow(5))) {
					for(EntityItem item : world.getEntitiesWithinAABB(EntityItem.class, entity.getEntityBoundingBox())) {
						ItemStack stack = item.getItem();
						if (stack.getItem() instanceof ItemAmmo) {
							//check entity ammo slots to see if it can pick the items up
							ItemStackHandler ammo = entity.refill;
							if (ammo != null) {
								ItemStack ammostack = ammo.getStackInSlot(0);
								if (ammostack==null || ammostack.isEmpty()) {
									ammo.setStackInSlot(0, stack);
									item.setDead();
								} else if (ammostack.getItem() == stack.getItem()) {
									int count = ammostack.getCount() + stack.getCount();
									if (count > 64) {
										ammostack.setCount(64);
										stack.setCount(count - 64);
									}
									else {
										ammostack.setCount(count);
									}
								}
							}
						}
						if (player.hasCapability(ModContent.HUNGER, null)) {
							stack = player.getCapability(ModContent.HUNGER, null).tryPickupFood(stack);
						}
						if (stack.getCount() == 0) item.setDead();
					}
				}
				//Mini Raids
				if (player.hasCapability(ModContent.MINI_RAID, null)) {
					IMiniRaid raid = player.getCapability(ModContent.MINI_RAID, null);
					//spawn the raid if the time is right
					if (raid.shouldSpawnRaid()) raid.spawnRaid();
				}
			}
		}
	}

	//Spawn in World
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void setWorldSpawn(CreateSpawnPosition event) {
		World world = event.getWorld();
		//set gamerule to disable random offset when spawning
		if (world.getGameRules().getInt("spawnRadius")>0) world.getGameRules().setOrCreateGameRule("spawnRadius", "0");
		Random rand = world.rand;
		if (world.provider.getDimension() == 0) {
			WorldGenSafehouse safehouse = new WorldGenSafehouse();
			//tries to find a wasteland biome to spawn the player in
			Biome biome = null;
			int x = 0;
			int y = 0;
			int z = 0;
			int tries = 0;
			while (true) {
				if (biome== WastelandWorld.apocalypse) {
					y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
					//checks if the safehouse is spawning below y60 or if the structure bounds intersect with a city or another biome
					if (y <= 60 || !ModUtils.isOnlyWasteland(world, x, z) || ModUtils.isCity(world, x, z)) {
						y = 0;
						x += rand.nextInt(32) - rand.nextInt(32);
						z += rand.nextInt(32) - rand.nextInt(32);
					}
					else if (y >= 60) {
						//detemines if the safehouse can be placed here
						if (safehouse.markPositions(world, new BlockPos(x, y-1, z), false)) break;
						y = 0;
						x += rand.nextInt(32) - rand.nextInt(32);
						z += rand.nextInt(32) - rand.nextInt(32);
					}
				} else if (tries % 10 == 0 && tries > 0) {
					Vec3d dir = DirectionUtils.getRandomDirectionVecXZ(rand);
					x += 100 * dir.x;
					z += 100 * dir.z;
				} else {
					x += rand.nextInt(64) - rand.nextInt(64);
					z += rand.nextInt(64) - rand.nextInt(64);
				}
				//gets the biome without loading chunks
				biome = world.getBiomeProvider().getBiomes(null, x, z, 1, 1, false)[0];
				tries++;

				//cancel after 1000 tries to not lock the game in an infinite loop
				if (tries >= 1000) {
					y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
					System.out.println("Found no suitable location for spawn");
					break;
				}
			}
			BlockPos spawn = new BlockPos(x, y, z);
			world.getWorldInfo().setSpawn(spawn);
			if (!safehouse.isMarked()) {
				safehouse.markPositions(world, spawn.down(), true);
			}
			safehouse.generate(world, rand, spawn.down());
			event.setCanceled(true);
		}
	}

	//cancels recurrentcomplex structures from spawning in certain circumsatances
	@SubscribeEvent
	public void structureGen(StructureGenerationEvent.Suggest event) {
		World world = event.getWorld();
		if (!world.isRemote) {
			//cancels structure if it intesects spawn chunks
			StructureBoundingBox box = event.spawnContext.boundingBox;
			BlockPos spawn = world.getSpawnPoint();
			if (box.intersectsWith(spawn.getX() - 20, spawn.getZ() - 20, spawn.getX() + 20, spawn.getZ() + 20)) {
				event.setCanceled(true);
				return;
			}
			IChunkProvider provider = world.getChunkProvider();
			//cancels structure if it's in a city
			if (provider instanceof ChunkProviderServer) {
				IChunkGenerator gen = ((ChunkProviderServer)provider).chunkGenerator;
				if (gen instanceof LostCityChunkGenerator) {
					ChunkPos pos0 = world.getChunkFromBlockCoords(new BlockPos(box.minX, 0, box.minZ)).getPos();
					ChunkPos pos1 = world.getChunkFromBlockCoords(new BlockPos(box.maxX, 0, box.maxZ)).getPos();
					for (int i = pos0.x; i <= pos1.x; i++) {
						for (int j = pos0.z; j <= pos1.z; j++) {
							if (BuildingInfo.isCity(i, j, (LostCityChunkGenerator) gen)) {
								event.setCanceled(true);
								return;
							}
						}
					}
				}
			}
		}
	}

	//remove coal, iron and gold from generating outside of deserts so we can use our own generation
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void createDecorator(GenerateMinable event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		Biome biome = world.getBiome(pos);
		if (biome != WastelandWorld.apocalypse_desert) {
			GenerateMinable.EventType type = event.getType();
			if (type == GenerateMinable.EventType.COAL || type == GenerateMinable.EventType.IRON || type == GenerateMinable.EventType.GOLD) {
				event.setResult(Result.DENY);
			}
		}
	}

}
