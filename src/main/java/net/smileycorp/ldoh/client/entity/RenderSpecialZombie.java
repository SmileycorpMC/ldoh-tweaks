package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.common.Constants;

public class RenderSpecialZombie<T extends EntityZombie> extends RenderBiped<T> {

    protected String texture;

    public RenderSpecialZombie(RenderManager rendermanager, String texture) {
        super(rendermanager, new ModelZombie(), 0.5F);
        this.texture = texture;
        addLayer(new LayerZombieArmour(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityZombie entity) {
        return Constants.loc("textures/entity/" + texture + ".png");
    }

}
