package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;

public class LayerZombieArmour extends LayerBipedArmor {
    
    private final float size;
    private final float legSize;
    
    public LayerZombieArmour(RenderLivingBase<?> parent) {
        this(parent, 1f, 0.5f);
    }
    
    public LayerZombieArmour(RenderLivingBase<?> parent, float size, float legSize) {
        super(parent);
        this.size = size;
        this.legSize = size;
    }
    
    @Override
    protected void initArmor() {
        modelArmor = new ModelZombie(size, true);
        modelLeggings = new ModelZombie(legSize, true);
    }
    
}
