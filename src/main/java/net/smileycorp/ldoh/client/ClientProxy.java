package net.smileycorp.ldoh.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.ldoh.client.entity.RenderSpecialZombie;
import net.smileycorp.ldoh.client.entity.RenderTFZombie;
import net.smileycorp.ldoh.client.entity.RenderZombieNurse;
import net.smileycorp.ldoh.client.tesr.TESRBarbedWire;
import net.smileycorp.ldoh.common.CommonProxy;
import net.smileycorp.ldoh.common.ModContent;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.entity.EntityDumbZombie;
import net.smileycorp.ldoh.common.entity.EntitySwatZombie;
import net.smileycorp.ldoh.common.entity.EntityTFZombie;
import net.smileycorp.ldoh.common.entity.EntityZombieMechanic;
import net.smileycorp.ldoh.common.entity.EntityZombieNurse;
import net.smileycorp.ldoh.common.entity.EntityZombieTechnician;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;

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
		ClientRegistry.bindTileEntitySpecialRenderer(TileBarbedWire.class, new TESRBarbedWire());
		RenderingRegistry.registerEntityRenderingHandler(EntityDumbZombie.class, m -> new RenderZombie(m));
		RenderingRegistry.registerEntityRenderingHandler(EntityTFZombie.class, m -> new RenderTFZombie(m));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieNurse.class, m -> new RenderZombieNurse(m));
		RenderingRegistry.registerEntityRenderingHandler(EntitySwatZombie.class, m -> new RenderSpecialZombie(m, "swat_zombie"));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieMechanic.class, m -> new RenderSpecialZombie(m, "zombie_mechanic"));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieTechnician.class, m -> new RenderSpecialZombie(m, "zombie_technician"));
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
