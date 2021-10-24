package net.smileycorp.ldoh.client;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.ldoh.common.ModDefinitions;

import org.lwjgl.opengl.GL11;

@EventBusSubscriber(modid=ModDefinitions.modid, value=Side.CLIENT)
public class ClientEventListener {
	
	public static String title = "";
	public static int starttime = 0;
	
	public static Color GAS_COLOUR = new Color(0.917647059f, 1f, 0.0470588235f, 0.2f);

	public static void displayTitle(String text, int day) {
		Minecraft mc = Minecraft.getMinecraft();
		ITextComponent message = new TextComponentTranslation(text, new Object[]{100-day});
		message.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_RED));
		mc.ingameGUI.displayTitle(message.getFormattedText(), null, 10, 20, 10);
	}
	
	public static void displayActionBar(String text) {
		Minecraft mc = Minecraft.getMinecraft();
		ITextComponent message = new TextComponentTranslation(text);
		message.setStyle(new Style().setBold(true).setColor(TextFormatting.YELLOW));
		mc.ingameGUI.setOverlayMessage(message, true);
	}
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Pre event){
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.player;
		if (player!= null && event.getType() == ElementType.VIGNETTE) {
			
		}
	}
	@SubscribeEvent
	public void postRenderOverlay(RenderGameOverlayEvent.Pre event){
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.player;
		if (player!= null && event.getType() == ElementType.ALL) {
			if (player.getPosition().getY()<=29.5) {
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDepthMask(false);
		        GL11.glDisable(GL11.GL_ALPHA_TEST);
		        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		    	Gui.drawRect(0, 0, mc.displayWidth, mc.displayHeight, GAS_COLOUR.getRGB());
		    	GL11.glDepthMask(true);
		        GL11.glEnable(GL11.GL_DEPTH_TEST);
		        GL11.glEnable(GL11.GL_ALPHA_TEST);
			}
		}
	}
	
	@SubscribeEvent
	public static void onRenderWorldLastEvent(RenderWorldLastEvent event)  {
		Minecraft mc = Minecraft.getMinecraft();
		Entity entity = mc.getRenderViewEntity();
		if (entity != null) {
			if (entity.posY >= 29.5 && entity.posY <= 60) {
				RenderManager rm = mc.getRenderManager();
				int size = rm.options == null ? 0 : rm.options.renderDistanceChunks*16;
				int r = GAS_COLOUR.getRed();
		        int g = GAS_COLOUR.getGreen();
		        int b = GAS_COLOUR.getBlue();
		        int a = GAS_COLOUR.getAlpha();
				final double y = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * event.getPartialTicks());
				int l = entity.world.getCombinedLight(entity.getPosition().up((int) Math.round(31-y)), 2);
				GlStateManager.pushMatrix();
				GlStateManager.disableLighting();
				GlStateManager.enableBlend();
				GlStateManager.disableAlpha();
				mc.getTextureManager().bindTexture(ModDefinitions.getResource("textures/misc/gas.png"));
				Tessellator tessellator = Tessellator.getInstance();
		        BufferBuilder buffer = tessellator.getBuffer();
		        buffer.setTranslation(0, 31-y - 0.01, 0);
		        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
				buffer.pos(-size, 0, -size).color(r, g, b, a).tex(0, 0).lightmap(l >> 16 & 0xFFFF, l & 0xFFFF).endVertex();
                buffer.pos(-size, 0, size).color(r, g, b, a).tex(0, 0).lightmap(l >> 16 & 0xFFFF, l & 0xFFFF).endVertex();
                buffer.pos(size, 0, size).color(r, g, b, a).tex(0, 0).lightmap(l >> 16 & 0xFFFF, l & 0xFFFF).endVertex();
                buffer.pos(size, 0, -size).color(r, g, b, a).tex(0, 0).lightmap(l >> 16 & 0xFFFF, l & 0xFFFF).endVertex();
                tessellator.draw();
                GlStateManager.disableBlend();
        		GlStateManager.enableAlpha();
        		GlStateManager.disableLighting();
        		GlStateManager.popMatrix();
        		buffer.setTranslation(0, 0, 0);
			}
		}
	}
	
}
