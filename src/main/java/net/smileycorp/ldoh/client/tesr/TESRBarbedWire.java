package net.smileycorp.ldoh.client.tesr;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
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
			mc.getTextureManager().bindTexture(RES_ITEM_GLINT);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			//GlStateManager.scale(8.0F, 8.0F, 8.0F);
			float f = Minecraft.getSystemTime() % 3000L / 3000.0F / 8.0F;
			//GlStateManager.translate(f, 0.0F, 0.0F);
			//GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
			dispatcher.getBlockModelRenderer().renderModelSmooth(world, dispatcher.getModelForState(state), state, pos, buffer, false, -8372020);
			//this.renderModel(model, -8372020);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			//GlStateManager.scale(8.0F, 8.0F, 8.0F);
			float f1 = Minecraft.getSystemTime() % 4873L / 4873.0F / 8.0F;
			//GlStateManager.translate(-f1, 0.0F, 0.0F);
			//GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
			dispatcher.getBlockModelRenderer().renderModelSmooth(world, dispatcher.getModelForState(state), state, pos, buffer, false, -8372020);
			//this.renderModel(model, -8372020);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.enableLighting();
			GlStateManager.depthFunc(515);
			GlStateManager.depthMask(true);
			mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		}
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}

}
