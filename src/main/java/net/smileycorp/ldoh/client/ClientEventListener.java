package net.smileycorp.ldoh.client;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.WorldType;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
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
import net.smileycorp.ldoh.client.entity.RenderSpecialZombie;
import net.smileycorp.ldoh.client.entity.RenderTF2Zombie;
import net.smileycorp.ldoh.client.entity.RenderTurret;
import net.smileycorp.ldoh.client.entity.RenderZombieFireman;
import net.smileycorp.ldoh.client.entity.RenderZombieNurse;
import net.smileycorp.ldoh.client.tesr.TESRBarbedWire;
import net.smileycorp.ldoh.client.tesr.TESRTurretItem;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.block.LDOHBlocks;
import net.smileycorp.ldoh.common.capabilities.ICuring;
import net.smileycorp.ldoh.common.capabilities.IHunger;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.smileycorp.ldoh.common.entity.EntityCrawlingHusk;
import net.smileycorp.ldoh.common.entity.EntityCrawlingZombie;
import net.smileycorp.ldoh.common.entity.EntitySwatZombie;
import net.smileycorp.ldoh.common.entity.EntityTF2Zombie;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.entity.EntityZombieFireman;
import net.smileycorp.ldoh.common.entity.EntityZombieMechanic;
import net.smileycorp.ldoh.common.entity.EntityZombieNurse;
import net.smileycorp.ldoh.common.entity.EntityZombieTechnician;
import net.smileycorp.ldoh.common.events.RegistryEvents;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;

import org.lwjgl.util.vector.Vector3f;

import rafradek.TF2weapons.client.gui.inventory.GuiMercenary;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;

import com.mrcrayfish.guns.client.gui.DisplayProperty;
import com.mrcrayfish.guns.client.gui.GuiWorkbench;

@SuppressWarnings("deprecation")
@EventBusSubscriber(modid=ModDefinitions.MODID, value=Side.CLIENT)
public class ClientEventListener {

	public static String title = "";
	public static int starttime = 0;

	public static Color GAS_COLOUR = new Color(0.917647059f, 1f, 0.0470588235f, 0.2f);
	public static ResourceLocation GAS_TEXTURE = ModDefinitions.getResource("textures/misc/gas.png");
	public static ResourceLocation TF_HUNGER_TEXTURE = ModDefinitions.getResource("textures/gui/tf_hunger.png");
	public static ResourceLocation MEDIC_SYRINGES_TEXTURE = ModDefinitions.getResource("textures/gui/medic_syringes.png");

