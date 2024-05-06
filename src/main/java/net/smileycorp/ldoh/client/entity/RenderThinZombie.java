package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.client.entity.model.ModelThinZombie;

public class RenderThinZombie<T extends EntityZombie> extends RenderBiped<T> {
    
    private final ResourceLocation texture;
    
    public RenderThinZombie(RenderManager rendermanager, ResourceLocation texture) {
        super(rendermanager, new ModelThinZombie(0f, 0f), 0.5F);
        this.texture = texture;
        addLayer(new LayerZombieArmour(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return texture;
    }

}
