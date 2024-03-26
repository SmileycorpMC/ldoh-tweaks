package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.client.entity.model.ModelZombieNurse;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.entity.zombie.EntityReaver;

public class RenderReaver extends RenderBiped<EntityReaver> {

    public RenderReaver(RenderManager rendermanager) {
        super(rendermanager, new ModelZombieNurse(0f, 0f), 0.5F);
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
            @Override
            protected void initArmor() {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityReaver entity) {
        return Constants.loc("textures/entity/reaver.png");
    }

}
