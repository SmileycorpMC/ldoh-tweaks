package net.smileycorp.ldoh.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid=ModDefinitions.modid, name = ModDefinitions.name, version = ModDefinitions.name, dependencies = ModDefinitions.dependencies)
public class LDOHTweaks {
	
	@SidedProxy(clientSide = ModDefinitions.client, serverSide = ModDefinitions.common)
	public static CommonProxy PROXY;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		PROXY.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		PROXY.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postInit(event);
	}
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		PROXY.serverStart(event);
	}
}
