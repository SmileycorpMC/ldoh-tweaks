package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.client.entity.model.ModelInfPhoenix;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.entity.infphoenix.EntityInfPhoenix;

public class RenderInfPhoenix extends RenderLiving<EntityInfPhoenix> {

    public RenderInfPhoenix(RenderManager rendermanager) {
        super(rendermanager, new ModelInfPhoenix(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityInfPhoenix entity) {
        return Constants.loc("textures/entity/inf_phoenix/" + entity.getName() + ".png");
    }

}