	//colour our custom spawn egg
	@SubscribeEvent
	public static void itemColourHandler(ColorHandlerEvent.Item event) {
		ItemColors registry = event.getItemColors();
		registry.registerItemColorHandler(new ItemEggColour(), LDOHItems.SPAWNER);
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		//register entity renderers
		RenderingRegistry.registerEntityRenderingHandler(EntityCrawlingZombie.class, m -> new RenderCrawlingZombie(m, new ResourceLocation("textures/entity/zombie/zombie.png")));
		RenderingRegistry.registerEntityRenderingHandler(EntityCrawlingHusk.class, m -> new RenderCrawlingZombie(m, new ResourceLocation("textures/entity/zombie/husk.png")));
		RenderingRegistry.registerEntityRenderingHandler(EntityTF2Zombie.class, m -> new RenderTF2Zombie(m));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieNurse.class, m -> new RenderZombieNurse(m));
		RenderingRegistry.registerEntityRenderingHandler(EntitySwatZombie.class, m -> new RenderSpecialZombie<EntitySwatZombie>(m, "swat_zombie"));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieMechanic.class, m -> new RenderSpecialZombie<EntityZombieMechanic>(m, "zombie_mechanic"));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieTechnician.class, m -> new RenderSpecialZombie<EntityZombieTechnician>(m, "zombie_technician"));
		RenderingRegistry.registerEntityRenderingHandler(EntityTurret.class, m -> new RenderTurret(m));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieFireman.class, m -> new RenderZombieFireman(m));
		//handle custom mapping for landmine blockstates
		ModelLoader.setCustomStateMapper(LDOHBlocks.LANDMINE, new StateMapperLandmine());
		//register item models
		for (Item item: RegistryEvents.ITEMS) {
			if (item instanceof IMetaItem) {
				for (int i = 0; i < ((IMetaItem) item).getMaxMeta(); i++) {
					ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(ModDefinitions.getResource("items/"+item.getRegistryName().getResourcePath()), ((IMetaItem) item).byMeta(i)));
				}
			} else ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
		}
		//register renderer for barbed wire healthbar
		ClientRegistry.bindTileEntitySpecialRenderer(TileBarbedWire.class, new TESRBarbedWire());
		//register turret item renderer
		Item.getItemFromBlock(LDOHBlocks.TURRET).setTileEntityItemStackRenderer(new TESRTurretItem());
		GuiWorkbench.addDisplayProperty(new ItemStack(LDOHItems.INCENDIARY_AMMO), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
	}

	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
		ModelResourceLocation loc = new ModelResourceLocation(ModDefinitions.getResource("turret"), "normal");
		TESRTurretItem renderer = (TESRTurretItem) Item.getItemFromBlock(LDOHBlocks.TURRET).getTileEntityItemStackRenderer();
		registry.putObject(loc, renderer.new WrappedBakedModel(registry.getObject(loc)));
	}

	//Render Gas Overlay when below gas level
	@SubscribeEvent
	public void postRenderOverlay(RenderGameOverlayEvent.Pre event){
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.player;
		if (player!= null && event.getType() == ElementType.ALL) {
			if (player.getPosition().getY()<=29.2 && player.world.getWorldType() != WorldType.FLAT) {
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
			if (entity.posY >= 29.2 && entity.world.getWorldType() != WorldType.FLAT) {
				RenderManager rm = mc.getRenderManager();
				//scale renderer base on render distance
				int size = rm.options == null ? 0 : (rm.options.renderDistanceChunks+1)*16;
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
				RenderingUtils.drawQuad(new Vec3d(cx-x+0.5-size, 30.9-y, cz-z+0.5-size), new Vec3d(cx-x+0.5+size, 30.9-y, cz-z+0.5+size), GAS_TEXTURE, 32,
						new Color(r, g, b, a), new Vector3f(cx-size, (float) y, cz-size), new Vector3f(cx-size, (float) y, cz-size));
				GlStateManager.disableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.disableLighting();
				GlStateManager.enableTexture2D();
				GlStateManager.popMatrix();
			}
		}
	}

	@SubscribeEvent
	public static void drawGUI(GuiScreenEvent.BackgroundDrawnEvent event) {
		if (event.getGui() instanceof GuiMercenary) {
			GuiMercenary gui = (GuiMercenary) event.getGui();
			EntityTF2Character entity = gui.mercenary;
			if (entity.hasCapability(LDOHCapabilities.HUNGER, null) &! entity.isRobot()) {
				Minecraft mc = Minecraft.getMinecraft();
				IHunger hunger = entity.getCapability(LDOHCapabilities.HUNGER, null);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(TF_HUNGER_TEXTURE);
				gui.drawTexturedModalRect(gui.getGuiLeft() - 54, gui.getGuiTop(), 0, 0, 54, 70);
				int v = hunger.hasHungerEffect() ? 79 : 70;
				int food = hunger.getFoodLevel();
				for (int i = 0; i<=Math.ceil(food/2); i++) {
					int x = gui.getGuiLeft() - 50 + ((4-i%5)*9);
					int y = gui.getGuiTop() + 16 + ((int)Math.floor(i/5)*9);
					int u = (i+1)*2>food ? 9 : 0;
					if ((i*2)<food) gui.drawTexturedModalRect(x, y, u, v, 9, 9);
				}
				FontRenderer font = gui.fontRenderer;
				if (!hunger.getFoodSlot().isEmpty()) {
					GlStateManager.enableLighting();
					GlStateManager.enableRescaleNormal();
					RenderHelper.enableGUIStandardItemLighting();
					ItemStack stack = hunger.getFoodSlot();
					gui.itemRender.renderItemAndEffectIntoGUI(stack, gui.getGuiLeft() - 36, gui.getGuiTop() + 42);
					gui.itemRender.renderItemOverlayIntoGUI(font, stack, gui.getGuiLeft() - 36, gui.getGuiTop() + 42, null);
					RenderHelper.disableStandardItemLighting();
					GlStateManager.disableLighting();
				}
				String text = I18n.translateToLocal("gui.text.Hunger");
				font.drawString(text, gui.getGuiLeft() - 26 - (font.getStringWidth(text)/2), gui.getGuiTop()+5, 4210752);
			}
			if (entity.hasCapability(LDOHCapabilities.CURING, null)) {
				Minecraft mc = Minecraft.getMinecraft();
				ICuring curing = entity.getCapability(LDOHCapabilities.CURING, null);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(MEDIC_SYRINGES_TEXTURE);
				gui.drawTexturedModalRect(gui.getGuiLeft() - 54, gui.getGuiTop() + 112, 0, 0, 54, 54);
				FontRenderer font = gui.fontRenderer;
				int count = curing.getSyringeCount();
				if (count > 0) {
					GlStateManager.enableLighting();
					GlStateManager.enableRescaleNormal();
					RenderHelper.enableGUIStandardItemLighting();
					ItemStack stack = new ItemStack(LDOHItems.SYRINGE, count, 2);
					gui.itemRender.renderItemAndEffectIntoGUI(stack, gui.getGuiLeft() - 36, gui.getGuiTop() + 136);
					gui.itemRender.renderItemOverlayIntoGUI(font, stack, gui.getGuiLeft() - 36, gui.getGuiTop() + 136, null);
					RenderHelper.disableStandardItemLighting();
					GlStateManager.disableLighting();
				}
				String text = I18n.translateToLocal("gui.text.Curing");
				font.drawString(text, gui.getGuiLeft() - 26 - (font.getStringWidth(text)/2), gui.getGuiTop()+117, 4210752);
			}
		}
	}

	@SubscribeEvent
	public static void drawGUIOverlay(GuiScreenEvent.DrawScreenEvent.Post event) {
		if (event.getGui() instanceof GuiMercenary) {
			GuiMercenary gui = (GuiMercenary) event.getGui();
			EntityTF2Character entity = gui.mercenary;
			int mouseX = event.getMouseX();
			int mouseY = event.getMouseY();
			if (entity.hasCapability(LDOHCapabilities.HUNGER, null) &! entity.isRobot()) {
				IHunger hunger = entity.getCapability(LDOHCapabilities.HUNGER, null);
				if (!hunger.getFoodSlot().isEmpty()) {
					int slotX = gui.getGuiLeft() - 36;
					int slotY = gui.getGuiTop() + 42;
					if (mouseX >= slotX  && mouseX <= slotX + 18 && mouseY >= slotY  && mouseY <= slotY + 18) {
						gui.renderToolTip(hunger.getFoodSlot(), mouseX, mouseY);
					}
				}
			}
			if (entity.hasCapability(LDOHCapabilities.CURING, null)) {
				int count = entity.getCapability(LDOHCapabilities.CURING, null).getSyringeCount();
				if (count > 0) {
					int slotX = gui.getGuiLeft() - 36;
					int slotY = gui.getGuiTop() + 136;
					if (mouseX >= slotX  && mouseX <= slotX + 18 && mouseY >= slotY  && mouseY <= slotY + 18) {
						gui.renderToolTip(new ItemStack(LDOHItems.SYRINGE, count, 2), mouseX, mouseY);
					}
				}
			}
		}
	}

}
