package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.client.entity.model.ModelCrawlingZombie;
import net.smileycorp.ldoh.common.entity.EntityCrawlingZombie;

public class RenderCrawlingZombie extends RenderBiped<EntityCrawlingZombie> {
	
	public ResourceLocation texture;

    public RenderCrawlingZombie(RenderManager rendermanager, ResourceLocation texture)
    {
        super(rendermanager, new ModelCrawlingZombie(), 0.5F);
        this.texture = texture;
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            @Override
			protected void initArmor()
            {
                this.modelLeggings = new ModelCrawlingZombie(0.5F, true);
                this.modelArmor = new ModelCrawlingZombie(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);
    }

    @Override
	protected ResourceLocation getEntityTexture(EntityCrawlingZombie entity) {
        return texture;
    }

}
