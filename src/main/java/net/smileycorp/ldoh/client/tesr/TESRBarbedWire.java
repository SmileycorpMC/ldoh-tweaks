package net.smileycorp.ldoh.client.tesr;

import java.awt.Color;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;

public class TESRBarbedWire extends TileEntitySpecialRenderer<TileBarbedWire> {

	public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

	@Override
	public void render(TileBarbedWire te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		Minecraft mc = Minecraft.getMinecraft();
		//render only if the player is looking at the block
		if (rendererDispatcher.cameraHitResult != null && te.getPos().equals(rendererDispatcher.cameraHitResult.getBlockPos())) {
			if (!mc.gameSettings.hideGUI) {
				//render name tag with the health left
				setLightmapDisabled(true);
				int max = te.getMaterial().getDurability();
				int cur = te.getDurability();
				drawNameplate(te, cur + "/" + max, x, y-0.25, z, 12);
				setLightmapDisabled(false);
			}
		}
		if (te.isEnchanted()) {
			System.out.println("enchanted");
			BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
			World world = getWorld();
			BlockPos pos = te.getPos();
			IBlockState state = world.getBlockState(pos);
			IBakedModel model = dispatcher.getModelForState(state);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			GlStateManager.depthMask(false);
			GlStateManager.depthFunc(514);
			GlStateManager.disableLighting();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
			bindTexture(RES_ITEM_GLINT);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			buffer.begin(7, DefaultVertexFormats.BLOCK);
			buffer.setTranslation(x - (double)pos.getX(), y - (double)pos.getY(), z - (double)pos.getZ());
			dispatcher.getBlockModelRenderer().renderModel(world, model, state, pos, buffer, false);
			buffer.setTranslation(0.0D, 0.0D, 0.0D);
			Color colour = new Color(-8372020);
			GlStateManager.color(colour.getRed(), colour.getGreen(), colour.getBlue(), 0.75f);
			tessellator.draw();
			GlStateManager.color(1, 1, 1, 1);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.enableLighting();
			GlStateManager.depthFunc(515);
			GlStateManager.depthMask(true);
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		}
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}

}
