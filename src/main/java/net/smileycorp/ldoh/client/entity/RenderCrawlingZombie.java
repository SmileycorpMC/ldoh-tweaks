package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.client.entity.model.ModelCrawlingZombie;

public class RenderCrawlingZombie extends RenderBiped<EntityZombie> {

    private final String texture;

    public RenderCrawlingZombie(RenderManager rendermanager, String texture) {
        super(rendermanager, new ModelCrawlingZombie(), 0.5F);
        this.texture = texture;
       addLayer(new LayerBipedArmor(this) {
           @Override
           protected void initArmor() {
               modelLeggings = new ModelCrawlingZombie(0.5F, true);
               modelArmor = new ModelCrawlingZombie(1.0F, true);
           }
       });
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityZombie entity) {
        return new ResourceLocation("textures/entity/zombie/" + texture + ".png");
    }

}
