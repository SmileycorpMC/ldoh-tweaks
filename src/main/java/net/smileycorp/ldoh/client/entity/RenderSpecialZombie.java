package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.common.Constants;

public class RenderSpecialZombie<T extends EntityZombie> extends RenderBiped<T> {

    protected String texture;

    public RenderSpecialZombie(RenderManager rendermanager, String texture) {
        super(rendermanager, new ModelZombie(), 0.5F);
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
            @Override
            protected void initArmor() {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);
        this.texture = texture;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityZombie entity) {
        return Constants.loc("textures/entity/" + texture + ".png");
    }

}
