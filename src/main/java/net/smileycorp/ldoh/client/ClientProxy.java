package net.smileycorp.ldoh.client;

import com.mrcrayfish.guns.client.gui.DisplayProperty;
import com.mrcrayfish.guns.client.gui.GuiWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.ldoh.client.colour.BlockTurretColour;
import net.smileycorp.ldoh.client.colour.ItemEggColour;
import net.smileycorp.ldoh.client.entity.*;
import net.smileycorp.ldoh.client.tesr.TESRBarbedWire;
import net.smileycorp.ldoh.client.tesr.TESRFilingCabinet;
import net.smileycorp.ldoh.client.tesr.TESRTurretItem;
import net.smileycorp.ldoh.common.CommonProxy;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.block.LDOHBlocks;
import net.smileycorp.ldoh.common.entity.EntityJuggernaut;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.entity.infphoenix.EntityInfPhoenix;
import net.smileycorp.ldoh.common.entity.zombie.*;
import net.smileycorp.ldoh.common.events.RegistryEvents;
import net.smileycorp.ldoh.common.item.ItemBlockMeta;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;
import net.smileycorp.ldoh.common.tile.TileFilingCabinet;
import net.smileycorp.ldoh.integration.mobends.LDOHMobendsAddon;

@EventBusSubscriber(value = Side.CLIENT, modid = ModDefinitions.MODID)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(new ClientEventListener());
        //register our random mobs support to the reload listener
        //should probably prevent this when optifine is installed
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

    //colour our custom spawn egg
    @SubscribeEvent
    public static void itemColourHandler(ColorHandlerEvent.Item event) {
        ItemColors registry = event.getItemColors();
        registry.registerItemColorHandler(new ItemEggColour(), LDOHItems.SPAWNER);
    }

    //register turret australium rendering
    @SubscribeEvent
    public static void blockColourHandler(ColorHandlerEvent.Block event) {
        BlockColors registry = event.getBlockColors();
        registry.registerBlockColorHandler(new BlockTurretColour(), LDOHBlocks.TURRET);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        //register entity renderers
        RenderingRegistry.registerEntityRenderingHandler(EntityCrawlingZombie.class, m -> new RenderCrawlingZombie(m, "zombie"));
        RenderingRegistry.registerEntityRenderingHandler(EntityCrawlingHusk.class, m -> new RenderCrawlingZombie(m, "husk"));
        RenderingRegistry.registerEntityRenderingHandler(EntityTF2Zombie.class, RenderTF2Zombie::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityZombieNurse.class, RenderZombieNurse::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySwatZombie.class, m -> new RenderSpecialZombie<>(m, "swat_zombie"));
        RenderingRegistry.registerEntityRenderingHandler(EntityZombieMechanic.class, m -> new RenderSpecialZombie<>(m, "zombie_mechanic"));
        RenderingRegistry.registerEntityRenderingHandler(EntityZombieTechnician.class, m -> new RenderSpecialZombie<>(m, "zombie_technician"));
        RenderingRegistry.registerEntityRenderingHandler(EntityTurret.class, RenderTurret::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityZombieFireman.class, RenderZombieFireman::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInfPhoenix.class, RenderInfPhoenix::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityJuggernaut.class, RenderJuggernaut::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityReaver.class, RenderReaver::new);
        //handle custom mapping for landmine blockstates
        ModelLoader.setCustomStateMapper(LDOHBlocks.LANDMINE, new StateMapperLandmine());
        //register item models
        for (Item item : RegistryEvents.ITEMS) {
            if (item instanceof IMetaItem) {
                for (int i = 0; i < ((IMetaItem) item).getMaxMeta(); i++) {
                    ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(ModDefinitions.getResource((item instanceof ItemBlockMeta ? "" : "items/") + item.getRegistryName().getResourcePath()), ((IMetaItem) item).byMeta(i)));
                }
            } else
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
        }
        //register renderer for barbed wire healthbar
        ClientRegistry.bindTileEntitySpecialRenderer(TileBarbedWire.class, new TESRBarbedWire());
        //register renderer for filing cabinet items
        ClientRegistry.bindTileEntitySpecialRenderer(TileFilingCabinet.class, new TESRFilingCabinet());
        //register turret item renderer
        Item.getItemFromBlock(LDOHBlocks.TURRET).setTileEntityItemStackRenderer(new TESRTurretItem());
        //add incendiary ammo rendering properties to gun workbench
        GuiWorkbench.addDisplayProperty(new ItemStack(LDOHItems.INCENDIARY_AMMO), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
        ModelResourceLocation loc = new ModelResourceLocation(ModDefinitions.getResource("turret"), "normal");
        //register our turret item renderer
        TESRTurretItem renderer = (TESRTurretItem) Item.getItemFromBlock(LDOHBlocks.TURRET).getTileEntityItemStackRenderer();
        registry.putObject(loc, renderer.new WrappedBakedModel(registry.getObject(loc)));
    }

}
