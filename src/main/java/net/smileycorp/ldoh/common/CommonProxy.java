package net.smileycorp.ldoh.common;

import com.mrcrayfish.furniture.init.FurnitureBlocks;
import com.mrcrayfish.furniture.init.FurnitureItems;
import com.mrcrayfish.guns.common.WorkbenchRegistry;
import com.mrcrayfish.guns.item.AmmoRegistry;
import com.mrcrayfish.guns.item.ItemAmmo;
import ichttt.mods.firstaid.api.distribution.DamageDistributionBuilderFactory;
import ichttt.mods.firstaid.api.enums.EnumPlayerPart;
import ivorius.reccomplex.events.RCEventBus;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.smileycorp.ldoh.client.gui.GuiTurret;
import net.smileycorp.ldoh.common.capabilities.*;
import net.smileycorp.ldoh.common.capabilities.IApocalypseBoss.ApocalypseBoss;
import net.smileycorp.ldoh.common.capabilities.IBreakBlocks.BreakBlocks;
import net.smileycorp.ldoh.common.capabilities.ICuring.Curing;
import net.smileycorp.ldoh.common.capabilities.IFollowers.Followers;
import net.smileycorp.ldoh.common.capabilities.IHunger.Hunger;
import net.smileycorp.ldoh.common.capabilities.ISpawnTracker.SpawnTracker;
import net.smileycorp.ldoh.common.capabilities.IUnburiedSpawner.UnburiedSpawner;
import net.smileycorp.ldoh.common.capabilities.IVillageData.VillageData;
import net.smileycorp.ldoh.common.command.CommandBossEvent;
import net.smileycorp.ldoh.common.command.CommandHandDebug;
import net.smileycorp.ldoh.common.command.CommandSpawnRaid;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.entity.zombie.EntityIncendiaryProjectile;
import net.smileycorp.ldoh.common.events.*;
import net.smileycorp.ldoh.common.inventory.ContainerTurret;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.network.PacketHandler;
import net.smileycorp.ldoh.common.tile.TileTurret;
import net.smileycorp.ldoh.common.util.TurretUpgrade;
import net.smileycorp.ldoh.integration.tektopia.TektopiaEvents;
import rafradek.TF2weapons.item.crafting.TF2CraftingManager;

