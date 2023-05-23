package net.smileycorp.ldoh.client.tesr;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
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
			//System.out.println("enchanted");
			float f = mc.player.ticksExisted +partialTicks;
			BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
			World world = getWorld();
			BlockPos pos = te.getPos();
			IBlockState state = world.getBlockState(pos);
			IBakedModel model = dispatcher.getModelForState(state);
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
			GlStateManager.enableBlend();
			GlStateManager.depthFunc(514);
			GlStateManager.depthMask(false);
			mc.getRenderItem().renderEffect(model);
			GlStateManager.translate(0, 0, 0);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			GlStateManager.matrixMode(5888);
			GlStateManager.enableLighting();
			GlStateManager.depthMask(true);
			GlStateManager.depthFunc(515);
			GlStateManager.disableBlend();
		}
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}

}
