package net.smileycorp.ldoh.client.tesr;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.smileycorp.ldoh.client.entity.RenderTurret;
import net.smileycorp.ldoh.client.entity.model.ModelTurret;
import net.smileycorp.ldoh.common.Constants;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

public class TESRTurretItem extends TileEntityItemStackRenderer {

    public static ModelResourceLocation BASE_LOC = new ModelResourceLocation(Constants.loc("turret"), "facing=up");

    protected ModelTurret turret = new ModelTurret();

    protected TransformType transforms;

    public class WrappedBakedModel implements IBakedModel {

        private final IBakedModel original;

        public WrappedBakedModel(IBakedModel original) {
            this.original = original;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
            return original.getQuads(state, side, rand);
        }

        @Override
        public boolean isAmbientOcclusion() {
            return original.isAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return original.isGui3d();
        }

        @Override
        public boolean isBuiltInRenderer() {
            return true;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return original.getParticleTexture();
        }

        @Override
        @SuppressWarnings("deprecation")
        public ItemCameraTransforms getItemCameraTransforms() {
            return original.getItemCameraTransforms();
        }

        @Override
        public ItemOverrideList getOverrides() {
            return original.getOverrides();
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType transforms) {
            TESRTurretItem.this.transforms = transforms;
            return Pair.of(this, null);
        }

    }

    @Override
    public void renderByItem(ItemStack stack) {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.pushMatrix();
        //get translations based on item position
        switch (transforms) {
            case GUI:
                GlStateManager.scale(1.15, 1.15, 1.15);
                GlStateManager.translate(0.43, 0.53, 0);
                break;
            case FIXED:
                GlStateManager.rotate(90, 0, 1, 0);
                GlStateManager.scale(2, 2, 2);
                GlStateManager.translate(-0.25, 0.25, 0.25);
                break;
            case GROUND:
                GlStateManager.scale(1.5, 1.5, 1.5);
                GlStateManager.translate(0.34, 0.3, 0.34);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                GlStateManager.scale(2, 2, 2);
                GlStateManager.rotate(90, 0, 1, 0);
                GlStateManager.translate(0, 0.2, 0.5);
                break;
            case FIRST_PERSON_LEFT_HAND:
                GlStateManager.scale(2, 2, 2);
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.translate(0, 0.2, 0);
                break;
            case THIRD_PERSON_RIGHT_HAND:
                GlStateManager.scale(1.3, 1.3, 1.3);
                GlStateManager.rotate(90, 0, 0, 1);
                GlStateManager.rotate(22.5f, 1, 0, 0);
                GlStateManager.translate(0.42, -0.2, 0.6);
                break;
            case THIRD_PERSON_LEFT_HAND:
                GlStateManager.scale(1.3, 1.3, 1.3);
                GlStateManager.translate(0.43, 0.35, 0.48);
                break;
            default:
                break;
        }
        //setup properties
        boolean enemy = false;
        boolean australium = false;
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null) {
            if (nbt.hasKey("isEnemy")) enemy = nbt.getBoolean("isEnemy");
            if (nbt.hasKey("entity")) {
                NBTTagCompound entity = nbt.getCompoundTag("entity");
                if (entity.hasKey("upgrades")) for (int i : entity.getIntArray("upgrades")) {
                    if (i == 6) australium = true;
                    break;
                }
            }
        }
        //get and bind the correct texture for type of turret
        IBakedModel base = mc.getRenderItem().getItemModelMesher().getModelManager().getModel(BASE_LOC);
        mc.getRenderItem().renderItem(stack, ForgeHooksClient.handleCameraTransforms(base, transforms, false));
        mc.getTextureManager().bindTexture(enemy ? RenderTurret.ENEMY_TEXTURE : RenderTurret.TEXTURE);
        GlStateManager.rotate(90, 0, 1, 0);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.translate(0, -0.9, 0);
        //eventually we'll add australium rendering to this
        //nbt = stack.getTagCompound();
        //render turret entity over item
        Team team = mc.player.getTeam();
        turret.render(enemy ? 0xAEAF77 : team == null ? 0x404040 : mc.fontRenderer.getColorCode(team.getColor().formattingCode),
                0.0261799388f * mc.world.getWorldTime(), enemy, australium, 0, 0.05f);
        GlStateManager.popMatrix();
    }

}
