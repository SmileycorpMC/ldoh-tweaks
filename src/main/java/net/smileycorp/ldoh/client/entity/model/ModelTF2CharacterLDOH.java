package net.smileycorp.ldoh.client.entity.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.MathHelper;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import rafradek.TF2weapons.client.model.ModelTF2Character;

public class ModelTF2CharacterLDOH extends ModelTF2Character {

	public ModelTF2CharacterLDOH() {
		super();
		isRiding = true;
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (entity.hasCapability(LDOHCapabilities.EXHAUSTION, null)) {
			if (entity.getCapability(LDOHCapabilities.EXHAUSTION, null).isSleeping((EntityLiving) entity)) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(0, 0.6, 0);
				super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
				GlStateManager.popMatrix();
			} else super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		} else super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float age, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
		if (entity.hasCapability(LDOHCapabilities.EXHAUSTION, null)) {
			if (entity.getCapability(LDOHCapabilities.EXHAUSTION, null).isSleeping((EntityLiving) entity)) {
				bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
				bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
				bipedRightArm.rotateAngleX += -((float)Math.PI / 5F);
				bipedLeftArm.rotateAngleX += -((float)Math.PI / 5F);
				bipedRightLeg.rotateAngleX = -1.4137167F;
				bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
				bipedRightLeg.rotateAngleZ = 0.07853982F;
				bipedLeftLeg.rotateAngleX = -1.4137167F;
				bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
				bipedLeftLeg.rotateAngleZ = -0.07853982F;
				bipedHead.rotateAngleX = MathHelper.sin((age/20 - 1.5f))/10 +0.1f;
				bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
			} else super.setRotationAngles(limbSwing, limbSwingAmount, age, netHeadYaw, headPitch, scaleFactor, entity);
		} else super.setRotationAngles(limbSwing, limbSwingAmount, age, netHeadYaw, headPitch, scaleFactor, entity);
	}

}
