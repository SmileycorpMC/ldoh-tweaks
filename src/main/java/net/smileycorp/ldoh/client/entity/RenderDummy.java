package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.client.entity.model.ModelDummy;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.entity.EntityDummy;

public class RenderDummy extends RenderLiving<EntityDummy> {

	public RenderDummy(RenderManager rendermanager)
	{
		super(rendermanager, new ModelDummy(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDummy entity) {
		return ModDefinitions.getResource("textures/entity/tf/"+entity.getTFTeam().getName()+"_"+entity.getTFClass()+"_sleep.png");
	}

}
