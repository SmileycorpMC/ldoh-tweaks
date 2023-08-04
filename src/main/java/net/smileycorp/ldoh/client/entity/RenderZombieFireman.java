package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.entity.EntityZombieFireman;

public class RenderZombieFireman extends RenderBiped<EntityZombieFireman> {

	public RenderZombieFireman(RenderManager rendermanager) {
		super(rendermanager, new ModelZombie(0.05f, false), 0.6F);
		LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
		{
			@Override
			protected void initArmor()
			{
				modelLeggings = new ModelZombie(0.825F, true);
				modelArmor = new ModelZombie(1.155F, true);
			}
		};
		this.addLayer(layerbipedarmor);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityZombieFireman entity) {
		return ModDefinitions.getResource("textures/entity/zombie_fireman.png");
	}

}
