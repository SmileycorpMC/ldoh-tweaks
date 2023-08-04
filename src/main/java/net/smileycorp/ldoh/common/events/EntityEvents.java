package net.smileycorp.ldoh.common.events;

import com.Fishmod.mod_LavaCow.entities.EntityBanshee;
import com.Fishmod.mod_LavaCow.entities.EntitySludgeLord;
import com.Fishmod.mod_LavaCow.entities.EntityZombieMushroom;
import com.Fishmod.mod_LavaCow.entities.flying.EntityPtera;
import com.Fishmod.mod_LavaCow.entities.flying.EntityVespa;
import com.Fishmod.mod_LavaCow.entities.tameable.EntityWeta;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityEmanaAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityEmana;
import funwayguy.epicsiegemod.ai.ESM_EntityAIGrief;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.atlas.api.SimpleStringMessage;
import net.smileycorp.hordes.common.event.HordeSpawnEntityEvent;
import net.smileycorp.hordes.common.event.InfectionDeathEvent;
import net.smileycorp.hordes.infection.InfectionRegister;
import net.smileycorp.ldoh.common.ConfigHandler;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.IBreakBlocks;
import net.smileycorp.ldoh.common.capabilities.ISpawnTracker;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.smileycorp.ldoh.common.entity.*;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.network.PacketHandler;
import net.smileycorp.ldoh.common.util.EnumBiomeType;
import net.smileycorp.ldoh.common.util.ModUtils;
import net.smileycorp.ldoh.integration.tektopia.TektopiaUtils;
import rafradek.TF2weapons.TF2weapons;
import rafradek.TF2weapons.entity.building.EntityBuilding;
import rafradek.TF2weapons.entity.building.EntitySentry;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;

import java.util.Collection;
import java.util.Random;

public class EntityEvents {

