package net.smileycorp.ldoh.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.ldoh.common.CommonProxy;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.integration.mobends.LDOHMobendsAddon;

@EventBusSubscriber(value = Side.CLIENT, modid = ModDefinitions.MODID)
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		MinecraftForge.EVENT_BUS.register(new ClientEventListener());
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(RandomTextureCache.INSTANCE);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		//Mobends support for entity models
		if (Loader.isModLoaded("mobends")) new LDOHMobendsAddon().register();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

}
