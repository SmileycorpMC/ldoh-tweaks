package net.smileycorp.ldoh.integration.tektopia;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfVillager;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.hordes.common.event.InfectionDeathEvent;
import net.smileycorp.hordes.infection.HordesInfection;
import net.smileycorp.hordes.infection.InfectionRegister;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.events.RegistryEvents;
import net.smileycorp.ldoh.integration.tektopia.entities.EntityLDOHArchitect;
import net.smileycorp.ldoh.integration.tektopia.entities.EntityLDOHTradesman;
import net.tangotek.tektopia.ModItems;
import net.tangotek.tektopia.VillageManager;
import net.tangotek.tektopia.entities.EntityArchitect;
import net.tangotek.tektopia.entities.EntityNecromancer;
import net.tangotek.tektopia.entities.EntityTradesman;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;

public class TektopiaEvents {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        RegistryEvents.registerItem(event.getRegistry(), TektopiaUtils.TF2_PROF_TOKEN);
    }

    //capability manager
    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        //give mercs home village
        if (!entity.hasCapability(TektopiaUtils.VILLAGE_DATA, null) && entity instanceof EntityTF2Character) {
            event.addCapability(Constants.loc("VillageData"), new IVillageData.Provider());
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        World world = event.getWorld();
        Entity entity = event.getEntity();
        if (entity instanceof EntityNecromancer) event.setCanceled(true);
        //replaces architect with our own version that has our modified trades
        if (entity instanceof EntityArchitect & !(entity instanceof EntityLDOHArchitect)) {
            EntityLDOHArchitect architect = new EntityLDOHArchitect(world, (EntityArchitect) entity);
            architect.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), (IEntityLivingData) null);
            world.spawnEntity(architect);
            event.setCanceled(true);
        }
        //replaces tradesman with our own version that has our modified trades
        else if (entity instanceof EntityTradesman & !(entity instanceof EntityLDOHTradesman)) {
            EntityLDOHTradesman tradesman = new EntityLDOHTradesman(world, (EntityTradesman) entity);
            tradesman.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), (IEntityLivingData) null);
            world.spawnEntity(tradesman);
            event.setCanceled(true);
        }
        if (entity.hasCapability(TektopiaUtils.VILLAGE_DATA, null)) {
            IVillageData cap = entity.getCapability(TektopiaUtils.VILLAGE_DATA, null);
            if (cap.shouldHaveVillage() & !cap.hasVillage()) {
                VillageManager villages = VillageManager.get(world);
                cap.setVillage(villages);
            }
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
    }

    //Player ticks
    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            World world = player.world;
            if (!world.isRemote) {
                if (world.getWorldTime() >= 1080000 & !GameStageHelper.hasStage(player, "town")) {
                    GameStageHelper.addStage(player, "town");
                    ITextComponent survivor = new TextComponentTranslation(Constants.VILLAGER_MESSAGE + ".Survivor");
                    survivor.setStyle(new Style().setBold(true));
                    player.sendMessage(new TextComponentTranslation(Constants.VILLAGER_MESSAGE + "0", survivor));
                    ITextComponent token = new TextComponentTranslation(ModItems.structureTownHall.getUnlocalizedName());
                    token.setStyle(new Style().setColor(TextFormatting.GREEN).setBold(true));
                    player.sendMessage(new TextComponentTranslation(Constants.VILLAGER_MESSAGE + "1", token));
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        int ID = 301;
        IForgeRegistry<EntityEntry> registry = event.getRegistry();
        EntityEntry ARCHITECT = EntityEntryBuilder.create().entity(EntityLDOHArchitect.class)
                .id(Constants.loc("architect"), ID++)
                .name("villager.architect").tracker(80, 3, true).build();
        registry.register(ARCHITECT);
        EntityEntry TRADESMAN = EntityEntryBuilder.create().entity(EntityLDOHTradesman.class)
                .id(Constants.loc("tradesman"), ID++)
                .name("villager.tradesman").tracker(80, 3, true).build();
        registry.register(TRADESMAN);
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
                if (entity instanceof EntityVillagerTek) {
                    //gives the infection effect
                    entity.addPotionEffect(new PotionEffect(HordesInfection.INFECTED, 10000, 0));
                }
            }
        }
    }

}
