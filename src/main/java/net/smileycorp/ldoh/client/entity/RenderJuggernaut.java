package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.client.entity.model.ModelJuggernaut;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.entity.EntityJuggernaut;

public class RenderJuggernaut extends RenderLiving<EntityJuggernaut> {

    public RenderJuggernaut(RenderManager rendermanager) {
        super(rendermanager, new ModelJuggernaut(), 1.5F);
        addLayer(new LayerJuggernautLights(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityJuggernaut entity) {
        return ModDefinitions.getResource("textures/entity/juggernaut_" + (entity.isAngy() ? "2" : "1") + ".png");
    }

}

