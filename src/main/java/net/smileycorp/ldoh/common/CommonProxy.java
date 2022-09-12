package net.smileycorp.ldoh.common;

import com.mrcrayfish.furniture.init.FurnitureBlocks;
import com.mrcrayfish.furniture.init.FurnitureItems;
import com.mrcrayfish.guns.common.WorkbenchRegistry;
import com.mrcrayfish.guns.item.AmmoRegistry;
import com.mrcrayfish.guns.item.ItemAmmo;

import ivorius.reccomplex.events.RCEventBus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.smileycorp.ldoh.client.gui.GuiTurret;
import net.smileycorp.ldoh.common.capabilities.Apocalypse;
import net.smileycorp.ldoh.common.capabilities.IApocalypse;
import net.smileycorp.ldoh.common.capabilities.IBreakBlocks;
import net.smileycorp.ldoh.common.capabilities.IBreakBlocks.BreakBlocks;
import net.smileycorp.ldoh.common.capabilities.ICuring;
import net.smileycorp.ldoh.common.capabilities.ICuring.Curing;
import net.smileycorp.ldoh.common.capabilities.IFollowers;
import net.smileycorp.ldoh.common.capabilities.IFollowers.Followers;
import net.smileycorp.ldoh.common.capabilities.IHunger;
import net.smileycorp.ldoh.common.capabilities.IHunger.Hunger;
import net.smileycorp.ldoh.common.capabilities.IMiniRaid;
import net.smileycorp.ldoh.common.capabilities.ISpawnTracker;
import net.smileycorp.ldoh.common.capabilities.ISpawnTracker.SpawnTracker;
import net.smileycorp.ldoh.common.capabilities.IUnburiedSpawner;
import net.smileycorp.ldoh.common.capabilities.IUnburiedSpawner.UnburiedSpawner;
import net.smileycorp.ldoh.common.capabilities.IVillageData;
import net.smileycorp.ldoh.common.capabilities.IVillageData.VillageData;
import net.smileycorp.ldoh.common.capabilities.MiniRaid;
import net.smileycorp.ldoh.common.command.CommandBossEvent;
import net.smileycorp.ldoh.common.command.CommandSpawnRaid;
import net.smileycorp.ldoh.common.entity.EntityIncendiaryProjectile;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.events.ApocalypseEvents;
import net.smileycorp.ldoh.common.events.EntityEvents;
import net.smileycorp.ldoh.common.events.PlayerEvents;
import net.smileycorp.ldoh.common.events.SpawnerEvents;
import net.smileycorp.ldoh.common.events.TF2Events;
import net.smileycorp.ldoh.common.events.TektopiaEvents;
import net.smileycorp.ldoh.common.events.WorldEvents;
import net.smileycorp.ldoh.common.inventory.ContainerTurret;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.network.PacketHandler;
import net.smileycorp.ldoh.common.tile.TileTurret;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.config = new Configuration(event.getSuggestedConfigurationFile());
		ConfigHandler.syncConfig();

		//Register event listeners
		MinecraftForge.EVENT_BUS.register(new ApocalypseEvents());
		MinecraftForge.EVENT_BUS.register(new EntityEvents());
		MinecraftForge.EVENT_BUS.register(new PlayerEvents());
		MinecraftForge.EVENT_BUS.register(new SpawnerEvents());
		MinecraftForge.EVENT_BUS.register(new TektopiaEvents());
		MinecraftForge.EVENT_BUS.register(new TF2Events());
		MinecraftForge.EVENT_BUS.register(new WorldEvents());
		MinecraftForge.ORE_GEN_BUS.register(new WorldEvents());
		RCEventBus.INSTANCE.register(new WorldEvents());

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

		//Setup Packets for use
		PacketHandler.initPackets();
	}

	public void init(FMLInitializationEvent event) {
		//Register Capabilities
		CapabilityManager.INSTANCE.register(ISpawnTracker.class, new ISpawnTracker.Storage(), () -> new SpawnTracker());
		CapabilityManager.INSTANCE.register(IBreakBlocks.class, new IBreakBlocks.Storage(), () -> new BreakBlocks(null));
		CapabilityManager.INSTANCE.register(IUnburiedSpawner.class, new IUnburiedSpawner.Storage(), () -> new UnburiedSpawner(null));
		CapabilityManager.INSTANCE.register(IMiniRaid.class, new IMiniRaid.Storage(), () -> new MiniRaid());
		CapabilityManager.INSTANCE.register(IHunger.class, new IHunger.Storage(), () -> new Hunger());
		CapabilityManager.INSTANCE.register(IApocalypse.class, new IApocalypse.Storage(), () -> new Apocalypse(null));
		CapabilityManager.INSTANCE.register(IFollowers.class, new IFollowers.Storage(), () -> new Followers());
		CapabilityManager.INSTANCE.register(ICuring.class, new ICuring.Storage(), () -> new Curing());
		CapabilityManager.INSTANCE.register(IVillageData.class, new IVillageData.Storage(), () -> new VillageData());

		//register turret gui
		NetworkRegistry.INSTANCE.registerGuiHandler(LDOHTweaks.INSTANCE, new IGuiHandler() {

			@Override
			public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
				if (id == 0){
					TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
					if (te instanceof TileTurret) {
						EntityTurret turret = ((TileTurret) te).getEntity();
						if (turret!= null) return new ContainerTurret(turret, player);
					}
				}
				return null;
			}

			@Override
			public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
				if (ID == 0){
					TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
					if (te instanceof TileTurret) {
						EntityTurret turret = ((TileTurret) te).getEntity();
						if (turret!= null) return new GuiTurret(turret, player);
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

		//add incendiary ammo
		AmmoRegistry.getInstance().registerProjectileFactory((ItemAmmo) LDOHItems.INCENDIARY_AMMO, EntityIncendiaryProjectile::new);
		WorkbenchRegistry.registerRecipe(new ItemStack(LDOHItems.INCENDIARY_AMMO, 16), new ItemStack(Items.GUNPOWDER),
				new ItemStack(Items.IRON_NUGGET, 8), new ItemStack(Items.GLOWSTONE_DUST));
	}

	public void postInit(FMLPostInitializationEvent event) {

	}

	public void serverStart(FMLServerStartingEvent event) {
		//Register Boss Command
		event.registerServerCommand(new CommandBossEvent());
		//Register mini raids command
		event.registerServerCommand(new CommandSpawnRaid());
	}

}
