package net.smileycorp.ldoh.common.events;

import com.Fishmod.mod_LavaCow.entities.EntityZombieMushroom;
import com.Fishmod.mod_LavaCow.entities.flying.EntityVespa;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityEmanaAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityEmana;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import mariot7.xlfoodmod.init.ItemListxlfoodmod;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.*;
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
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.smileycorp.atlas.api.SimpleStringMessage;
import net.smileycorp.hordes.common.event.HordeSpawnEntityEvent;
import net.smileycorp.hordes.common.event.InfectionDeathEvent;
import net.smileycorp.hordes.infection.InfectionRegister;
import net.smileycorp.ldoh.common.ConfigHandler;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.TimedEvents;
import net.smileycorp.ldoh.common.capabilities.IBreakBlocks;
import net.smileycorp.ldoh.common.capabilities.ISpawnTracker;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.entity.IEnemyMachine;
import net.smileycorp.ldoh.common.entity.zombie.*;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.network.PacketHandler;
import net.smileycorp.ldoh.common.util.EnumBiomeType;
import net.smileycorp.ldoh.common.util.ModUtils;
import rafradek.TF2weapons.TF2weapons;
import rafradek.TF2weapons.entity.building.EntityBuilding;
import rafradek.TF2weapons.entity.mercenary.EntitySpy;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;
import rafradek.TF2weapons.item.ItemFromData;

import java.util.Collection;
import java.util.Random;

public class EntityEvents {

