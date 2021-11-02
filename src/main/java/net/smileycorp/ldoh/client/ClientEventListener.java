package net.smileycorp.ldoh.client;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.client.RenderingUtils;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.ldoh.client.entity.RenderCrawlingZombie;
import net.smileycorp.ldoh.client.entity.RenderDummy;
import net.smileycorp.ldoh.client.entity.RenderSpecialZombie;
import net.smileycorp.ldoh.client.entity.RenderTFZombie;
import net.smileycorp.ldoh.client.entity.RenderZombieNurse;
import net.smileycorp.ldoh.client.tesr.TESRBarbedWire;
import net.smileycorp.ldoh.common.ModContent;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.entity.EntityCrawlingHusk;
import net.smileycorp.ldoh.common.entity.EntityCrawlingZombie;
import net.smileycorp.ldoh.common.entity.EntityDummy;
import net.smileycorp.ldoh.common.entity.EntitySwatZombie;
import net.smileycorp.ldoh.common.entity.EntityTFZombie;
import net.smileycorp.ldoh.common.entity.EntityZombieMechanic;
import net.smileycorp.ldoh.common.entity.EntityZombieNurse;
import net.smileycorp.ldoh.common.entity.EntityZombieTechnician;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;

import org.lwjgl.util.vector.Vector3f;

@EventBusSubscriber(modid=ModDefinitions.modid, value=Side.CLIENT)
public class ClientEventListener {

	public static String title = "";
	public static int starttime = 0;

	public static Color GAS_COLOUR = new Color(0.917647059f, 1f, 0.0470588235f, 0.2f);
	public static ResourceLocation GAS_TEXTURE = ModDefinitions.getResource("textures/misc/gas.png");

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		//register renderer for barbed wire healthbar
		ClientRegistry.bindTileEntitySpecialRenderer(TileBarbedWire.class, new TESRBarbedWire());
		//register entity renderers
		RenderingRegistry.registerEntityRenderingHandler(EntityCrawlingZombie.class, m -> new RenderCrawlingZombie(m, new ResourceLocation("textures/entity/zombie/zombie.png")));
		RenderingRegistry.registerEntityRenderingHandler(EntityCrawlingHusk.class, m -> new RenderCrawlingZombie(m, new ResourceLocation("textures/entity/zombie/husk.png")));
		RenderingRegistry.registerEntityRenderingHandler(EntityTFZombie.class, m -> new RenderTFZombie(m));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieNurse.class, m -> new RenderZombieNurse(m));
		RenderingRegistry.registerEntityRenderingHandler(EntitySwatZombie.class, m -> new RenderSpecialZombie<EntitySwatZombie>(m, "swat_zombie"));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieMechanic.class, m -> new RenderSpecialZombie<EntityZombieMechanic>(m, "zombie_mechanic"));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieTechnician.class, m -> new RenderSpecialZombie<EntityZombieTechnician>(m, "zombie_technician"));
		RenderingRegistry.registerEntityRenderingHandler(EntityDummy.class, m -> new RenderDummy(m));
		//handle custom mapping for landmine blockstates
		ModelLoader.setCustomStateMapper(ModContent.LANDMINE, new StateMapperLandmine());
		//register item models
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

	//colour our custom spawn egg
	@SubscribeEvent
	public static void itemColourHandler(ColorHandlerEvent.Item event) {
		ItemColors registry = event.getItemColors();
		registry.registerItemColorHandler(new ItemEggColour(), ModContent.SPAWNER);
	}

	//Render Gas Overlay when below gas level
	@SubscribeEvent
	public void postRenderOverlay(RenderGameOverlayEvent.Pre event){
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.player;
		if (player!= null && event.getType() == ElementType.ALL) {
			if (player.getPosition().getY()<=29.2) {
				int r = GAS_COLOUR.getRed();
				int g = GAS_COLOUR.getGreen();
				int b = GAS_COLOUR.getBlue();
				int a = GAS_COLOUR.getAlpha();
				final double x = player.lastTickPosX + ((player.posX - player.lastTickPosX) * event.getPartialTicks());
				final double y = player.lastTickPosY + ((player.posY - player.lastTickPosY) * event.getPartialTicks());
				final double z = player.lastTickPosZ + ((player.posZ - player.lastTickPosZ) * event.getPartialTicks());
				float f = 1 / 32 /10000;
				int height = mc.displayHeight;
				int width = mc.displayWidth;
				GlStateManager.pushMatrix();
				GlStateManager.disableLighting();
				GlStateManager.enableBlend();
				GlStateManager.disableAlpha();
				GlStateManager.disableTexture2D();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				mc.getTextureManager().bindTexture(GAS_TEXTURE);
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferbuilder = tessellator.getBuffer();
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
				bufferbuilder.pos(0, height, 0).tex(0, height * f).color(r, g, b, a).normal((float)x, (float)y, (float)z).endVertex();
				bufferbuilder.pos(width, height, 0).tex(width * f, height * f).color(r, g, b, a).normal((float)x, (float)y, (float)z).endVertex();
				bufferbuilder.pos(width, 0, 0).tex(width * f, 0).color(r, g, b, a).normal((float)x, (float)y, (float)z).endVertex();
				bufferbuilder.pos(0, 0, 0).tex(0, 0).color(r, g, b, a).normal((float)x, (float)y, (float)z).endVertex();
				tessellator.draw();
				GlStateManager.disableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.disableLighting();
				GlStateManager.enableTexture2D();
				GlStateManager.popMatrix();
			}
		}
	}

	//Render gas layer in world when above gas level
	@SubscribeEvent
	public static void onRenderWorldLastEvent(RenderWorldLastEvent event)  {
		Minecraft mc = Minecraft.getMinecraft();
		Entity entity = mc.getRenderViewEntity();
		if (entity != null) {
			if (entity.posY >= 29.2) {
				RenderManager rm = mc.getRenderManager();
				//scale renderer base on render distance
				int size = rm.options == null ? 0 : rm.options.renderDistanceChunks*16;
				int r = GAS_COLOUR.getRed();
				int g = GAS_COLOUR.getGreen();
				int b = GAS_COLOUR.getBlue();
				int a = GAS_COLOUR.getAlpha() + 40;
				//coords for the centre of the current chunk
				int cx = ((int) Math.floor(entity.posX/16))*16 + 8;
				int cz = ((int) Math.floor(entity.posZ/16))*16 + 8;
				final double x = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * event.getPartialTicks());
				final double y = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * event.getPartialTicks());
				final double z = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * event.getPartialTicks());
				GlStateManager.pushMatrix();
				GlStateManager.disableLighting();
				GlStateManager.enableBlend();
				GlStateManager.disableAlpha();
				GlStateManager.disableTexture2D();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				RenderingUtils.drawQuad(new Vec3d(cx-x+0.5-size, 31-y - 0.1, cz-z+0.5-size), new Vec3d(cx-x+0.5+size, 31-y - 0.1, cz-z+0.5+size), GAS_TEXTURE, 32,
						new Color(r, g, b, a), new Vector3f(cx-size, (float) y, cz-size), new Vector3f(cx-size, (float) y, cz-size));
				GlStateManager.disableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.disableLighting();
				GlStateManager.enableTexture2D();
				GlStateManager.popMatrix();
			}
		}
	}

}
