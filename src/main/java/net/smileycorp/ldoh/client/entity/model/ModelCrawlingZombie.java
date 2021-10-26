package net.smileycorp.ldoh.client.entity.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelCrawlingZombie extends ModelBiped {
	
	public ModelCrawlingZombie() {
        this(0.0F, false);
    }
	
	public ModelCrawlingZombie(float modelSize, boolean p_i1168_2_) {
        super(modelSize, 0.0F, 64, p_i1168_2_ ? 32 : 64);
        textureWidth = 64;
        textureHeight = 64;
        textureWidth = 64;
        textureHeight = 64;
        bipedLeftArm = new ModelRenderer(this, 40, 16);
        bipedLeftArm.setRotationPoint(-5.0F, 20.0F, 0.0F);
        bipedLeftArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        setRotateAngle(bipedLeftArm, -1.2566370614359172F, 0.18727382873899157F, 0.10000736613927509F);
        bipedRightArm = new ModelRenderer(this, 40, 16);
        bipedRightArm.mirror = true;
        bipedRightArm.setRotationPoint(5.0F, 20.0F, 0.0F);
        bipedRightArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        setRotateAngle(bipedRightArm, -1.2566370614359172F, -0.18203784098300857F, -0.10000736613927509F);
        bipedBody = new ModelRenderer(this, 16, 16);
        bipedBody.setRotationPoint(0.0F, 20.0F, 0.0F);
        bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        setRotateAngle(bipedBody, 1.3203415791337103F, 0.0F, 0.0F);
        bipedHeadwear = new ModelRenderer(this, 32, 0);
        bipedHeadwear.setRotationPoint(0.0F, 21.0F, -0.2F);
        bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        bipedHead = new ModelRenderer(this, 0, 0);
        bipedHead.setRotationPoint(0.0F, 21.0F, -0.2F);
        bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
    }
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        bipedLeftArm.render(scale);
        bipedRightArm.render(scale);
        bipedBody.render(scale);
        bipedHeadwear.render(scale);
        bipedHead.render(scale);
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;
        this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
        float f = MathHelper.sin(limbSwing)*0.2f;
        this.bipedRightArm.rotateAngleX = f -1.2566370614359172F;
        this.bipedLeftArm.rotateAngleX = -f -1.2566370614359172F;
        this.bipedRightArm.rotateAngleY = f -0.18203784098300857F;
        this.bipedLeftArm.rotateAngleY = f + 0.18727382873899157F;
        this.bipedBody.rotateAngleY = (float) Math.sin(limbSwing*1.5) * 0.2f;
        copyModelAngles(this.bipedHead, this.bipedHeadwear);
    }
	
	private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
	
}
