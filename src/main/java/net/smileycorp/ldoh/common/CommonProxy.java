package net.smileycorp.ldoh.common;

import ivorius.reccomplex.events.RCEventBus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
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
import net.smileycorp.ldoh.common.capabilities.IExhaustion;
import net.smileycorp.ldoh.common.capabilities.IExhaustion.Exhaustion;
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
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.events.ApocalypseEvents;
import net.smileycorp.ldoh.common.events.EntityEvents;
import net.smileycorp.ldoh.common.events.PlayerEvents;
import net.smileycorp.ldoh.common.events.SpawnerEvents;
import net.smileycorp.ldoh.common.events.TF2Events;
import net.smileycorp.ldoh.common.events.TektopiaEvents;
import net.smileycorp.ldoh.common.events.WorldEvents;
import net.smileycorp.ldoh.common.inventory.ContainerTurret;
import net.smileycorp.ldoh.common.network.PacketHandler;
import net.smileycorp.ldoh.common.tile.TileTurret;
import net.smileycorp.ldoh.integration.mobends.LDOHMobendsAddon;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
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
		LootTableList.register(ModDefinitions.SAFEHOUSE_FRIDGE);
		//Setup Packets for use
		PacketHandler.initPackets();
	}

	public void init(FMLInitializationEvent event) {
		//Mobends support for nurse model
		if (Loader.isModLoaded("mobends")) new LDOHMobendsAddon().register();
		//Register Capabilities
		CapabilityManager.INSTANCE.register(ISpawnTracker.class, new ISpawnTracker.Storage(), () -> new SpawnTracker());
		CapabilityManager.INSTANCE.register(IBreakBlocks.class, new IBreakBlocks.Storage(), () -> new BreakBlocks(null));
		CapabilityManager.INSTANCE.register(IUnburiedSpawner.class, new IUnburiedSpawner.Storage(), () -> new UnburiedSpawner(null));
		CapabilityManager.INSTANCE.register(IMiniRaid.class, new IMiniRaid.Storage(), () -> new MiniRaid());
		CapabilityManager.INSTANCE.register(IHunger.class, new IHunger.Storage(), () -> new Hunger());
		CapabilityManager.INSTANCE.register(IApocalypse.class, new IApocalypse.Storage(), () -> new Apocalypse(null));
		CapabilityManager.INSTANCE.register(IFollowers.class, new IFollowers.Storage(), () -> new Followers());
		CapabilityManager.INSTANCE.register(ICuring.class, new ICuring.Storage(), () -> new Curing());
		CapabilityManager.INSTANCE.register(IExhaustion.class, new IExhaustion.Storage(), () -> new Exhaustion());
		CapabilityManager.INSTANCE.register(IVillageData.class, new IVillageData.Storage(), () -> new VillageData());
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