import java.util.Objects;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.config = new Configuration(event.getSuggestedConfigurationFile());
        ConfigHandler.syncConfig();

        //Register event listeners
        MinecraftForge.EVENT_BUS.register(new ApocalypseEvents());
        MinecraftForge.EVENT_BUS.register(new EntityEvents());
        MinecraftForge.EVENT_BUS.register(new PlayerEvents());
        MinecraftForge.EVENT_BUS.register(new SpawnerEvents());
        MinecraftForge.EVENT_BUS.register(new TF2Events());
        MinecraftForge.EVENT_BUS.register(new WorldEvents());
        MinecraftForge.ORE_GEN_BUS.register(new WorldEvents());
        MinecraftForge.EVENT_BUS.register(new DefenseEvents());
        RCEventBus.INSTANCE.register(new WorldEvents());
        if (Loader.isModLoaded("tektopia")) MinecraftForge.EVENT_BUS.register(new TektopiaEvents());

        //Add Safehouse loot tables
        LootTableList.register(ModDefinitions.SAFEHOUSE_CHEST);
        LootTableList.register(ModDefinitions.SAFEHOUSE_CABINET);
        LootTableList.register(ModDefinitions.SAFEHOUSE_MEDICAL_FRIDGE);
        LootTableList.register(ModDefinitions.SAFEHOUSE_FRIDGE);
        LootTableList.register(ModDefinitions.SAFEHOUSE_CRATE);
        LootTableList.register(ModDefinitions.NEST_CRATE);
        LootTableList.register(ModDefinitions.MILITARY_CRATE);
        LootTableList.register(ModDefinitions.MILITARY_AMMO);
        LootTableList.register(ModDefinitions.MILITARY_TREASURE);
        LootTableList.register(ModDefinitions.TECH_CRATE);
        LootTableList.register(ModDefinitions.EIFFEL_GOOD_GOOD);

        //Setup Packets for use
        PacketHandler.initPackets();

        //register cfm recipe handler
        FMLInterModComms.sendMessage("cfm", "register", "net.smileycorp.ldoh.common.events.RegistryEvents.registerCFMRecipes");

        //first aid damage

        DamageDistributionBuilderFactory factory = DamageDistributionBuilderFactory.getInstance();
        
        factory.newStandardBuilder()
                .addDistributionLayer(EntityEquipmentSlot.CHEST, EnumPlayerPart.BODY)
                .disableNeighbourRestDistribution()
                .registerStatic(LDOHTweaks.TOXIC_GAS_DAMAGE);

        factory.newStandardBuilder()
                .addDistributionLayer(EntityEquipmentSlot.FEET, EnumPlayerPart.LEFT_FOOT, EnumPlayerPart.RIGHT_FOOT)
                .addDistributionLayer(EntityEquipmentSlot.LEGS, EnumPlayerPart.LEFT_LEG, EnumPlayerPart.RIGHT_LEG)
                .addDistributionLayer(EntityEquipmentSlot.CHEST, EnumPlayerPart.BODY)
                .registerStatic(LDOHTweaks.SHRAPNEL_DAMAGE);
    }

    public void init(FMLInitializationEvent event) {
        //Register Capabilities
        CapabilityManager.INSTANCE.register(ISpawnTracker.class, new ISpawnTracker.Storage(), SpawnTracker::new);
        CapabilityManager.INSTANCE.register(IBreakBlocks.class, new IBreakBlocks.Storage(), () -> new BreakBlocks(null));
        CapabilityManager.INSTANCE.register(IUnburiedSpawner.class, new IUnburiedSpawner.Storage(), () -> new UnburiedSpawner(null));
        CapabilityManager.INSTANCE.register(IMiniRaid.class, new IMiniRaid.Storage(), MiniRaid::new);
        CapabilityManager.INSTANCE.register(IHunger.class, new IHunger.Storage(), Hunger::new);
        CapabilityManager.INSTANCE.register(IApocalypse.class, new IApocalypse.Storage(),
                () -> ConfigHandler.legacyApocalypse ? new LegacyApocalypse(null) : new Apocalypse(null));
        CapabilityManager.INSTANCE.register(IFollowers.class, new IFollowers.Storage(), Followers::new);
        CapabilityManager.INSTANCE.register(ICuring.class, new ICuring.Storage(), Curing::new);
        CapabilityManager.INSTANCE.register(IVillageData.class, new IVillageData.Storage(), VillageData::new);
        CapabilityManager.INSTANCE.register(IApocalypseBoss.class, new IApocalypseBoss.Storage(), () -> new ApocalypseBoss());

        //register turret gui
        NetworkRegistry.INSTANCE.registerGuiHandler(LDOHTweaks.INSTANCE, new IGuiHandler() {

            @Override
            public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
                if (id == 0) {
                    TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
                    if (te instanceof TileTurret) {
                        EntityTurret turret = ((TileTurret) te).getEntity();
                        if (turret != null) return new ContainerTurret(turret, player);
                    }
                }
                return null;
            }

            @Override
            public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                if (ID == 0) {
                    TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
                    if (te instanceof TileTurret) {
                        EntityTurret turret = ((TileTurret) te).getEntity();
                        if (turret != null) return new GuiTurret(turret, player);
                    }
                }
                return null;
            }

        });
        Item.getItemFromBlock(FurnitureBlocks.CRATE).setMaxStackSize(1);
        Item.getItemFromBlock(FurnitureBlocks.CRATE_SPRUCE).setMaxStackSize(1);
        Item.getItemFromBlock(FurnitureBlocks.CRATE_BIRCH).setMaxStackSize(1);
        Item.getItemFromBlock(FurnitureBlocks.CRATE_JUNGLE).setMaxStackSize(1);
        Item.getItemFromBlock(FurnitureBlocks.CRATE_ACACIA).setMaxStackSize(1);
        Item.getItemFromBlock(FurnitureBlocks.CRATE_DARK_OAK).setMaxStackSize(1);
        FurnitureItems.CROWBAR.setMaxStackSize(1);
        Material.DRAGON_EGG.setRequiresTool();

        //add incendiary ammo
        AmmoRegistry.getInstance().registerProjectileFactory((ItemAmmo) LDOHItems.INCENDIARY_AMMO, EntityIncendiaryProjectile::new);
        WorkbenchRegistry.registerRecipe(new ItemStack(LDOHItems.INCENDIARY_AMMO, 16), new ItemStack(Items.GUNPOWDER),
                new ItemStack(Items.IRON_NUGGET, 8), new ItemStack(Items.GLOWSTONE_DUST));
        //add australium turret upgrade
        TF2CraftingManager.INSTANCE.addRecipe(new ShapedOreRecipe(ModDefinitions.getResource("austrailum_turret_upgrade"), TurretUpgrade.AUSTRALIUM.getItem(),
                new Object[]{"III", "IUI", "III", 'I', "ingotAustralium", 'U', TurretUpgrade.BLANK.getItem()}));
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void serverStart(FMLServerStartingEvent event) {
        //Register Boss Command
        event.registerServerCommand(new CommandBossEvent());
        //Register mini raids command
        event.registerServerCommand(new CommandSpawnRaid());

        event.registerServerCommand(new CommandHandDebug());
    }

}
