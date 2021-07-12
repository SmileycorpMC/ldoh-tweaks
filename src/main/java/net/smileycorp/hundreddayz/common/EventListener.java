package net.smileycorp.hundreddayz.common;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
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
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
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
import net.smileycorp.hordes.common.event.HordeSpawnEntityEvent;
import net.smileycorp.hordes.common.event.InfectionDeathEvent;
import net.smileycorp.hordes.common.hordeevent.HordeSpawnProvider;
import net.smileycorp.hordes.common.hordeevent.IHordeSpawn;
import net.smileycorp.hordes.infection.HordesInfection;
import net.smileycorp.hundreddayz.common.capability.SpawnProvider;
import net.smileycorp.hundreddayz.common.entity.EntityTFZombie;
import net.smileycorp.hundreddayz.common.entity.EntityZombieNurse;
import net.smileycorp.hundreddayz.common.world.WorldDataSpawnBase;
import net.smileycorp.hundreddayz.common.world.WorldGenSpawnBase;
import rafradek.TF2weapons.TF2weapons;
import rafradek.TF2weapons.entity.building.EntitySentry;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;
import rafradek.TF2weapons.item.ItemAmmo;
import biomesoplenty.api.biome.BOPBiomes;

import com.Fishmod.mod_LavaCow.entities.EntitySludgeLord;
import com.Fishmod.mod_LavaCow.entities.flying.EntityVespa;
import com.Fishmod.mod_LavaCow.entities.tameable.EntityUnburied;
import com.animania.api.interfaces.IAnimaniaAnimal;
import com.dhanantry.scapeandrunparasites.entity.ai.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.ai.EntityTInfected;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityLodo;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityRathol;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.legacy.wasteland.world.WastelandWorld;

@EventBusSubscriber(modid=ModDefinitions.modid)
public class EventListener {
	
