package net.smileycorp.ldoh.client.tesr;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
			RenderItem renderer = mc.getRenderItem();
			GlStateManager.pushMatrix();
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockFilingCabinet.FACING).getOpposite();
			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.rotate(90*(facing.ordinal()-2), 0,  1, 0);
			GlStateManager.translate(facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ());
			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(te.getContainedItem(), te.getWorld(), player);
			model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GUI, false);
			Minecraft.getMinecraft().getRenderItem().renderItem(te.getContainedItem(), model);
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableLighting();
			GlStateManager.popMatrix();
		}
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}

}
