package net.smileycorp.ldoh.client.entity.model;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelJuggernaut extends ModelBase {
	private final ModelRenderer body;
	private final ModelRenderer left_leg;
	private final ModelRenderer cube_r1;
	private final ModelRenderer left_underleg;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer right_leg;
	private final ModelRenderer cube_r4;
	private final ModelRenderer right_underleg;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;
	private final ModelRenderer torax;
	private final ModelRenderer cube_r7;
	private final ModelRenderer cube_r8;
	private final ModelRenderer cube_r9;
	private final ModelRenderer cube_r10;
	private final ModelRenderer cube_r11;
	private final ModelRenderer cube_r12;
	private final ModelRenderer cube_r13;
	private final ModelRenderer left_arm;
	private final ModelRenderer cube_r14;
	private final ModelRenderer left_minigun;
	private final ModelRenderer right_arm;
	private final ModelRenderer cube_r15;
	private final ModelRenderer right_minigun;

	public ModelJuggernaut() {
		textureWidth = 128;
		textureHeight = 128;

		body = new ModelRenderer(this);
		body.setRotationPoint(-6.0F, 24.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 30, 26, -4.0F, -25.0F, -1.0F, 20, 4, 6, 0.0F, false));

		left_leg = new ModelRenderer(this);
		left_leg.setRotationPoint(-3.0F, -23.0F, 2.0F);
		body.addChild(left_leg);
		

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(-1.5F, 8.5F, -4.5F);
		left_leg.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.5236F, 0.0F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 72, -2.5F, -11.5F, -2.5F, 5, 13, 5, 0.0F, false));

		left_underleg = new ModelRenderer(this);
		left_underleg.setRotationPoint(-2.0F, 8.0F, -3.0F);
		left_leg.addChild(left_underleg);
		left_underleg.cubeList.add(new ModelBox(left_underleg, 21, 71, -3.0F, 12.0F, -6.0F, 7, 3, 9, 0.0F, false));
		left_underleg.cubeList.add(new ModelBox(left_underleg, 53, 73, -3.0F, 0.0F, -5.0F, 7, 4, 7, 0.0F, false));
		left_underleg.cubeList.add(new ModelBox(left_underleg, 0, 0, -2.0F, 3.0F, -3.0F, 2, 8, 3, 0.0F, false));
		left_underleg.cubeList.add(new ModelBox(left_underleg, 47, 0, 1.0F, 3.0F, -3.0F, 2, 8, 3, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(0.5F, 0.5F, -1.5F);
		left_underleg.addChild(cube_r2);
		setRotationAngle(cube_r2, -0.7418F, 0.0F, 0.0F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 26, -1.5F, -4.5F, -3.5F, 3, 7, 2, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.5F, 11.5F, -1.5F);
		left_underleg.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.48F, 0.0F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 75, 29, -3.0F, -3.5F, -2.5F, 6, 6, 7, 0.0F, false));

		right_leg = new ModelRenderer(this);
		right_leg.setRotationPoint(15.0F, -23.0F, 2.0F);
		body.addChild(right_leg);
		

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(1.5F, 8.5F, -4.5F);
		right_leg.addChild(cube_r4);
		setRotationAngle(cube_r4, -0.5236F, 0.0F, 0.0F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 72, -2.5F, -11.5F, -2.5F, 5, 13, 5, 0.0F, true));

		right_underleg = new ModelRenderer(this);
		right_underleg.setRotationPoint(2.0F, 8.0F, -3.0F);
		right_leg.addChild(right_underleg);
		right_underleg.cubeList.add(new ModelBox(right_underleg, 21, 71, -4.0F, 12.0F, -6.0F, 7, 3, 9, 0.0F, true));
		right_underleg.cubeList.add(new ModelBox(right_underleg, 53, 73, -4.0F, 0.0F, -5.0F, 7, 4, 7, 0.0F, true));
		right_underleg.cubeList.add(new ModelBox(right_underleg, 0, 0, 0.0F, 3.0F, -3.0F, 2, 8, 3, 0.0F, true));
		right_underleg.cubeList.add(new ModelBox(right_underleg, 47, 0, -3.0F, 3.0F, -3.0F, 2, 8, 3, 0.0F, true));

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(-0.5F, 0.5F, -1.5F);
		right_underleg.addChild(cube_r5);
		setRotationAngle(cube_r5, -0.7418F, 0.0F, 0.0F);
		cube_r5.cubeList.add(new ModelBox(cube_r5, 0, 26, -1.5F, -4.5F, -3.5F, 3, 7, 2, 0.0F, true));

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(-0.5F, 11.5F, -1.5F);
		right_underleg.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.48F, 0.0F, 0.0F);
		cube_r6.cubeList.add(new ModelBox(cube_r6, 75, 29, -3.0F, -3.5F, -2.5F, 6, 6, 7, 0.0F, true));

		torax = new ModelRenderer(this);
		torax.setRotationPoint(6.0F, -24.0F, 2.0F);
		body.addChild(torax);
		

		cube_r7 = new ModelRenderer(this);
		cube_r7.setRotationPoint(7.0F, -9.8F, -12.7F);
		torax.addChild(cube_r7);
		setRotationAngle(cube_r7, 0.3054F, -0.3054F, 0.0F);
		cube_r7.cubeList.add(new ModelBox(cube_r7, 30, 65, -2.0F, -2.0F, 0.1F, 4, 4, 1, 0.25F, true));
		cube_r7.cubeList.add(new ModelBox(cube_r7, 30, 60, -2.0F, -2.0F, 0.1F, 4, 4, 1, -0.1F, true));

		cube_r8 = new ModelRenderer(this);
		cube_r8.setRotationPoint(-7.0F, -9.8F, -12.7F);
		torax.addChild(cube_r8);
		setRotationAngle(cube_r8, 0.3054F, 0.3054F, 0.0F);
		cube_r8.cubeList.add(new ModelBox(cube_r8, 30, 65, -2.0F, -2.0F, 0.1F, 4, 4, 1, 0.25F, false));
		cube_r8.cubeList.add(new ModelBox(cube_r8, 30, 60, -2.0F, -2.0F, 0.1F, 4, 4, 1, -0.1F, false));

		cube_r9 = new ModelRenderer(this);
		cube_r9.setRotationPoint(0.0F, -10.3F, -8.7F);
		torax.addChild(cube_r9);
		setRotationAngle(cube_r9, -0.1309F, 0.0F, 0.0F);
		cube_r9.cubeList.add(new ModelBox(cube_r9, 40, 36, -4.0F, -5.5F, -4.6F, 8, 11, 9, 0.0F, false));

		cube_r10 = new ModelRenderer(this);
		cube_r10.setRotationPoint(4.0F, -17.5F, 3.0F);
		torax.addChild(cube_r10);
		setRotationAngle(cube_r10, -0.2618F, 0.5236F, 0.0F);
		cube_r10.cubeList.add(new ModelBox(cube_r10, 77, 80, -1.0F, -4.5F, -2.0F, 4, 15, 4, 0.0F, true));

		cube_r11 = new ModelRenderer(this);
		cube_r11.setRotationPoint(-4.0F, -17.5F, 3.0F);
		torax.addChild(cube_r11);
		setRotationAngle(cube_r11, -0.2618F, -0.5236F, 0.0F);
		cube_r11.cubeList.add(new ModelBox(cube_r11, 77, 80, -3.0F, -4.5F, -2.0F, 4, 15, 4, 0.0F, false));

		cube_r12 = new ModelRenderer(this);
		cube_r12.setRotationPoint(0.0F, -1.5F, 0.0F);
		torax.addChild(cube_r12);
		setRotationAngle(cube_r12, 0.3054F, 0.0F, 0.0F);
		cube_r12.cubeList.add(new ModelBox(cube_r12, 0, 0, -8.0F, -14.5F, -9.0F, 16, 11, 15, 0.0F, false));

		cube_r13 = new ModelRenderer(this);
		cube_r13.setRotationPoint(0.0F, -1.5F, 0.0F);
		torax.addChild(cube_r13);
		setRotationAngle(cube_r13, 0.5236F, 0.0F, 0.0F);
		cube_r13.cubeList.add(new ModelBox(cube_r13, 0, 26, -5.0F, -10.5F, -5.0F, 10, 15, 10, 0.0F, false));

		left_arm = new ModelRenderer(this);
		left_arm.setRotationPoint(-8.0F, -5.0F, -4.0F);
		torax.addChild(left_arm);
		left_arm.cubeList.add(new ModelBox(left_arm, 21, 52, -6.0F, -3.0F, -2.0F, 7, 4, 4, 0.0F, false));
		left_arm.cubeList.add(new ModelBox(left_arm, 76, 42, -10.5F, -3.4F, -7.0F, 6, 6, 3, 0.0F, false));

		cube_r14 = new ModelRenderer(this);
		cube_r14.setRotationPoint(-8.0F, -4.0F, -1.5F);
		left_arm.addChild(cube_r14);
		setRotationAngle(cube_r14, 0.0F, 0.0F, -0.1309F);
		cube_r14.cubeList.add(new ModelBox(cube_r14, 0, 51, -3.0F, -6.0F, -4.5F, 6, 12, 9, 0.0F, false));

		left_minigun = new ModelRenderer(this);
		left_minigun.setRotationPoint(-7.5F, -0.5F, -7.0F);
		left_arm.addChild(left_minigun);
		left_minigun.cubeList.add(new ModelBox(left_minigun, 30, 56, -0.5F, 1.3F, -14.0F, 1, 1, 14, 0.0F, false));
		left_minigun.cubeList.add(new ModelBox(left_minigun, 30, 56, -2.5F, 0.5F, -14.0F, 1, 1, 14, 0.0F, false));
		left_minigun.cubeList.add(new ModelBox(left_minigun, 30, 56, 1.5F, 0.5F, -14.0F, 1, 1, 14, 0.0F, false));
		left_minigun.cubeList.add(new ModelBox(left_minigun, 30, 56, -2.5F, -1.5F, -14.0F, 1, 1, 14, 0.0F, false));
		left_minigun.cubeList.add(new ModelBox(left_minigun, 30, 56, -0.5F, -2.3F, -14.0F, 1, 1, 14, 0.0F, false));
		left_minigun.cubeList.add(new ModelBox(left_minigun, 30, 56, 1.5F, -1.5F, -14.0F, 1, 1, 14, 0.0F, false));
		left_minigun.cubeList.add(new ModelBox(left_minigun, 46, 56, -3.0F, -3.0F, -6.0F, 6, 6, 1, 0.0F, false));
		left_minigun.cubeList.add(new ModelBox(left_minigun, 62, 16, -3.0F, -3.0F, -13.4F, 6, 6, 3, 0.0F, false));

		right_arm = new ModelRenderer(this);
		right_arm.setRotationPoint(8.0F, -5.0F, -4.0F);
		torax.addChild(right_arm);
		right_arm.cubeList.add(new ModelBox(right_arm, 21, 52, -1.0F, -3.0F, -2.0F, 7, 4, 4, 0.0F, true));
		right_arm.cubeList.add(new ModelBox(right_arm, 76, 42, 4.5F, -3.4F, -7.0F, 6, 6, 3, 0.0F, true));

		cube_r15 = new ModelRenderer(this);
		cube_r15.setRotationPoint(8.0F, -4.0F, -1.5F);
		right_arm.addChild(cube_r15);
		setRotationAngle(cube_r15, 0.0F, 0.0F, 0.1309F);
		cube_r15.cubeList.add(new ModelBox(cube_r15, 0, 51, -3.0F, -6.0F, -4.5F, 6, 12, 9, 0.0F, true));

		right_minigun = new ModelRenderer(this);
		right_minigun.setRotationPoint(7.5F, -0.5F, -7.0F);
		right_arm.addChild(right_minigun);
		right_minigun.cubeList.add(new ModelBox(right_minigun, 30, 56, -0.5F, 1.3F, -14.0F, 1, 1, 14, 0.0F, true));
		right_minigun.cubeList.add(new ModelBox(right_minigun, 30, 56, 1.5F, 0.5F, -14.0F, 1, 1, 14, 0.0F, true));
		right_minigun.cubeList.add(new ModelBox(right_minigun, 30, 56, -2.5F, 0.5F, -14.0F, 1, 1, 14, 0.0F, true));
		right_minigun.cubeList.add(new ModelBox(right_minigun, 30, 56, 1.5F, -1.5F, -14.0F, 1, 1, 14, 0.0F, true));
		right_minigun.cubeList.add(new ModelBox(right_minigun, 30, 56, -0.5F, -2.3F, -14.0F, 1, 1, 14, 0.0F, true));
		right_minigun.cubeList.add(new ModelBox(right_minigun, 30, 56, -2.5F, -1.5F, -14.0F, 1, 1, 14, 0.0F, true));
		right_minigun.cubeList.add(new ModelBox(right_minigun, 46, 56, -3.0F, -3.0F, -6.0F, 6, 6, 1, 0.0F, true));
		right_minigun.cubeList.add(new ModelBox(right_minigun, 62, 16, -3.0F, -3.0F, -13.4F, 6, 6, 3, 0.0F, true));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		body.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}