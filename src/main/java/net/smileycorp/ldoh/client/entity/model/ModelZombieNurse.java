package net.smileycorp.ldoh.client.entity.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelZombie;

public class ModelZombieNurse extends ModelZombie {
	
	 public ModelZombieNurse(float modelSize, float p_i1149_2_) {
		 	super(modelSize, false); 
	        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
	        this.bipedLeftLeg.setRotationPoint(-0.5F, 12.0F, 0.0F);
	        this.bipedLeftLeg.addBox(-3.0F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
	        this.bipedRightArm = new ModelRenderer(this, 40, 16);
	        this.bipedRightArm.mirror = true;
	        this.bipedRightArm.setRotationPoint(5F, 2.0F, 0.0F);
	        this.bipedRightArm.addBox(-1.5F, -1.5F, -1.5F, 3, 12, 3, 0.0F);
	        this.setRotateAngle(bipedRightArm, -1.4486232791552935F, 0.09075712110370514F, 0.03665191429188092F);
	        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
	        this.bipedRightLeg.mirror = true;
	        this.bipedRightLeg.setRotationPoint(0.5F, 12.0F, 0.0F);
	        this.bipedRightLeg.addBox(0.0F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
	        this.bipedHead = new ModelRenderer(this, 0, 0);
	        this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
	        this.bipedHead.addBox(-3.5F, -8.0F, -3.5F, 7, 8, 7, 0.0F);
	        ModelRenderer dress = new ModelRenderer(this, 16, 32);
	        dress.setRotationPoint(0.0F, 2.0F, 0.0F);
	        dress.addBox(-4.0F, 0.0F, -2.0F, 8, 14, 4, 0.0F);
	        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
	        this.bipedLeftArm.setRotationPoint(-4.5F, 2.0F, 0.0F);
	        this.bipedLeftArm.addBox(-2.0F, -1.5F, -1.5F, 3, 12, 3, 0.0F);
	        this.setRotateAngle(bipedLeftArm, -1.4486232791552935F, -0.09075712110370514F, -0.03665191429188092F);
	        this.bipedBody = new ModelRenderer(this, 16, 16);
	        this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
	        this.bipedBody.addBox(-3.5F, 0.0F, -1.5F, 7, 12, 3, 0.0F);
	        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
	        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
	        this.bipedHeadwear.addBox(-3.5F, -8.0F, -3.5F, 7, 8, 7, 0.5F);
	        this.bipedBody.addChild(dress);
	    }
	 
	 public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
	        modelRenderer.rotateAngleX = x;
	        modelRenderer.rotateAngleY = y;
	        modelRenderer.rotateAngleZ = z;
	    }
	

}