	//TF2 Team Joining
	@SubscribeEvent
	public static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
		World world = event.getEntity().world;
		EntityPlayer player = event.getEntityPlayer();
		Entity target = event.getTarget();
		ItemStack stack = event.getItemStack();
		//adds player to npc team
		if (target instanceof EntityTF2Character && !world.isRemote) {
			if (stack.isEmpty() && player.getTeam() == null) {
				ModDefinitions.addPlayerToTeam(player, target.getTeam().getName());
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDeath(LivingDeathEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		if (!world.isRemote) {
			//replaces the tf2 character with an infected human
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
			//adds the player to a team after killing the oposite npc
			if (event.getSource().getTrueSource() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
				if (entity instanceof EntityTF2Character) {
					if (!((EntityTF2Character) entity).isRobot()) {
						if (player.getTeam() == null) {
							if (entity.getTeam() == world.getScoreboard().getTeam("RED")) {
								ModDefinitions.addPlayerToTeam(player, "BLU");
							} else if (entity.getTeam() == world.getScoreboard().getTeam("BLU")) {
								ModDefinitions.addPlayerToTeam(player, "RED");
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		Entity entity = event.getEntity();
		if (!world.isRemote) {
			//replacing zombies with rare spawns
			if (entity.getClass() == EntityZombie.class) {
				if (entity.hasCapability(SpawnProvider.SPAWN_TRACKER, null)) {
					if(!entity.getCapability(SpawnProvider.SPAWN_TRACKER, null).isSpawned()) {
						if (entity.hasCapability(HordeSpawnProvider.HORDESPAWN, null)) {
							IHordeSpawn cap = entity.getCapability(HordeSpawnProvider.HORDESPAWN, null);
							if (cap.isHordeSpawned()) {
								if (DataUtils.isValidUUID(cap.getPlayerUUID())) {
									entity.getCapability(SpawnProvider.SPAWN_TRACKER, null).setSpawned(true);
									return;
								}
							}
						}
						int day = Math.round(world.getWorldTime()/24000);
						EntityZombie zombie = (EntityZombie) entity;
						EntityMob newentity = null;
						//turns zombies into inf humans after day 50
						if (day>50) {
							newentity = new EntityInfHuman(world);
						}
						//turns zombies into husks in a desert
						else if (world.getBiome(entity.getPosition()) == WastelandWorld.apocalypse_desert) {
							newentity = new EntityHusk(world);
						//turns zombies into a random variant based on rng
						} else {
							Random rand = world.rand;
							int randInt = rand.nextInt(100);
							if (randInt < 4) {
								newentity = new EntityTFZombie(world);
							} else if (randInt == 4) {
								//newentity = new EntityZombieNurse(world);
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
							setEntitySpeed((EntityMob) entity);
						} else {
							setEntitySpeed((EntityMob) entity);
							//set capability flag so this event doesn't retrigger for the zombie
							entity.getCapability(SpawnProvider.SPAWN_TRACKER, null).setSpawned(true);
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
			}
			//kills xp orbs as they spawn because we dont need them
			if (entity instanceof EntityXPOrb) {
				entity.setDead();
			}
			//makes the vespa hostile to the player
			else if (entity instanceof EntityVespa) {
				EntityVespa vespa = (EntityVespa) entity;
				vespa.targetTasks.addTask(2, new EntityAINearestAttackableTarget(vespa, EntityPlayer.class, false));
				vespa.targetTasks.addTask(3, new EntityAINearestAttackableTarget(vespa, EntityTF2Character.class, false));
			}
			//makes the sludge lord hostile to the player
			else if (entity instanceof EntitySludgeLord) {
				EntitySludgeLord slord = (EntitySludgeLord) entity;
				slord.targetTasks.addTask(2, new EntityAINearestAttackableTarget(slord, EntityPlayer.class, false));
				slord.targetTasks.addTask(3, new EntityAINearestAttackableTarget(slord, EntityTF2Character.class, false));
			}
			
		}
	}
	
	private void setEntitySpeed(EntityMob entity) {
		World world = entity.world;
		Biome biome = world.getBiome(entity.getPosition());
		IAttributeInstance speed = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		if (biome == BOPBiomes.wasteland.get()) {
			speed.setBaseValue(0.41);
		} else {
			speed.setBaseValue(0.23);
		}
		//speed.applyModifier(new DayTimeSpeedModifier(world));;
	}

	@SubscribeEvent
	public void hordeSpawn(HordeSpawnEntityEvent event) {
		Entity entity = event.entity;
		World world = entity.world;
		EntityPlayer player = event.getEventPlayer();
		if (!world.isRemote) {
			if (player.getPosition().getY() - event.pos.getY() > 45) {
				event.entity = new EntityVespa(world);
				event.pos = new BlockPos(event.pos.getX(), player.posY, event.pos.getX());
			} else if (entity.getClass() == EntityZombie.class) {
				//turns zombies into husks in a desert
				if (world.getBiome(event.pos) == WastelandWorld.apocalypse_desert) {
					event.entity = new EntityHusk(world);
				//turns zombies into a random variant based on rng
				} else {
					Random rand = world.rand;
					int randInt = rand.nextInt(100);
					if (randInt < 4) {
						event.entity = new EntityTFZombie(world);
					} else if (randInt == 4) {
						//event.entity = new EntityZombieNurse(world);
					}
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
			int day = Math.round(world.getWorldTime()/24000);
			if (entity instanceof EntityParasiteBase) {
				if (entity.getClass() == EntityLodo.class) {
					if (day < 30) event.setResult(Result.DENY);
				}
				else if (entity.getClass() == EntityMudo.class) {
					if (day < 50) event.setResult(Result.DENY);
				}
				else if (entity instanceof EntityTInfected) {
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
			//adds 1/10 chance for bleed effect to husk
			if (attacker instanceof EntityHusk && world.rand.nextInt(10)==0) {
				entity.addPotionEffect(new PotionEffect(TF2weapons.bleeding, 70, 0));
			}
			//adds 1/5 chance for bleed effect to nurse
			if (attacker instanceof EntityZombieNurse && world.rand.nextInt(5)==0) {
				entity.addPotionEffect(new PotionEffect(TF2weapons.bleeding, 30, 1));
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
		if (!entity.hasCapability(SpawnProvider.SPAWN_TRACKER, null) && entity.getClass() == EntityZombie.class) {
			event.addCapability(ModDefinitions.getResource("SpawnProvider"), new SpawnProvider());
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
						if (helm.getItem() == ModContent.GAS_MASK && helm.getMetadata() < ModContent.GAS_MASK.getMaxDamage()) {
							helm.damageItem(1, player);
						} else {
							player.attackEntityFrom(ModContent.TOXIC_GAS_DAMAGE, 1);
							if (player instanceof EntityPlayerMP) {
								CommonPacketHandler.NETWORK_INSTANCE.sendTo(new SimpleStringMessage(ModDefinitions.gasMessage), (EntityPlayerMP) player);
							}
						}
					}
				}
				//unburied
				int y = (int) Math.floor(player.getPosition().getY());
				Chunk chunk = world.getChunkFromBlockCoords(player.getPosition());
				if (y < Math.max(chunk.getLowestHeight(), 30) &! world.canBlockSeeSky(player.getPosition()) && player.ticksExisted%60==0 && rand.nextInt(Math.max(y, 1)) <= 15) {
					Vec3d dir = DirectionUtils.getRandomDirectionVecXZ(rand);
					BlockPos pos = new BlockPos(player.posX + dir.x*(rand.nextInt(5)+2), player.posY, player.posZ + dir.z*(rand.nextInt(5)+5));
					if (!(world.isAirBlock(pos) && world.isAirBlock(pos.up()) && world.isBlockFullCube(pos.down()) 
							&& DirectionUtils.isBrightnessAllowed(world, pos, 7, 0))) {
						for (int j = -5; j <6; j++) {
							if (world.isAirBlock(pos.up(j)) && world.isAirBlock(pos.up(j+1)) && world.isBlockFullCube(pos.up(j-1))) {
								pos = pos.up(j);
								break;
							}
						}
					}
					if (world.isAirBlock(pos) && world.isAirBlock(pos.up()) && world.isBlockFullCube(pos.down()) 
							&& DirectionUtils.isBrightnessAllowed(world, pos, 7, 0) &! world.canBlockSeeSky(pos)) {
						EntityUnburied entity = new EntityUnburied(world);
						entity.setPosition(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
						entity.onInitialSpawn(world.getDifficultyForLocation(pos), null);
						world.spawnEntity(entity);
					}
				}
				//lava bucket burning
				if (player.inventory.hasItemStack(new ItemStack(Items.LAVA_BUCKET))) {
					if (player.ticksExisted%20 == 0 && world.rand.nextInt(10)==0) {
						player.setFire(5);
					}
				}
				for(EntityTF2Character entity : world.getEntitiesWithinAABB(EntityTF2Character.class, player.getEntityBoundingBox().grow(5))) {
					for(EntityItem item : world.getEntitiesWithinAABB(EntityItem.class, entity.getEntityBoundingBox())) {
						ItemStack stack = item.getItem();
						if (stack.getItem() instanceof ItemAmmo) {
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
										if (stack.getCount() == 0) item.setDead();
									}
									else {
										ammostack.setCount(count);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	//Spawn in World
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public static void setWorldSpawn(CreateSpawnPosition event) {
		World world = event.getWorld();
		Random rand = world.rand;
		WorldProvider provider = world.provider;
		if (world.provider.getDimension() == 0) {
			//tries to find a wasteland biome to spawn the player in
			Biome biome = null;
			int x = 0;
			int y = provider.getAverageGroundLevel();
			int z = 0;
			int tries = 0;
			while (biome!=WastelandWorld.apocalypse || y <= 60) {
				if (biome== WastelandWorld.apocalypse) {
					x += rand.nextInt(32) - rand.nextInt(32);
	                z += rand.nextInt(32) - rand.nextInt(32);
				} else {
					x += rand.nextInt(64) - rand.nextInt(64);
	                z += rand.nextInt(64) - rand.nextInt(64);
				}
                y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
                biome = world.getBiomeProvider().getBiome(new BlockPos(x, y, z));
                tries++;
              
                //cancel after 100 tries to not lock the game in an infinite loop
                if (tries++ == 100) {
                    break;
                }
			}
			BlockPos spawn = new BlockPos(x, y, z);
			world.getWorldInfo().setSpawn(spawn);
			//spawn structure
            event.setCanceled(true);
		}
	}

	//Spawn in World
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void setWorldSpawn(DecorateBiomeEvent.Pre event) {
		World world = event.getWorld();
		if (!world.isRemote) {
			WorldDataSpawnBase data = WorldDataSpawnBase.get(world);
			if (!data.isGenerated()) {
				WorldGenSpawnBase spawnbase = new WorldGenSpawnBase();
				spawnbase.generate(world, world.rand, world.getSpawnPoint().down());
				data.setGenerated();
			}
		}
	}
	
	//modify biome decorators
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
