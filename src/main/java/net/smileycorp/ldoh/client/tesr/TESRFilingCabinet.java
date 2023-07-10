package net.smileycorp.ldoh.client.tesr;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.smileycorp.ldoh.common.block.BlockFilingCabinet;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;
import net.smileycorp.ldoh.common.tile.TileFilingCabinet;
import org.lwjgl.opengl.GL11;

public class TESRFilingCabinet extends TileEntitySpecialRenderer<TileFilingCabinet> {

	@Override
	public void render(TileFilingCabinet te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (!te.isEmpty() && player != null) {
			GlStateManager.pushMatrix();
			mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockFilingCabinet.FACING);
			setLightmapDisabled(true);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.depthMask(true);
			GlStateManager.enableAlpha();
			switch (facing) {
				case WEST:
					GlStateManager.translate(x, y+0.4, z+0.5);
					GlStateManager.rotate(-90, 0,  1, 0);
					break;
				case EAST:
					GlStateManager.translate(x+1, y+0.4, z+0.5);
					GlStateManager.rotate(90, 0,  1, 0);
					break;
				case NORTH:
					GlStateManager.translate(x+0.5, y+0.4, z);
					GlStateManager.rotate(180, 0,  1, 0);
					break;
				case SOUTH:
					GlStateManager.translate(x+0.5, y+0.4, z+1);
					break;
			}
			GlStateManager.scale(0.6f, 0.6f,  0.01f);
			RenderItem render = mc.getRenderItem();
			IBakedModel model = render.getItemModelWithOverrides(te.getContainedItem(), te.getWorld(), player);
			model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GUI, false);
			render.renderItem(te.getContainedItem(), model);
			GlStateManager.rotate(180, 1, 0,  0);
			GlStateManager.scale(0.05f, 0.05f,  0.05f);
			FontRenderer font = mc.fontRenderer;
			String count = String.valueOf(te.getCurrentCount());
			GlStateManager.translate(0, 0, -1);
			font.drawString(count, -font.getStringWidth(count), 2, 0xFFFFFF);
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableLighting();
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.popMatrix();
		}
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}

}