	//capability manager
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		//track whether zombies or crawling zombies were loaded from world data or not
		if (!entity.hasCapability(LDOHCapabilities.SPAWN_TRACKER, null) && entity instanceof EntityLiving) {
			event.addCapability(ModDefinitions.getResource("SpawnProvider"), new ISpawnTracker.Provider());
		}
		//lets entities break blocks if the capability is set to enabled
		if (!entity.hasCapability(LDOHCapabilities.BLOCK_BREAKING, null) && entity instanceof EntityLiving) {
			event.addCapability(ModDefinitions.getResource("BlockBreaker"), new IBreakBlocks.Provider((EntityLiving) entity));
		}
	}

	//hooks into the hordes infection event
	//lets players kill infected mobs to stop infection
	//highest priority to fire before our other handlers
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onInfect(InfectionDeathEvent event) {
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			event.setCanceled(true);
			return;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		Entity entity = event.getEntity();
		if (!world.isRemote) {
			//replacing zombies with rare spawns
			if (entity.hasCapability(LDOHCapabilities.SPAWN_TRACKER, null)) {
				ISpawnTracker tracker = entity.getCapability(LDOHCapabilities.SPAWN_TRACKER, null);
				if(!tracker.isSpawned()) {
					if (entity.getClass() == EntityZombie.class) {
						EntityZombie zombie = (EntityZombie) entity;
						EntityMob newentity = null;
						if (!ConfigHandler.legacySpawns) {
							Random rand = world.rand;
							//select random number first to allow for proper weighting
							int randInt = rand.nextInt(100);
							if (randInt < 3) {
								newentity = new EntityTF2Zombie(world);
							} else if (randInt == 3) {
								newentity = new EntityZombieNurse(world);
							}  else if (randInt < 15) {
								newentity = EnumBiomeType.DESERT.matches(world.getBiome(entity.getPosition())) ?
										new EntityCrawlingHusk(world) : new EntityCrawlingZombie(world);
							} else if (randInt < 17) {
								newentity = new EntityZombieFireman(world);
							} else if (world.getWorldTime() < 240000) {
								newentity = new EntityDummyZombie2(world);
							} else if (world.getWorldTime() < 480000) {
								newentity = new EntityDummyZombie1(world);
							}
							//turns zombies into husks in a desert
							else if (EnumBiomeType.DESERT.matches(world.getBiome(entity.getPosition()))) {
								newentity = new EntityHusk(world);
							}
						}
						//turns zombies into husks in a desert
						else if (EnumBiomeType.DESERT.matches(world.getBiome(entity.getPosition()))) {
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
							ModUtils.setEntitySpeed(newentity);
						} else {
							ModUtils.setEntitySpeed((EntityMob) entity);
							tracker.setSpawned(true);
						}
						//replace crawling zombies with their husk counterpart in deserts
					} else if (entity.getClass() == EntityCrawlingZombie.class) {
						if (EnumBiomeType.DESERT.matches(world.getBiome(entity.getPosition()))) {
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
							tracker.setSpawned(true);
						}
					}
					else if (entity.getClass() == EntityDummyZombie0.class) {
						if (EnumBiomeType.DESERT.matches(world.getBiome(entity.getPosition()))) {
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
							tracker.setSpawned(true);
						}
					} else if (entity.getClass() == EntityDummyZombie1.class) {
						if (EnumBiomeType.DESERT.matches(world.getBiome(entity.getPosition()))) {
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
							tracker.setSpawned(true);
						}
					} else if (entity.getClass() == EntityDummyZombie2.class) {
						if (EnumBiomeType.DESERT.matches(world.getBiome(entity.getPosition()))) {
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
							tracker.setSpawned(true);
						}
					} else if (entity instanceof EntityZombieMushroom) {
						if (EnumBiomeType.BADLANDS.matches(world.getBiome(entity.getPosition()))) ((EntityZombieMushroom)entity).setSkin(1);
						tracker.setSpawned(true);
					}
					if(!tracker.isSpawned()) tracker.setSpawned(true);
				}
			}
			if (entity instanceof EntityZombie) {
				//adds additional targeting to zombies to make them attack other mobs
				EntityZombie zombie = (EntityZombie) entity;
				zombie.targetTasks.addTask(3, new EntityAINearestAttackableTarget(zombie, EntityTF2Character.class, false));
				zombie.targetTasks.addTask(3, new EntityAINearestAttackableTarget(zombie, EntitySentry.class, false));
				zombie.targetTasks.addTask(3, new EntityAINearestAttackableTarget(zombie, EntityAnimal.class, false));
				if (Loader.isModLoaded("tektopia")) TektopiaUtils.addTargetTask(zombie);
				zombie.tasks.addTask(3, new ESM_EntityAIGrief(zombie));
			}
			//makes the vespa hostile to the player and other mobs
			else if (entity instanceof EntityVespa) {
				EntityVespa vespa = (EntityVespa) entity;
				vespa.targetTasks.addTask(2, new EntityAINearestAttackableTarget(vespa, EntityPlayer.class, false));
				vespa.targetTasks.addTask(3, new EntityAINearestAttackableTarget(vespa, EntityTF2Character.class, false));
				vespa.targetTasks.addTask(3, new EntityAINearestAttackableTarget(vespa, EntityAnimal.class, false));
				if (Loader.isModLoaded("tektopia")) TektopiaUtils.addTargetTask(vespa);
			}
			//makes the sludge lord hostile to the player
			else if (entity instanceof EntitySludgeLord) {
				EntitySludgeLord slord = (EntitySludgeLord) entity;
				slord.targetTasks.addTask(2, new EntityAINearestAttackableTarget(slord, EntityPlayer.class, false));
				slord.targetTasks.addTask(3, new EntityAINearestAttackableTarget(slord, EntityTF2Character.class, false));
				slord.targetTasks.addTask(3, new EntityAINearestAttackableTarget(slord, EntityAnimal.class, false));
				if (Loader.isModLoaded("tektopia")) TektopiaUtils.addTargetTask(slord);
			}
			//makes the weta hostile to the player
			else if (entity instanceof EntityWeta) {
				EntityWeta weta = (EntityWeta) entity;
				weta.targetTasks.addTask(2, new EntityAINearestAttackableTarget(weta, EntityPlayer.class, false));
				weta.targetTasks.addTask(3, new EntityAINearestAttackableTarget(weta, EntityTF2Character.class, false));
				weta.targetTasks.addTask(3, new EntityAINearestAttackableTarget(weta, EntityAnimal.class, false));
				if (Loader.isModLoaded("tektopia")) TektopiaUtils.addTargetTask(weta);
				//weta.tasks.addTask(3, new AIBreakEgg(weta));
			}
			//makes the ptera hostile to the player
			else if (entity instanceof EntityPtera) {
				EntityPtera ptera = (EntityPtera) entity;
				ptera.targetTasks.addTask(2, new EntityAINearestAttackableTarget(ptera, EntityPlayer.class, false));
				ptera.targetTasks.addTask(3, new EntityAINearestAttackableTarget(ptera, EntityTF2Character.class, false));
				ptera.targetTasks.addTask(3, new EntityAINearestAttackableTarget(ptera, EntityAnimal.class, false));
				if (Loader.isModLoaded("tektopia")) TektopiaUtils.addTargetTask(ptera);
				//ptera.tasks.addTask(3, new AIBreakEgg(ptera));
			}
			//makes the banshee hostile to the player
			else if (entity instanceof EntityBanshee) {
				EntityBanshee banshee = (EntityBanshee) entity;
				banshee.targetTasks.addTask(2, new EntityAINearestAttackableTarget(banshee, EntityPlayer.class, false));
				banshee.targetTasks.addTask(3, new EntityAINearestAttackableTarget(banshee, EntityTF2Character.class, false));
				banshee.targetTasks.addTask(3, new EntityAINearestAttackableTarget(banshee, EntityAnimal.class, false));
				if (Loader.isModLoaded("tektopia")) TektopiaUtils.addTargetTask(banshee);
				//banshee.tasks.addTask(3, new AIBreakEgg(banshee));
			}
		}
		//fix rare skeleton horse traps from appearing as well as skeletons and creepers spawning from fish's undead rising
		if (entity instanceof EntitySkeletonHorse) event.setCanceled(true);
		else if (entity instanceof EntitySkeleton) event.setCanceled(true);
		else if (entity instanceof EntityCreeper) event.setCanceled(true);
	}

	@SubscribeEvent
	public void hordeSpawn(HordeSpawnEntityEvent event) {
		Entity entity = event.entity;
		World world = entity.world;
		EntityPlayer player = event.getEntityPlayer();
		if (!world.isRemote) {
			//replace zombies with vespas if they are in a sky base
			if (player.getPosition().getY() - event.pos.getY() > 30) {
				if (entity instanceof EntityParasiteBase) entity = (event.getDay() >= 90) ? new EntityEmanaAdapted(world) : new EntityEmana(world);
				else entity = new EntityVespa(world);
				//give the vespas the ability to break blocks
				if (event.entity.hasCapability(LDOHCapabilities.BLOCK_BREAKING, null)) {
					event.entity.getCapability(LDOHCapabilities.BLOCK_BREAKING, null).enableBlockBreaking(true);
				}
				event.pos = new BlockPos(event.pos.getX(), player.posY, event.pos.getZ());

			} else if (entity.getClass() == EntityZombie.class) {
				//turns zombies into a random variant based on rng and day
				Random rand = world.rand;
				int randInt = rand.nextInt(100);
				if (randInt < 3) {
					event.entity = new EntityTF2Zombie(world);
				} else if (event.getDay() <= 50 && randInt < 45 - (world.getWorldTime()/24000)) {
					event.entity = new EntityCrawlingZombie(world);
				}
			}
		}
	}

	@SubscribeEvent
	public void calculateDamage(LivingHurtEvent event) {
		if (ConfigHandler.legacyDamage) return;
		EntityLivingBase entity = event.getEntityLiving();
		Entity attacker = event.getSource().getImmediateSource();
		World world = entity.world;
		if (!world.isRemote) {
			if (InfectionRegister.canCauseInfection(attacker)) {
				ItemStack stack = entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
				Collection<AttributeModifier> modifiers = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND)
						.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
				float amount = 1;
				EnchantmentHelper.getModifierForCreature(stack, entity.getCreatureAttribute());
				for (AttributeModifier modifier : modifiers) amount += modifier.getAmount();
				amount = Math.max(amount, 3f);
				event.setAmount(Math.max(amount, event.getAmount()));
			}
		}
	}

	@SubscribeEvent
	public void onDamage(LivingDamageEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		Entity attacker = event.getSource().getImmediateSource();
		World world = entity.world;
		if (!world.isRemote) {
			if (InfectionRegister.canCauseInfection(attacker) && ConfigHandler.legacyDamage) {
				event.setAmount(3f);
			}
			//adds 1/10 chance for bleed effect from husks
			if ((attacker instanceof EntityHusk) && world.rand.nextInt(10) == 0) {
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
			LootEntryItem clothLoot = new LootEntryItem(LDOHItems.CLOTH_FABRIC, 1, 1, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(0, 2))}, new LootCondition[0], "cloth_fabric");
			table.addPool(new LootPool(new LootEntryItem[]{clothLoot}, new LootCondition[]{new KilledByPlayer(false),
					new RandomChanceWithLooting(1f, 0.5f)}, new RandomValueRange(1), new RandomValueRange(0), "cloth_fabric"));
			LootEntryItem eye = new LootEntryItem(Items.FERMENTED_SPIDER_EYE, 1, 1, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1))}, new LootCondition[0], "spider_eye");
			table.addPool(new LootPool(new LootEntryItem[]{eye}, new LootCondition[]{new RandomChanceWithLooting(0.1f, 0.05f)}, new RandomValueRange(1), new RandomValueRange(0), "spider_eye"));
		}
	}

	//make entities that can break blocks do so
	@SubscribeEvent
	public void livingTick(LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		if (entity.hasCapability(LDOHCapabilities.BLOCK_BREAKING, null)) {
			IBreakBlocks cap = entity.getCapability(LDOHCapabilities.BLOCK_BREAKING, null);
			if (cap.canBreakBlocks()) {
				if (world.getWorldTime() % 10 == 0) {
					if (cap.tryBreakBlocks()) entity.attackEntityFrom(DamageSource.MAGIC, 10f);
				}
			}
		}
		//toxic gas
		if (!world.isRemote && world.getWorldType() != WorldType.FLAT
				&! (entity instanceof EntityParasiteBase || entity.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)) {
			if (entity.getPosition().getY() + entity.getEyeHeight() <= 31) {
				if (entity instanceof EntityBuilding || entity instanceof EntityTurret) return;
				if (entity instanceof EntityTF2Character) if (((EntityTF2Character) entity).isRobot()) return;
				ItemStack helm = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
				if (entity.ticksExisted % 35 == 0) {
					//check if player has a gas mask and damage it instead, check damage to prevent it from fully breaking
					if (helm.getItem() == LDOHItems.GAS_MASK && helm.getMetadata() < helm.getMaxDamage()) {
						helm.damageItem(1, entity);
						if (helm.getMetadata() == helm.getMaxDamage() && entity instanceof EntityPlayerMP) {
							((EntityPlayerMP)entity).connection.sendPacket(new SPacketSoundEffect(SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.PLAYERS, entity.posX, entity.posY, entity.posZ, 1.0F, 1.0F));
						}
					} else {
						//deal damage if not wearing it and display message
						entity.attackEntityFrom(LDOHTweaks.TOXIC_GAS_DAMAGE, 1);
						if (entity instanceof EntityPlayerMP) {
							PacketHandler.NETWORK_INSTANCE.sendTo(new SimpleStringMessage(ModDefinitions.GAS_MESSAGE), (EntityPlayerMP) entity);
						}
					}
				}
			}
		}
	}
}
