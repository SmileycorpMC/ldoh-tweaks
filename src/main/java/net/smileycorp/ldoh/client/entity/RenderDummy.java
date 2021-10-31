package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.client.entity.model.ModelDummy;
import net.smileycorp.ldoh.common.entity.EntityDummy;

public class RenderDummy extends RenderLiving<EntityDummy> {

	public RenderDummy(RenderManager rendermanager)
	{
		super(rendermanager, new ModelDummy(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDummy entity) {
		return new ResourceLocation("rafradek_tf2_weapons", "textures/entity/tf2/"+entity.getTFTeam().getName()+"/"+entity.getTFClass()+".png");
	}

}
