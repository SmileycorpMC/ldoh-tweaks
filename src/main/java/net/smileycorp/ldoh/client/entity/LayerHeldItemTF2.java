package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;

public class LayerHeldItemTF2 extends LayerHeldItem {

	public LayerHeldItemTF2(RenderLivingBase<?> renderer) {
		super(renderer);
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (entity.hasCapability(LDOHCapabilities.EXHAUSTION, null) && entity instanceof EntityLiving)
			if (entity.getCapability(LDOHCapabilities.EXHAUSTION, null).isSleeping((EntityLiving) entity)) return;
		super.doRenderLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
	}
}
