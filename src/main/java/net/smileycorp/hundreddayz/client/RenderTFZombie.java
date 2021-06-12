package net.smileycorp.hundreddayz.client;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.hundreddayz.common.ModDefinitions;
import net.smileycorp.hundreddayz.common.entity.EntityTFZombie;

public class RenderTFZombie extends RenderBiped<EntityTFZombie> {

    public RenderTFZombie(RenderManager rendermanager)
    {
        super(rendermanager, new ModelZombie(), 0.5F);
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            @Override
			protected void initArmor()
            {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);
    }

    @Override
	protected ResourceLocation getEntityTexture(EntityTFZombie entity) {
        return ModDefinitions.getResource("textures/entity/tfzombie/"+entity.getTFTeam().getName()+"_"+entity.getTFClass()+"_zombie.png");
    }

}
