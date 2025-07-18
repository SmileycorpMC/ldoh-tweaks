package net.smileycorp.ldoh.common.events;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
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
import net.minecraft.util.math.BlockPos;
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
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.capabilities.*;
import net.smileycorp.ldoh.common.entity.ai.AIModifiedMedigun;
import net.smileycorp.ldoh.common.entity.zombie.EntityTF2Zombie;
import net.smileycorp.ldoh.common.util.ModUtils;
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
import rafradek.TF2weapons.util.TF2Class;

import java.util.ArrayList;
import java.util.List;

public class TF2Events {

    //capability manager
    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        //gives tf2 mercs hunger
        if (!entity.hasCapability(LDOHCapabilities.HUNGER, null) && entity instanceof EntityTF2Character) {
            if (!((EntityTF2Character) entity).isRobot())
                event.addCapability(Constants.loc("Hunger"), new IHunger.Provider());
        }
        //give medics ability to cure
        if (!entity.hasCapability(LDOHCapabilities.CURING, null) && entity instanceof EntityMedic) {
            event.addCapability(Constants.loc("Curing"), new ICuring.Provider());
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
                        newentity.renderYawOffset = entity.renderYawOffset;
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
                //prevent spy easter egg from forcing player teams
                if (entity instanceof EntityTF2Character & !(entity instanceof EntitySpy)) {
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
            if (!((EntityTF2Character) entity).isRobot()) {
                IHunger hunger = entity.getCapability(LDOHCapabilities.HUNGER, null);
                hunger.onUpdate((EntityLiving) entity);
                if (entity.motionX > 0.2 || entity.motionY > 0.2 || entity.motionZ > 0.2)
                    hunger.onUpdate((EntityLiving) entity);
            }
        }
        if (entity instanceof EntityTF2Character & !world.isRemote) {
            EntityTF2Character merc = (EntityTF2Character) entity;
            TF2Class tfClass = merc.getTF2Class();
            //gifting ammo and food to tf2 characters
            for (EntityItem item : world.getEntitiesWithinAABB(EntityItem.class, merc.getEntityBoundingBox())) {
                ItemStack stack = item.getItem();
                if (stack.getItem() instanceof ItemAmmo) {
                    //check entity ammo slots to see if it can pick the items up
                    ItemStackHandler ammo = merc.refill;
                    if (ammo != null) {
                        ItemStack ammostack = ammo.getStackInSlot(0);
                        if (ammostack == null || ammostack.isEmpty()) {
                            ammo.setStackInSlot(0, stack);
                            item.setDead();
                        } else if (ammostack.getItem() == stack.getItem()) {
                            int count = ammostack.getCount() + stack.getCount();
                            if (count > 64) {
                                ItemStack ret = ammostack.copy();
                                ret.setCount(64);
                                ammo.setStackInSlot(0, ret);
                                stack.setCount(count - 64);
                            } else {
                                ItemStack ret = ammostack.copy();
                                ret.setCount(count);
                                ammo.setStackInSlot(0, ret);
                            }
                        }
                    }
                }
                //gifting weapons
                if (ItemFromData.isItemOfClass(ItemFromData.getData(stack), tfClass)) {
                    InventoryLoadout loadout = merc.loadout;
                    for (int i = 0; i < 4; i++) {
                        if (merc.loadout.getStackInSlot(i).isEmpty() && ItemFromData.isItemOfClassSlot(ItemFromData.getData(stack), i, tfClass)) {
                            loadout.insertItem(i, stack, false);
                            item.setDead();
                        }
                    }
                }
                if (merc.hasCapability(LDOHCapabilities.HUNGER, null) & !merc.isRobot()) {
                    stack = merc.getCapability(LDOHCapabilities.HUNGER, null).tryPickupFood(stack, merc);
                }
                if (merc.hasCapability(LDOHCapabilities.CURING, null)) {
                    stack = merc.getCapability(LDOHCapabilities.CURING, null).tryPickupSyringe(stack, merc);
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
        if (world.isRemote) return;
        if (!InfectionRegister.canCauseInfection(attacker) | !(entity instanceof EntityTF2Character)) return;
        if (((EntityTF2Character) entity).isRobot() || entity.isPotionActive(HordesInfection.INFECTED)) return;
        entity.addPotionEffect(new PotionEffect(HordesInfection.INFECTED, 10000, 0));
    }

    //hooks into the hordes infection event
    @SubscribeEvent
    public void onInfect(InfectionDeathEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.world;
        if (entity instanceof EntityTF2Character) {
            //check the entity isn't a robot
            if (!((EntityTF2Character) entity).isRobot()) {
                //spawns a tf2 zombie in the place of the dead merc
                EntityTF2Zombie zombie = new EntityTF2Zombie((EntityTF2Character) entity);
                world.spawnEntity(zombie);
                zombie.setPosition(entity.posX, entity.posY, entity.posZ);
                zombie.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
                entity.setDead();
                event.setResult(Result.DENY);
            }
        }
    }

    @SuppressWarnings({})
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        World world = event.getWorld();
        Entity entity = event.getEntity();
        if (!world.isRemote) {
            if (entity instanceof EntitySentry) {
                EntitySentry sentry = (EntitySentry) entity;
                if (sentry.getOwner() != null && sentry.getOwner().getTeam() != null)
                    world.getScoreboard().addPlayerToTeam(sentry.getCachedUniqueIdString(), sentry.getOwner().getTeam().getName());
                sentry.targetTasks.taskEntries.clear();
                sentry.targetTasks.addTask(2, new EntityAISpotTarget<EntityLivingBase>(sentry, EntityLivingBase.class, true, true,
                        e -> ModUtils.canTarget(sentry, e), false, true));
            }
            if (entity instanceof EntityTF2Character) {
                EntityTF2Character merc = (EntityTF2Character) entity;
                //makes tf2 mercs avoid zombies more
                merc.tasks.addTask(3, new EntityAIAvoidEntity<EntityMob>(merc, EntityMob.class, e -> ModUtils.canTarget(merc, e), 5.0F, 0.6D, 0.6D));
                //redo targeting ai
                merc.targetTasks.taskEntries.clear();
                if (entity instanceof EntityMedic) {
                    //medic heal targeting
                    EntityAIBase ai = new EntityAINearestChecked<EntityLivingBase>(merc, EntityLivingBase.class, true, false, e -> ModUtils.shouldHeal(merc, e), false, true) {
                        @Override
                        public boolean shouldExecute() {
                            return ModUtils.shouldHeal(merc, targetEntity);
                        }
                    };
                    merc.targetTasks.addTask(1, ai);
                    merc.targetTasks.addTask(2, new EntityAIHurtByTarget(merc, true));
                    merc.targetTasks.addTask(3, new EntityAINearestChecked<>(merc, EntityLivingBase.class, true, false, e -> ModUtils.canTarget(merc, e), true, false));
                } else {
                    merc.targetTasks.addTask(1, new EntityAIHurtByTarget(merc, true));
                    merc.targetTasks.addTask(2, new EntityAINearestChecked<>(merc, EntityLivingBase.class, true, false, e -> ModUtils.canTarget(merc, e), true, false));
                }
                if (entity.hasCapability(LDOHCapabilities.SPAWN_TRACKER, null)) {
                    ISpawnTracker tracker = entity.getCapability(LDOHCapabilities.SPAWN_TRACKER, null);
                    if (!tracker.isSpawned()) {
                        ((EntityTF2Character)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
                        ((EntityTF2Character)entity).makeOffers();
                        if (entity instanceof EntitySpy && entity.hasCapability(LDOHCapabilities.HUNGER, null)) {
                            entity.getCapability(LDOHCapabilities.HUNGER, null).setFoodStack(new ItemStack(ItemListxlfoodmod.baguette, 8));
                        }
                        tracker.setSpawned(true);
                    }
                } else if (entity instanceof EntityMedic) {
                    List<EntityAIBase> tasks = new ArrayList<>();
                    for (EntityAITaskEntry task : ((EntityTF2Character) entity).tasks.taskEntries) {
                        EntityAIBase ai = task.action;
                        if (ai instanceof EntityAIUseMedigun & !(ai instanceof AIModifiedMedigun)) tasks.add(ai);
                    }
                    if (!tasks.isEmpty()) {
                        for (EntityAIBase task : tasks) ((EntityTF2Character) entity).tasks.removeTask(task);
                        ((EntityTF2Character) entity).tasks.addTask(3, new AIModifiedMedigun(merc));
                    }
                }
                //sync capability data to clients
                if (merc.hasCapability(LDOHCapabilities.HUNGER, null))
                    merc.getCapability(LDOHCapabilities.HUNGER, null).syncClients(merc);
                if (merc.hasCapability(LDOHCapabilities.CURING, null))
                    merc.getCapability(LDOHCapabilities.CURING, null).syncClients(merc);
            //give persistence to tf2 buildings
            } else if (entity instanceof EntityBuilding && entity.hasCapability(LDOHCapabilities.SPAWN_TRACKER, null)) {
                ISpawnTracker tracker = entity.getCapability(LDOHCapabilities.SPAWN_TRACKER, null);
                if (!tracker.isSpawned()) {
                    ((EntityBuilding) entity).enablePersistence();
                    tracker.setSpawned(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void playerTrack(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof EntityLiving && event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityLiving entity = (EntityLiving) event.getTarget();
            if (entity.hasCapability(LDOHCapabilities.HUNGER, null))
                entity.getCapability(LDOHCapabilities.HUNGER, null).syncClient(entity, (EntityPlayerMP) event.getEntityPlayer());
            if (entity.hasCapability(LDOHCapabilities.CURING, null))
                entity.getCapability(LDOHCapabilities.CURING, null).syncClient(entity, (EntityPlayerMP) event.getEntityPlayer());
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
            if (cap.isFollowing((EntityLiving) entity)) {
                cap.stopFollowing((EntityLiving) entity);
                event.setCanceled(true);
            }
        }
    }
    
}
