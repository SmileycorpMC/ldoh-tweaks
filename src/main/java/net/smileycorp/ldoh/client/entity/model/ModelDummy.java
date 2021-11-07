package net.smileycorp.ldoh.client.entity.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelDummy extends ModelBiped {

	public ModelDummy() {
		super(0, 0, 64, 64);
		isRiding = true;
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0.6, 0);
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.popMatrix();
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float age, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
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
	}

}
