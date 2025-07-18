package net.smileycorp.ldoh.client.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.smileycorp.ldoh.common.block.BlockFilingCabinet;
import net.smileycorp.ldoh.common.tile.TileFilingCabinet;

public class TESRFilingCabinet extends TileEntitySpecialRenderer<TileFilingCabinet> {

    @Override
    public void render(TileFilingCabinet te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        //draw filing cabinet contents
        if (!te.isEmpty() && player != null) {
            EnumFacing facing = te.getWorld().getBlockState(te.getPos()).getValue(BlockFilingCabinet.FACING);
            setLightmapDisabled(true);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.depthMask(true);
            GlStateManager.enableAlpha();
            drawText(mc, te, facing, x, y, z);
            renderItem(mc, te, facing, x, y, z);
        }
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
    }

    //draw item count
    private void drawText(Minecraft mc, TileFilingCabinet te, EnumFacing facing, double x, double y, double z) {
        GlStateManager.pushMatrix();
        switch (facing) {
            case WEST:
                GlStateManager.translate(x, y + 0.4, z + 0.5);
                GlStateManager.rotate(-90, 0, 1, 0);
                break;
            case EAST:
                GlStateManager.translate(x + 1, y + 0.4, z + 0.5);
                GlStateManager.rotate(90, 0, 1, 0);
                break;
            case NORTH:
                GlStateManager.translate(x + 0.5, y + 0.4, z);
                GlStateManager.rotate(180, 0, 1, 0);
                break;
            case SOUTH:
                GlStateManager.translate(x + 0.5, y + 0.4, z + 1);
                break;
        }
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.scale(0.025f, 0.025f, 0.025f);
        GlStateManager.translate(13, 2, -0.5);
        FontRenderer font = mc.fontRenderer;
        String count = String.valueOf(te.getCurrentCount());
        font.drawString(count, -font.getStringWidth(count), 2, 0xFFFFFF);
        GlStateManager.popMatrix();
    }

    //draw item sprite
    private void renderItem(Minecraft mc, TileFilingCabinet te, EnumFacing facing, double x, double y, double z) {
        GlStateManager.pushMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        switch (facing) {
            case WEST:
                GlStateManager.translate(x - 0.01, y + 0.4, z + 0.5);
                GlStateManager.rotate(-90, 0, 1, 0);
                break;
            case EAST:
                GlStateManager.translate(x + 1.01, y + 0.4, z + 0.5);
                GlStateManager.rotate(90, 0, 1, 0);
                break;
            case NORTH:
                GlStateManager.translate(x + 0.5, y + 0.4, z - 0.01);
                GlStateManager.rotate(180, 0, 1, 0);
                break;
            case SOUTH:
                GlStateManager.translate(x + 0.5, y + 0.4, z + 1.01);
                break;
        }
        GlStateManager.scale(0.6f, 0.6f, 0.001f);
        RenderItem render = mc.getRenderItem();
        IBakedModel model = render.getItemModelWithOverrides(te.getContainedItem(), te.getWorld(), mc.player);
        model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GUI, false);
        render.renderItem(te.getContainedItem(), model);
        GlStateManager.disableRescaleNormal();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
    }

}
