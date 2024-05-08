package net.smileycorp.ldoh.client.entity.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelZombie;

public class ModelThinZombie extends ModelZombie {

    //need this on the off chance someone removes mobends
    public ModelThinZombie(float modelSize, float p_i1149_2_) {
        super(modelSize, false);
        bipedLeftLeg = new ModelRenderer(this, 0, 16);
        bipedLeftLeg.setRotationPoint(-0.5F, 12.0F, 0.0F);
        bipedLeftLeg.addBox(-3.0F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
        bipedRightArm = new ModelRenderer(this, 40, 16);
        bipedRightArm.mirror = true;
        bipedRightArm.setRotationPoint(5F, 2.0F, 0.0F);
        bipedRightArm.addBox(-1.5F, -1.5F, -1.5F, 3, 12, 3, 0.0F);
        setRotateAngle(bipedRightArm, -1.4486232791552935F, 0.09075712110370514F, 0.03665191429188092F);
        bipedRightLeg = new ModelRenderer(this, 0, 16);
        bipedRightLeg.mirror = true;
        bipedRightLeg.setRotationPoint(0.5F, 12.0F, 0.0F);
        bipedRightLeg.addBox(0.0F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
        bipedHead = new ModelRenderer(this, 0, 0);
        bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        bipedHead.addBox(-3.5F, -8.0F, -3.5F, 7, 8, 7, 0.0F);
        ModelRenderer dress = new ModelRenderer(this, 16, 32);
        dress.setRotationPoint(0.0F, 2.0F, 0.0F);
        dress.addBox(-4.0F, 0.0F, -2.0F, 8, 14, 4, 0.0F);
        bipedLeftArm = new ModelRenderer(this, 40, 16);
        bipedLeftArm.setRotationPoint(-4.5F, 2.0F, 0.0F);
        bipedLeftArm.addBox(-2.0F, -1.5F, -1.5F, 3, 12, 3, 0.0F);
        setRotateAngle(bipedLeftArm, -1.4486232791552935F, -0.09075712110370514F, -0.03665191429188092F);
        bipedBody = new ModelRenderer(this, 16, 16);
        bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        bipedBody.addBox(-3.5F, 0.0F, -1.5F, 7, 12, 3, 0.0F);
        bipedHeadwear = new ModelRenderer(this, 32, 0);
        bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        bipedHeadwear.addBox(-3.5F, -8.0F, -3.5F, 7, 8, 7, 0.5F);
        bipedBody.addChild(dress);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }


}
