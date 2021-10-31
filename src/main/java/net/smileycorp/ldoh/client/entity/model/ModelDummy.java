package net.smileycorp.ldoh.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDummy extends ModelBase {
	public ModelRenderer left_arm;
	public ModelRenderer left_leg;
	public ModelRenderer head;
	public ModelRenderer body;
	public ModelRenderer right_arm;
	public ModelRenderer right_leg;
	public ModelRenderer hat;

	public ModelDummy() {
		textureWidth = 64;
		textureHeight = 64;
		head = new ModelRenderer(this, 0, 0);
		head.setRotationPoint(0.0F, 22.0F, 6.0F);
		head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		setRotateAngle(head, -1.5707963267948966F, 0.0F, 0.0F);
		left_arm = new ModelRenderer(this, 40, 16);
		left_arm.setRotationPoint(-5.0F, 22.0F, 4.0F);
		left_arm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		setRotateAngle(left_arm, -1.5707963267948966F, 0.0F, 0.04817108735504349F);
		left_leg = new ModelRenderer(this, 0, 16);
		left_leg.setRotationPoint(-1.9F, 22.0F, -6.0F);
		left_leg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		setRotateAngle(left_leg, -1.5707963267948966F, 0.0F, 0.0F);
		body = new ModelRenderer(this, 16, 16);
		body.setRotationPoint(0.0F, 22.0F, 6.0F);
		body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		setRotateAngle(body, -1.5707963267948966F, 0.0F, 0.0F);
		right_arm = new ModelRenderer(this, 40, 16);
		right_arm.mirror = true;
		right_arm.setRotationPoint(5.0F, 22.0F, 4.0F);
		right_arm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		setRotateAngle(right_arm, -1.5707963267948966F, 0.0F, -0.04817108735504349F);
		right_leg = new ModelRenderer(this, 0, 16);
		right_leg.mirror = true;
		right_leg.setRotationPoint(1.9F, 22.0F, -6.0F);
		right_leg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		setRotateAngle(right_leg, -1.5707963267948966F, 0.0F, 0.0F);
		hat = new ModelRenderer(this, 32, 0);
		hat.setRotationPoint(0.0F, 22.0F, 6.0F);
		hat.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
		setRotateAngle(hat, -1.5707963267948966F, 0.0F, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		head.render(f5);
		left_arm.render(f5);
		left_leg.render(f5);
		body.render(f5);
		right_arm.render(f5);
		right_leg.render(f5);
		hat.render(f5);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