    //capability manager
    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        //track whether zombies or crawling zombies were loaded from world data or not
        if (!entity.hasCapability(LDOHCapabilities.SPAWN_TRACKER, null) && entity instanceof EntityLiving) {
            event.addCapability(Constants.loc("SpawnProvider"), new ISpawnTracker.Provider());
        }
        //lets entities break blocks if the capability is set to enabled
        if (!entity.hasCapability(LDOHCapabilities.BLOCK_BREAKING, null) && entity instanceof EntityLiving) {
            event.addCapability(Constants.loc("BlockBreaker"), new IBreakBlocks.Provider((EntityLiving) entity));
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

    //prevent entities spawning within 30 blocks of world spawn, should hopefully fix invisible zombies in the safehouse
    @SubscribeEvent
    public void checkSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.getWorld() == null || event.getWorld().isRemote) return;
        World world = event.getWorld();
        BlockPos spawn = world.getSpawnPoint();
        if (spawn.getDistance((int) event.getX(), spawn.getY(), (int) event.getZ()) <= 30) ;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        World world = event.getWorld();
        Entity entity = event.getEntity();
        //fix rare skeleton horse traps from appearing as well as skeletons and creepers spawning from fish's undead rising
        if (entity instanceof EntitySkeletonHorse) event.setCanceled(true);
        else if (entity instanceof EntitySkeleton) event.setCanceled(true);
        else if (entity instanceof EntityCreeper) event.setCanceled(true);
        if (!world.isRemote) {
            //replacing zombies with rare spawns
            if (entity.hasCapability(LDOHCapabilities.SPAWN_TRACKER, null)) {
                ISpawnTracker tracker = entity.getCapability(LDOHCapabilities.SPAWN_TRACKER, null);
                if (!tracker.isSpawned()) {
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
                            } else if (randInt < 15) {
                                newentity = EnumBiomeType.DESERT.matches(world.getBiome(entity.getPosition())) ?
                                        new EntityCrawlingHusk(world) : new EntityCrawlingZombie(world);
                            } else if (randInt < 17) {
                                newentity = new EntityZombieFireman(world);
                                //turns zombies into husks in a desert
                            } else if (EnumBiomeType.DESERT.matches(world.getBiome(entity.getPosition()))) {
                                newentity = new EntityHusk(world);
                            }
                        }
                        //turns zombies into husks in a desert
                        else if (EnumBiomeType.DESERT.matches(world.getBiome(entity.getPosition()))) {
                            newentity = new EntityHusk(world);
                        }
                        //sets up new entity
                        if (newentity != null) {
                            newentity.renderYawOffset = zombie.renderYawOffset;
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
                            newentity.renderYawOffset = zombie.renderYawOffset;
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
                        if (EnumBiomeType.BADLANDS.matches(world.getBiome(entity.getPosition())))
                            ((EntityZombieMushroom) entity).setSkin(1);
                        tracker.setSpawned(true);
                    }
                    if (!tracker.isSpawned()) tracker.setSpawned(true);
                }
            }
            //add special targeting ai
            if (ModUtils.hasSelectors(entity)) ModUtils.setTargetSelectors((EntityCreature) entity);
        }
    }

    @SubscribeEvent
    public void hordeSpawn(HordeSpawnEntityEvent event) {
        Entity entity = event.entity;
        World world = entity.world;
        EntityPlayer player = event.getEntityPlayer();
        if (!world.isRemote) {
            //replace zombies with vespas if they are in a sky base
            if (player.getPosition().getY() - event.pos.getY() > 30) {
                if (entity instanceof EntityParasiteBase)
                    event.entity = (event.getDay() >= 90) ? new EntityEmanaAdapted(world) : new EntityEmana(world);
                else event.entity = new EntityVespa(world);
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
                } else if (event.getDay() <= 50 && randInt < 45 - (world.getWorldTime() / 24000)) {
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
                amount = 3f;
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
            if (event.getEntity() instanceof EntityZombie && attacker instanceof EntityPlayer && entity.getRNG().nextInt(1000) == 0) {
                EntitySpy spy = new EntitySpy(world);
                spy.setPosition(entity.posX, entity.posY, entity.posZ);
                spy.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
                if (TimedEvents.isHalloween())
                    spy.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemFromData.getNewStack("merasmushat"));
                world.spawnEntity(spy);
                spy.attackEntityFrom(event.getSource(), spy.getHealth());
                spy.setHealth(0);
                entity.setDead();
                event.setCanceled(true);
            }
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
        if (loc.equals(LootTableList.ENTITIES_ZOMBIE)) {
            LootTable table = event.getTable();
            LootEntryItem clothLoot = new LootEntryItem(LDOHItems.CLOTH_FABRIC, 1, 1, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(0, 2))}, new LootCondition[0], "cloth_fabric");
            table.addPool(new LootPool(new LootEntryItem[]{clothLoot}, new LootCondition[]{new KilledByPlayer(false),
                    new RandomChanceWithLooting(1f, 0.5f)}, new RandomValueRange(1), new RandomValueRange(0), "cloth_fabric"));
            LootEntryItem eye = new LootEntryItem(Items.FERMENTED_SPIDER_EYE, 1, 1, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1))}, new LootCondition[0], "spider_eye");
            table.addPool(new LootPool(new LootEntryItem[]{eye}, new LootCondition[]{new RandomChanceWithLooting(0.1f, 0.05f)}, new RandomValueRange(1), new RandomValueRange(0), "spider_eye"));
        } else if (loc.equals(new ResourceLocation("rafradek_tf2_weapons:entities/spy"))) {
            LootTable table = event.getTable();
            LootEntryItem baguette = new LootEntryItem(ItemListxlfoodmod.baguette, 1, 1, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 64))}, new LootCondition[0], "baguette");
            table.addPool(new LootPool(new LootEntryItem[]{baguette}, new LootCondition[]{(rand, ctx) -> !TimedEvents.isHalloween(), new KilledByPlayer(false),
                    new RandomChanceWithLooting(1f, 0f)}, new RandomValueRange(1), new RandomValueRange(0), "baguette"));
            LootEntryItem candy = new LootEntryItem(LDOHItems.CANDY_CORN, 1, 1, new LootFunction[]{new SetCount(new LootCondition[0], new RandomValueRange(1, 64))}, new LootCondition[0], "candy");
            table.addPool(new LootPool(new LootEntryItem[]{candy}, new LootCondition[]{(rand, ctx) -> TimedEvents.isHalloween(), new KilledByPlayer(false),
                    new RandomChanceWithLooting(1f, 0f)}, new RandomValueRange(1), new RandomValueRange(0), "candy"));
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
                & !(entity instanceof EntityParasiteBase || entity.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)) {
            if (entity.getPosition().getY() + entity.getEyeHeight() <= 31) {
                if (entity instanceof EntityBuilding || entity instanceof EntityTurret) return;
                if (entity instanceof EntityTF2Character) if (((EntityTF2Character) entity).isRobot()) return;
                ItemStack helm = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (entity.ticksExisted % 35 == 0) {
                    //check if player has a gas mask and damage it instead, check damage to prevent it from fully breaking
                    if (helm.getItem() == LDOHItems.GAS_MASK || helm.getItem() == LDOHItems.NANO_HELM && helm.getItemDamage() < helm.getMaxDamage() - 1) {
                        helm.damageItem(1, entity);
                        if (helm.getItemDamage() == helm.getMaxDamage() - 10 && entity instanceof EntityPlayerMP) {
                            ((EntityPlayerMP) entity).connection.sendPacket(new SPacketSoundEffect(SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.PLAYERS, entity.posX, entity.posY, entity.posZ, 1.0F, 1.0F));
                        }
                    } else {
                        //deal damage if not wearing it and display message
                        entity.attackEntityFrom(LDOHTweaks.TOXIC_GAS_DAMAGE, 1);
                        if (entity instanceof EntityPlayerMP) {
                            PacketHandler.NETWORK_INSTANCE.sendTo(new SimpleStringMessage(Constants.GAS_MESSAGE), (EntityPlayerMP) entity);
                        }
                    }
                }
            }
        }
    }

    //enable parasites on day 50
    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (world.isRemote || world.getWorldTime() < 1200000) return;
        SRPSaveData parasite_data = SRPSaveData.get(world);
        if (parasite_data == null || parasite_data.getEvolutionPhase(0) > -2) return;
        parasite_data.setEvolutionPhase((byte) 0, true, world, true);
        parasite_data.markDirty();
    }

    //prevent coth before day 50 and for machine entities
    @SubscribeEvent
    public void applyEffect(PotionEvent.PotionApplicableEvent event) {
        if (event.getPotionEffect() == null || event.getEntityLiving() == null) return;
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.getEntityWorld();
        if (event.getPotionEffect().getPotion() == SRPPotions.COTH_E && (entity instanceof EntityBuilding ||
                entity instanceof IEnemyMachine || (entity instanceof EntityTF2Character && ((EntityTF2Character) entity).isRobot())
                || world.getWorldTime() < 1200000)) event.setResult(Event.Result.DENY);
    }

}
