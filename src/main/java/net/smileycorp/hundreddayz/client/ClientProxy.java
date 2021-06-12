package net.smileycorp.hundreddayz.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.hundreddayz.common.CommonProxy;
import net.smileycorp.hundreddayz.common.ModContent;
import net.smileycorp.hundreddayz.common.ModDefinitions;
import net.smileycorp.hundreddayz.common.entity.EntityTFZombie;
import net.smileycorp.hundreddayz.common.entity.EntityZombieNurse;

@EventBusSubscriber(value = Side.CLIENT, modid = ModDefinitions.modid)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		MinecraftForge.EVENT_BUS.register(new ClientEventListener());
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityTFZombie.class, m-> new RenderTFZombie(m));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieNurse.class, m-> new RenderZombieNurse(m));
		for (Item item: ModContent.items) {
			if (item instanceof IMetaItem) {
				for (int i = 0; i < ((IMetaItem) item).getMaxMeta(); i++) {
					ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(ModDefinitions.getResource("items/"+item.getRegistryName().getResourcePath()), ((IMetaItem) item).byMeta(i)));
				}
			} else {
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
			}
		}
		
	}
	
	@SubscribeEvent
	public static void itemColourHandler(ColorHandlerEvent.Item event) {
		ItemColors registry = event.getItemColors();
		registry.registerItemColorHandler(new ItemEggColour(), ModContent.SPAWNER);
	}
}
