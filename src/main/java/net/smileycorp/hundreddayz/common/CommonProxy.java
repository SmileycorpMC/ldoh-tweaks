package net.smileycorp.hundreddayz.common;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.smileycorp.hordes.infection.InfectionCureHandler;
import net.smileycorp.hundreddayz.common.apocalypse.ApocalypseEventListener;
import net.smileycorp.hundreddayz.common.apocalypse.ApocalypseSpawnTable;
import net.smileycorp.hundreddayz.common.apocalypse.CommandBossEvent;
import net.smileycorp.hundreddayz.common.capability.ISpawnTracker;
import net.smileycorp.hundreddayz.common.capability.ITimeDetector;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		ApocalypseSpawnTable.init();
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new EventListener());
		MinecraftForge.ORE_GEN_BUS.register(new EventListener());
		MinecraftForge.EVENT_BUS.register(new ApocalypseEventListener());
		CommonPacketHandler.initPackets();
	}
	
	public void init(FMLInitializationEvent event) {
		GameRegistry.addSmelting(new ItemStack(ModContent.SYRINGE, 1, 3), new ItemStack(ModContent.SYRINGE, 1, 0), 0.1f);
		//AddonHelper.registerAddon(ModDefinitions.modid, new HundredDayzAddon());
		CapabilityManager.INSTANCE.register(ISpawnTracker.class, new ISpawnTracker.Storage(), new ISpawnTracker.Factory());
		CapabilityManager.INSTANCE.register(ITimeDetector.class, new ITimeDetector.Storage(), new ITimeDetector.Factory());
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		InfectionCureHandler.addCureItem(new ItemStack(ModContent.SYRINGE, 2));
	}
	
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandBossEvent());
	}
}
