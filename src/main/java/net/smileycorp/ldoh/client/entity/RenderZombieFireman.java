package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.entity.zombie.EntityZombieFireman;

public class RenderZombieFireman extends RenderBiped<EntityZombieFireman> {

    public RenderZombieFireman(RenderManager rendermanager) {
        super(rendermanager, new ModelZombie(0.05f, false), 0.6F);
        addLayer(new LayerZombieArmour(this, 1.155F, 0.825F));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityZombieFireman entity) {
        return Constants.loc("textures/entity/zombie_fireman.png");
    }

}
