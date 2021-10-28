package net.smileycorp.ldoh.client;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
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

@EventBusSubscriber(modid=ModDefinitions.modid, value=Side.CLIENT)
public class ClientEventListener {

	public static String title = "";
	public static int starttime = 0;

	public static Color GAS_COLOUR = new Color(0.917647059f, 1f, 0.0470588235f, 0.2f);
	public static ResourceLocation GAS_TEXTURE = ModDefinitions.getResource("textures/misc/gas.png");

	//show title with the specified text
	public static void displayTitle(String text, int day) {
		Minecraft mc = Minecraft.getMinecraft();
		ITextComponent message = new TextComponentTranslation(text, new Object[]{100-day});
		message.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_RED));
		mc.ingameGUI.displayTitle(message.getFormattedText(), null, 10, 20, 10);
	}

	//show action bar message with the specified text
	public static void displayActionBar(String text) {
		Minecraft mc = Minecraft.getMinecraft();
		ITextComponent message = new TextComponentTranslation(text);
		message.setStyle(new Style().setBold(true).setColor(TextFormatting.YELLOW));
		mc.ingameGUI.setOverlayMessage(message, true);
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
				mc.getTextureManager().bindTexture(GAS_TEXTURE);
				float f = 1 / 32 /10000;
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
				buffer.pos(cx-x+0.5-size, 31-y - 0.1, cz-z+0.5-size).tex(0, 0).color(r, g, b, a).normal(cx-size, (float) y, cz-size).endVertex();
				buffer.pos(cx-x+0.5-size, 31-y - 0.1, cz-z+0.5+size).tex(0, 2*size-f).color(r, g, b, a).normal(cx-size, (float) y, cz+size).endVertex();
				buffer.pos(cx-x+0.5+size, 31-y - 0.1, cz-z+0.5+size).tex(2*size-f, 2*size-f).color(r, g, b, a).normal(cx+size, (float) y, cz+size).endVertex();
				buffer.pos(cx-x+0.5+size, 31-y - 0.1, cz-z+0.5-size).tex(2*size-f, 0).color(r, g, b, a).normal(cx+size, (float) y, cz-size).endVertex();
				tessellator.draw();
				GlStateManager.disableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.disableLighting();
				GlStateManager.enableTexture2D();
				GlStateManager.popMatrix();
			}
		}
	}

}
