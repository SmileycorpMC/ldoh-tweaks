package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.entity.EntityJuggernaut;

public class LayerJuggernautLights implements LayerRenderer<EntityJuggernaut> {

    private final RenderJuggernaut renderer;

    public LayerJuggernautLights(RenderJuggernaut renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityJuggernaut entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entity.isInvisible()) return;
        renderer.bindTexture(Constants.loc("textures/entity/juggernaut_lights_" + (entity.isAngy() ? "2" : "1") + ".png"));
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.depthMask(true);
        int i = 61680;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        i = entity.getBrightnessForRender();
        j = i % 65536;
        k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        renderer.setLightmap(entity);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

}
