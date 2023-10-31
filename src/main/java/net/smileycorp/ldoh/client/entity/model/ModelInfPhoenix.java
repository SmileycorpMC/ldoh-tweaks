package net.smileycorp.ldoh.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.smileycorp.ldoh.common.entity.infphoenix.EntityInfPhoenix;

public class ModelInfPhoenix extends ModelBase {
	private final ModelRenderer body;
	private final ModelRenderer torax;
	private final ModelRenderer cube_r1;
	private final ModelRenderer left;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer neck_left;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;
	private final ModelRenderer cube_r7;
	private final ModelRenderer cube_r8;
	private final ModelRenderer cube_r9;
	private final ModelRenderer cube_r10;
	private final ModelRenderer cube_r11;
	private final ModelRenderer cube_r12;
	private final ModelRenderer head_left;
	private final ModelRenderer cube_r13;
	private final ModelRenderer cube_r14;
	private final ModelRenderer cube_r15;
	private final ModelRenderer cube_r16;
	private final ModelRenderer cube_r17;
	private final ModelRenderer cube_r18;
	private final ModelRenderer cube_r19;
	private final ModelRenderer cube_r20;
	private final ModelRenderer cube_r21;
	private final ModelRenderer cube_r22;
	private final ModelRenderer right;
	private final ModelRenderer cube_r23;
	private final ModelRenderer cube_r24;
	private final ModelRenderer cube_r25;
	private final ModelRenderer neck_right;
	private final ModelRenderer cube_r26;
	private final ModelRenderer cube_r27;
	private final ModelRenderer cube_r28;
	private final ModelRenderer cube_r29;
	private final ModelRenderer cube_r30;
	private final ModelRenderer cube_r31;
	private final ModelRenderer cube_r32;
	private final ModelRenderer cube_r33;
	private final ModelRenderer head_right;
	private final ModelRenderer cube_r34;
	private final ModelRenderer cube_r35;
	private final ModelRenderer cube_r36;
	private final ModelRenderer cube_r37;
	private final ModelRenderer cube_r38;
	private final ModelRenderer cube_r39;
	private final ModelRenderer cube_r40;
	private final ModelRenderer cube_r41;
	private final ModelRenderer cube_r42;
	private final ModelRenderer cube_r43;
	private final ModelRenderer left_wing;
	private final ModelRenderer left_wing2;
	private final ModelRenderer right_wing;
	private final ModelRenderer right_wing2;
	private final ModelRenderer tentacle_left;
	private final ModelRenderer tentacle_left2;
	private final ModelRenderer tentacle_left3;
	private final ModelRenderer tentacle_mid;
	private final ModelRenderer tentacle_mid2;
	private final ModelRenderer tentacle_mid3;
	private final ModelRenderer tentacle_right;
	private final ModelRenderer tentacle_right2;
	private final ModelRenderer tentacle_right3;
	private final ModelRenderer left_leg;
	private final ModelRenderer cube_r44;
	private final ModelRenderer cube_r45;
	private final ModelRenderer right_leg;
	private final ModelRenderer cube_r46;
	private final ModelRenderer cube_r47;

	public ModelInfPhoenix() {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 15.0F, 0.0F);
		

		torax = new ModelRenderer(this);
		torax.setRotationPoint(0.0F, -1.0F, 0.0F);
		body.addChild(torax);
		setRotationAngle(torax, -0.1745F, 0.0F, 0.0F);
		torax.cubeList.add(new ModelBox(torax, 0, 0, -3.5F, -1.3F, -2.5F, 7, 5, 6, 0.0F, false));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.5F, 1.6F, 6.0F);
		torax.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.2618F, 0.0F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 22, 11, -3.0F, -1.7F, -4.0F, 5, 4, 4, 0.0F, false));

		left = new ModelRenderer(this);
		left.setRotationPoint(-0.5F, 1.0F, -2.5F);
		torax.addChild(left);
		

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(1.0F, -1.0F, -3.0F);
		left.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.1745F, 0.7854F, 0.0F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 8, 46, -1.6F, 0.3F, -0.2F, 2, 3, 0, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(1.0F, -1.0F, -3.0F);
		left.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.7854F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 12, 46, -1.4F, -1.0F, 0.5F, 2, 3, 0, 0.0F, false));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(0.0F, 0.0F, 3.0F);
		left.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0873F, 0.0F, 0.0F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 19, -2.5F, -2.4F, -5.5F, 3, 4, 4, 0.0F, false));

		neck_left = new ModelRenderer(this);
		neck_left.setRotationPoint(0.0F, -3.0F, -2.5F);
		left.addChild(neck_left);
		

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(1.0F, -1.0F, -1.0F);
		neck_left.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.6109F, 0.0F, 0.0F);
		cube_r5.cubeList.add(new ModelBox(cube_r5, 36, 46, -1.0F, -1.6F, -0.2F, 2, 2, 0, 0.0F, false));

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(1.0F, -1.0F, -1.0F);
		neck_left.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.1745F, 0.7854F, 0.0F);
		cube_r6.cubeList.add(new ModelBox(cube_r6, 32, 46, -1.0F, -0.3F, -0.2F, 2, 2, 0, 0.0F, false));

		cube_r7 = new ModelRenderer(this);
		cube_r7.setRotationPoint(1.0F, -1.5F, 0.7F);
		neck_left.addChild(cube_r7);
		setRotationAngle(cube_r7, 0.1745F, 0.0F, 0.0F);
		cube_r7.cubeList.add(new ModelBox(cube_r7, 28, 46, -1.0F, -1.6F, 0.2F, 2, 2, 0, 0.0F, false));

		cube_r8 = new ModelRenderer(this);
		cube_r8.setRotationPoint(1.0F, -1.0F, -1.0F);
		neck_left.addChild(cube_r8);
		setRotationAngle(cube_r8, 0.3491F, 0.0F, 0.0F);
		cube_r8.cubeList.add(new ModelBox(cube_r8, 16, 46, -1.0F, 1.1F, 2.1F, 2, 2, 0, 0.0F, false));

		cube_r9 = new ModelRenderer(this);
		cube_r9.setRotationPoint(1.0F, -1.0F, -1.0F);
		neck_left.addChild(cube_r9);
		setRotationAngle(cube_r9, 0.2618F, 0.6981F, -0.0873F);
		cube_r9.cubeList.add(new ModelBox(cube_r9, 20, 46, -1.0F, 1.1F, -0.3F, 2, 2, 0, 0.0F, false));

		cube_r10 = new ModelRenderer(this);
		cube_r10.setRotationPoint(1.0F, -1.5F, 0.7F);
		neck_left.addChild(cube_r10);
		setRotationAngle(cube_r10, 0.6109F, -0.7854F, 0.0F);
		cube_r10.cubeList.add(new ModelBox(cube_r10, 24, 46, -0.4F, -1.6F, 0.0F, 2, 2, 0, 0.0F, false));

		cube_r11 = new ModelRenderer(this);
		cube_r11.setRotationPoint(1.0F, -1.5F, 0.7F);
		neck_left.addChild(cube_r11);
		setRotationAngle(cube_r11, 0.6109F, 0.0F, 0.0F);
		cube_r11.cubeList.add(new ModelBox(cube_r11, 24, 46, -1.0F, -0.3F, 0.2F, 2, 2, 0, 0.0F, false));

		cube_r12 = new ModelRenderer(this);
		cube_r12.setRotationPoint(0.5F, -1.5F, 0.5F);
		neck_left.addChild(cube_r12);
		setRotationAngle(cube_r12, 0.3927F, 0.0F, 0.0F);
		cube_r12.cubeList.add(new ModelBox(cube_r12, 24, 27, -1.5F, -1.5F, -2.2F, 2, 5, 3, -0.1F, false));

		head_left = new ModelRenderer(this);
		head_left.setRotationPoint(0.5F, -2.5F, -1.5F);
		neck_left.addChild(head_left);
		

		cube_r13 = new ModelRenderer(this);
		cube_r13.setRotationPoint(0.6F, 0.3F, -2.8F);
		head_left.addChild(cube_r13);
		setRotationAngle(cube_r13, 0.3927F, 0.0F, -0.6109F);
		cube_r13.cubeList.add(new ModelBox(cube_r13, 32, 42, -0.8F, -0.2F, -0.8F, 2, 0, 2, 0.0F, false));

		cube_r14 = new ModelRenderer(this);
		cube_r14.setRotationPoint(0.6F, -0.6F, -2.1F);
		head_left.addChild(cube_r14);
		setRotationAngle(cube_r14, 0.4363F, 0.2618F, -0.4363F);
		cube_r14.cubeList.add(new ModelBox(cube_r14, 0, 42, -1.4F, -0.3F, -0.5F, 2, 0, 3, 0.0F, false));

		cube_r15 = new ModelRenderer(this);
		cube_r15.setRotationPoint(0.3F, -1.9F, 0.7F);
		head_left.addChild(cube_r15);
		setRotationAngle(cube_r15, 0.1745F, 0.2182F, -0.5236F);
		cube_r15.cubeList.add(new ModelBox(cube_r15, 8, 42, -0.2F, -0.2F, -2.5F, 2, 0, 3, 0.0F, false));

		cube_r16 = new ModelRenderer(this);
		cube_r16.setRotationPoint(0.5F, -1.8F, -1.8F);
		head_left.addChild(cube_r16);
		setRotationAngle(cube_r16, 0.5672F, 0.1745F, -0.8727F);
		cube_r16.cubeList.add(new ModelBox(cube_r16, 32, 44, -1.1F, -0.3F, -1.2F, 2, 0, 2, 0.0F, false));

		cube_r17 = new ModelRenderer(this);
		cube_r17.setRotationPoint(-0.5F, 0.3F, -1.9F);
		head_left.addChild(cube_r17);
		setRotationAngle(cube_r17, 0.7854F, 0.0F, 0.0F);
		cube_r17.cubeList.add(new ModelBox(cube_r17, 16, 42, -0.4F, -0.9F, -1.6F, 1, 1, 2, -0.05F, false));

		cube_r18 = new ModelRenderer(this);
		cube_r18.setRotationPoint(0.0F, 0.0F, 0.0F);
		head_left.addChild(cube_r18);
		setRotationAngle(cube_r18, 0.3927F, 0.0F, 0.0F);
		cube_r18.cubeList.add(new ModelBox(cube_r18, 22, 42, -0.9F, -0.6F, -3.7F, 1, 1, 2, -0.1F, false));
		cube_r18.cubeList.add(new ModelBox(cube_r18, 37, 55, -1.7F, -2.4F, -2.1F, 2, 3, 4, -0.25F, false));

		cube_r19 = new ModelRenderer(this);
		cube_r19.setRotationPoint(-1.5F, -1.9F, -0.1F);
		head_left.addChild(cube_r19);
		setRotationAngle(cube_r19, 1.4835F, -0.2618F, -0.5585F);
		cube_r19.cubeList.add(new ModelBox(cube_r19, 22, 51, -0.4F, -1.1F, -0.5F, 1, 1, 3, -0.1F, false));

		cube_r20 = new ModelRenderer(this);
		cube_r20.setRotationPoint(-1.5F, -0.9F, 0.9F);
		head_left.addChild(cube_r20);
		setRotationAngle(cube_r20, -0.3927F, -0.7854F, -0.3927F);
		cube_r20.cubeList.add(new ModelBox(cube_r20, 28, 46, 0.3F, -4.4F, -1.9F, 0, 5, 3, 0.0F, true));

		cube_r21 = new ModelRenderer(this);
		cube_r21.setRotationPoint(0.0F, -2.0F, 0.0F);
		head_left.addChild(cube_r21);
		setRotationAngle(cube_r21, -0.3927F, 0.0F, 0.0F);
		cube_r21.cubeList.add(new ModelBox(cube_r21, 30, 55, -0.8F, -3.4F, -0.1F, 1, 4, 0, 0.0F, false));

		cube_r22 = new ModelRenderer(this);
		cube_r22.setRotationPoint(0.0F, 0.0F, 1.0F);
		head_left.addChild(cube_r22);
		setRotationAngle(cube_r22, -0.2618F, 0.0F, 0.0F);
		cube_r22.cubeList.add(new ModelBox(cube_r22, 16, 37, -1.7F, -2.7F, -1.2F, 2, 3, 2, -0.26F, false));

		right = new ModelRenderer(this);
		right.setRotationPoint(0.5F, 1.0F, -2.5F);
		torax.addChild(right);
		

		cube_r23 = new ModelRenderer(this);
		cube_r23.setRotationPoint(-1.0F, -1.0F, -3.0F);
		right.addChild(cube_r23);
		setRotationAngle(cube_r23, 0.1745F, -0.7854F, 0.0F);
		cube_r23.cubeList.add(new ModelBox(cube_r23, 8, 46, -0.4F, 0.3F, -0.2F, 2, 3, 0, 0.0F, true));

		cube_r24 = new ModelRenderer(this);
		cube_r24.setRotationPoint(-1.0F, -1.0F, -3.0F);
		right.addChild(cube_r24);
		setRotationAngle(cube_r24, 0.0F, -0.7854F, 0.0F);
		cube_r24.cubeList.add(new ModelBox(cube_r24, 12, 46, -0.6F, -1.0F, 0.5F, 2, 3, 0, 0.0F, true));

		cube_r25 = new ModelRenderer(this);
		cube_r25.setRotationPoint(0.0F, 0.0F, 3.0F);
		right.addChild(cube_r25);
		setRotationAngle(cube_r25, 0.0873F, 0.0F, 0.0F);
		cube_r25.cubeList.add(new ModelBox(cube_r25, 0, 19, -0.5F, -2.4F, -5.5F, 3, 4, 4, 0.0F, true));

		neck_right = new ModelRenderer(this);
		neck_right.setRotationPoint(0.0F, -3.0F, -2.5F);
		right.addChild(neck_right);
		

		cube_r26 = new ModelRenderer(this);
		cube_r26.setRotationPoint(-1.0F, -1.0F, -1.0F);
		neck_right.addChild(cube_r26);
		setRotationAngle(cube_r26, 0.6109F, 0.0F, 0.0F);
		cube_r26.cubeList.add(new ModelBox(cube_r26, 36, 46, -1.0F, -1.6F, -0.2F, 2, 2, 0, 0.0F, true));

		cube_r27 = new ModelRenderer(this);
		cube_r27.setRotationPoint(-1.0F, -1.0F, -1.0F);
		neck_right.addChild(cube_r27);
		setRotationAngle(cube_r27, 0.1745F, -0.7854F, 0.0F);
		cube_r27.cubeList.add(new ModelBox(cube_r27, 32, 46, -1.0F, -0.3F, -0.2F, 2, 2, 0, 0.0F, true));

		cube_r28 = new ModelRenderer(this);
		cube_r28.setRotationPoint(-1.0F, -1.5F, 0.7F);
		neck_right.addChild(cube_r28);
		setRotationAngle(cube_r28, 0.1745F, 0.0F, 0.0F);
		cube_r28.cubeList.add(new ModelBox(cube_r28, 28, 46, -1.0F, -1.6F, 0.2F, 2, 2, 0, 0.0F, true));

		cube_r29 = new ModelRenderer(this);
		cube_r29.setRotationPoint(-1.0F, -1.0F, -1.0F);
		neck_right.addChild(cube_r29);
		setRotationAngle(cube_r29, 0.3491F, 0.0F, 0.0F);
		cube_r29.cubeList.add(new ModelBox(cube_r29, 16, 46, -1.0F, 1.1F, 2.1F, 2, 2, 0, 0.0F, true));

		cube_r30 = new ModelRenderer(this);
		cube_r30.setRotationPoint(-1.0F, -1.0F, -1.0F);
		neck_right.addChild(cube_r30);
		setRotationAngle(cube_r30, 0.2618F, -0.6981F, 0.0873F);
		cube_r30.cubeList.add(new ModelBox(cube_r30, 20, 46, -1.0F, 1.1F, -0.3F, 2, 2, 0, 0.0F, true));

		cube_r31 = new ModelRenderer(this);
		cube_r31.setRotationPoint(-1.0F, -1.5F, 0.7F);
		neck_right.addChild(cube_r31);
		setRotationAngle(cube_r31, 0.6109F, 0.7854F, 0.0F);
		cube_r31.cubeList.add(new ModelBox(cube_r31, 24, 46, -1.6F, -1.6F, 0.0F, 2, 2, 0, 0.0F, true));

		cube_r32 = new ModelRenderer(this);
		cube_r32.setRotationPoint(-1.0F, -1.5F, 0.7F);
		neck_right.addChild(cube_r32);
		setRotationAngle(cube_r32, 0.6109F, 0.0F, 0.0F);
		cube_r32.cubeList.add(new ModelBox(cube_r32, 24, 46, -1.0F, -0.3F, 0.2F, 2, 2, 0, 0.0F, true));

		cube_r33 = new ModelRenderer(this);
		cube_r33.setRotationPoint(-0.5F, -1.5F, 0.5F);
		neck_right.addChild(cube_r33);
		setRotationAngle(cube_r33, 0.3927F, 0.0F, 0.0F);
		cube_r33.cubeList.add(new ModelBox(cube_r33, 24, 27, -0.5F, -1.5F, -2.2F, 2, 5, 3, -0.1F, true));

		head_right = new ModelRenderer(this);
		head_right.setRotationPoint(-0.5F, -2.5F, -1.5F);
		neck_right.addChild(head_right);
		

		cube_r34 = new ModelRenderer(this);
		cube_r34.setRotationPoint(-0.6F, 0.3F, -2.8F);
		head_right.addChild(cube_r34);
		setRotationAngle(cube_r34, 0.3927F, 0.0F, 0.6109F);
		cube_r34.cubeList.add(new ModelBox(cube_r34, 32, 42, -1.2F, -0.2F, -0.8F, 2, 0, 2, 0.0F, true));

		cube_r35 = new ModelRenderer(this);
		cube_r35.setRotationPoint(-0.6F, -0.6F, -2.1F);
		head_right.addChild(cube_r35);
		setRotationAngle(cube_r35, 0.4363F, -0.2618F, 0.4363F);
		cube_r35.cubeList.add(new ModelBox(cube_r35, 0, 42, -0.6F, -0.3F, -0.5F, 2, 0, 3, 0.0F, true));

		cube_r36 = new ModelRenderer(this);
		cube_r36.setRotationPoint(-0.3F, -1.9F, 0.7F);
		head_right.addChild(cube_r36);
		setRotationAngle(cube_r36, 0.1745F, -0.2182F, 0.5236F);
		cube_r36.cubeList.add(new ModelBox(cube_r36, 8, 42, -1.8F, -0.2F, -2.5F, 2, 0, 3, 0.0F, true));

		cube_r37 = new ModelRenderer(this);
		cube_r37.setRotationPoint(-0.5F, -1.8F, -1.8F);
		head_right.addChild(cube_r37);
		setRotationAngle(cube_r37, 0.5672F, -0.1745F, 0.8727F);
		cube_r37.cubeList.add(new ModelBox(cube_r37, 32, 44, -0.9F, -0.3F, -1.2F, 2, 0, 2, 0.0F, true));

		cube_r38 = new ModelRenderer(this);
		cube_r38.setRotationPoint(0.5F, 0.3F, -1.9F);
		head_right.addChild(cube_r38);
		setRotationAngle(cube_r38, 0.7854F, 0.0F, 0.0F);
		cube_r38.cubeList.add(new ModelBox(cube_r38, 16, 42, -0.6F, -0.9F, -1.6F, 1, 1, 2, -0.05F, true));

		cube_r39 = new ModelRenderer(this);
		cube_r39.setRotationPoint(0.0F, 0.0F, 0.0F);
		head_right.addChild(cube_r39);
		setRotationAngle(cube_r39, 0.3927F, 0.0F, 0.0F);
		cube_r39.cubeList.add(new ModelBox(cube_r39, 22, 42, -0.1F, -0.6F, -3.7F, 1, 1, 2, -0.1F, true));
		cube_r39.cubeList.add(new ModelBox(cube_r39, 37, 55, -0.3F, -2.4F, -2.1F, 2, 3, 4, -0.25F, true));

		cube_r40 = new ModelRenderer(this);
		cube_r40.setRotationPoint(1.5F, -1.9F, -0.1F);
		head_right.addChild(cube_r40);
		setRotationAngle(cube_r40, 1.4835F, 0.2618F, 0.5585F);
		cube_r40.cubeList.add(new ModelBox(cube_r40, 22, 51, -0.6F, -1.1F, -0.5F, 1, 1, 3, -0.1F, true));

		cube_r41 = new ModelRenderer(this);
		cube_r41.setRotationPoint(1.5F, -0.9F, 0.9F);
		head_right.addChild(cube_r41);
		setRotationAngle(cube_r41, -0.3927F, 0.7854F, 0.3927F);
		cube_r41.cubeList.add(new ModelBox(cube_r41, 28, 46, -0.3F, -4.4F, -1.9F, 0, 5, 3, 0.0F, false));

		cube_r42 = new ModelRenderer(this);
		cube_r42.setRotationPoint(0.0F, -2.0F, 0.0F);
		head_right.addChild(cube_r42);
		setRotationAngle(cube_r42, -0.3927F, 0.0F, 0.0F);
		cube_r42.cubeList.add(new ModelBox(cube_r42, 30, 55, -0.2F, -3.4F, -0.1F, 1, 4, 0, 0.0F, true));

		cube_r43 = new ModelRenderer(this);
		cube_r43.setRotationPoint(0.0F, 0.0F, 1.0F);
		head_right.addChild(cube_r43);
		setRotationAngle(cube_r43, -0.2618F, 0.0F, 0.0F);
		cube_r43.cubeList.add(new ModelBox(cube_r43, 16, 37, -0.3F, -2.7F, -1.2F, 2, 3, 2, -0.26F, true));

		left_wing = new ModelRenderer(this);
		left_wing.setRotationPoint(-1.8F, -1.35F, -0.5F);
		torax.addChild(left_wing);
		left_wing.cubeList.add(new ModelBox(left_wing, 40, 11, -8.7F, -0.9546F, -0.9479F, 9, 1, 2, 0.0F, false));
		left_wing.cubeList.add(new ModelBox(left_wing, 26, 0, -8.7F, 0.0454F, 1.0521F, 9, 0, 6, 0.0F, false));

		left_wing2 = new ModelRenderer(this);
		left_wing2.setRotationPoint(-8.7F, -0.75F, 0.0F);
		left_wing.addChild(left_wing2);
		left_wing2.cubeList.add(new ModelBox(left_wing2, 16, 35, -5.0F, -0.2046F, -0.4479F, 5, 1, 1, 0.0F, false));
		left_wing2.cubeList.add(new ModelBox(left_wing2, 0, 11, -5.0F, 0.7954F, 0.3521F, 5, 0, 6, 0.0F, false));

		right_wing = new ModelRenderer(this);
		right_wing.setRotationPoint(1.8F, -1.35F, -0.5F);
		torax.addChild(right_wing);
		right_wing.cubeList.add(new ModelBox(right_wing, 40, 11, -0.3F, -0.9546F, -0.9479F, 9, 1, 2, 0.0F, true));
		right_wing.cubeList.add(new ModelBox(right_wing, 26, 0, -0.3F, 0.0454F, 1.0521F, 9, 0, 6, 0.0F, true));

		right_wing2 = new ModelRenderer(this);
		right_wing2.setRotationPoint(8.7F, -0.75F, 0.0F);
		right_wing.addChild(right_wing2);
		right_wing2.cubeList.add(new ModelBox(right_wing2, 16, 35, 0.0F, -0.2046F, -0.4479F, 5, 1, 1, 0.0F, true));
		right_wing2.cubeList.add(new ModelBox(right_wing2, 0, 11, 0.0F, 0.7954F, 0.3521F, 5, 0, 6, 0.0F, true));

		tentacle_left = new ModelRenderer(this);
		tentacle_left.setRotationPoint(-2.5F, 1.0F, 6.0F);
		torax.addChild(tentacle_left);
		setRotationAngle(tentacle_left, 0.0F, 0.0F, 0.0F);
		tentacle_left.cubeList.add(new ModelBox(tentacle_left, 14, 19, -1.0F, -2.0F, -1.0F, 2, 2, 5, 0.0F, false));

		tentacle_left2 = new ModelRenderer(this);
		tentacle_left2.setRotationPoint(0.0F, -1.0F, 4.0F);
		tentacle_left.addChild(tentacle_left2);
		tentacle_left2.cubeList.add(new ModelBox(tentacle_left2, 28, 19, -1.0F, -1.0F, -1.0F, 2, 2, 5, -0.25F, false));

		tentacle_left3 = new ModelRenderer(this);
		tentacle_left3.setRotationPoint(0.0F, 0.0F, 3.5F);
		tentacle_left2.addChild(tentacle_left3);
		tentacle_left3.cubeList.add(new ModelBox(tentacle_left3, 34, 27, -0.5F, -0.5F, -0.5F, 1, 1, 4, 0.0F, false));

		tentacle_mid = new ModelRenderer(this);
		tentacle_mid.setRotationPoint(0.0F, 1.0F, 6.0F);
		torax.addChild(tentacle_mid);
		setRotationAngle(tentacle_mid, 0.0F, 0.0F, 0.0F);
		tentacle_mid.cubeList.add(new ModelBox(tentacle_mid, 14, 19, -1.0F, -2.0F, -1.0F, 2, 2, 5, 0.0F, false));

		tentacle_mid2 = new ModelRenderer(this);
		tentacle_mid2.setRotationPoint(0.0F, -1.0F, 4.0F);
		tentacle_mid.addChild(tentacle_mid2);
		tentacle_mid2.cubeList.add(new ModelBox(tentacle_mid2, 28, 19, -1.0F, -1.0F, -1.0F, 2, 2, 5, -0.25F, false));

		tentacle_mid3 = new ModelRenderer(this);
		tentacle_mid3.setRotationPoint(0.0F, 0.0F, 3.5F);
		tentacle_mid2.addChild(tentacle_mid3);
		tentacle_mid3.cubeList.add(new ModelBox(tentacle_mid3, 34, 27, -0.5F, -0.5F, -0.5F, 1, 1, 4, 0.0F, false));

		tentacle_right = new ModelRenderer(this);
		tentacle_right.setRotationPoint(2.5F, 1.0F, 6.0F);
		torax.addChild(tentacle_right);
		setRotationAngle(tentacle_right, 0.0F, 0.0F, 0.0F);
		tentacle_right.cubeList.add(new ModelBox(tentacle_right, 14, 19, -1.0F, -2.0F, -1.0F, 2, 2, 5, 0.0F, true));

		tentacle_right2 = new ModelRenderer(this);
		tentacle_right2.setRotationPoint(0.0F, -1.0F, 4.0F);
		tentacle_right.addChild(tentacle_right2);
		tentacle_right2.cubeList.add(new ModelBox(tentacle_right2, 28, 19, -1.0F, -1.0F, -1.0F, 2, 2, 5, -0.25F, true));

		tentacle_right3 = new ModelRenderer(this);
		tentacle_right3.setRotationPoint(0.0F, 0.0F, 3.5F);
		tentacle_right2.addChild(tentacle_right3);
		tentacle_right3.cubeList.add(new ModelBox(tentacle_right3, 34, 27, -0.5F, -0.5F, -0.5F, 1, 1, 4, 0.0F, true));

		left_leg = new ModelRenderer(this);
		left_leg.setRotationPoint(-2.5F, 0.0F, -1.75F);
		body.addChild(left_leg);
		setRotationAngle(left_leg, 0.0F, -0.7854F, 0.0F);
		left_leg.cubeList.add(new ModelBox(left_leg, 0, 35, -0.5858F, 2.0F, 2.1642F, 2, 5, 2, 0.0F, false));

		cube_r44 = new ModelRenderer(this);
		cube_r44.setRotationPoint(0.4142F, 8.5F, 3.2642F);
		left_leg.addChild(cube_r44);
		setRotationAngle(cube_r44, -0.3927F, 0.0F, 0.0F);
		cube_r44.cubeList.add(new ModelBox(cube_r44, 28, 42, -0.5F, -1.5F, -1.2F, 1, 3, 1, 0.0F, false));

		cube_r45 = new ModelRenderer(this);
		cube_r45.setRotationPoint(0.4142F, 1.5F, 1.6642F);
		left_leg.addChild(cube_r45);
		setRotationAngle(cube_r45, 0.7854F, 0.0F, 0.0F);
		cube_r45.cubeList.add(new ModelBox(cube_r45, 0, 27, -1.5F, -2.5F, -1.5F, 3, 5, 3, 0.0F, false));

		right_leg = new ModelRenderer(this);
		right_leg.setRotationPoint(2.5F, 0.0F, -1.75F);
		body.addChild(right_leg);
		setRotationAngle(right_leg, 0.0F, 0.7854F, 0.0F);
		right_leg.cubeList.add(new ModelBox(right_leg, 0, 35, -1.4142F, 2.0F, 2.1642F, 2, 5, 2, 0.0F, true));

		cube_r46 = new ModelRenderer(this);
		cube_r46.setRotationPoint(-0.4142F, 8.5F, 3.2642F);
		right_leg.addChild(cube_r46);
		setRotationAngle(cube_r46, -0.3927F, 0.0F, 0.0F);
		cube_r46.cubeList.add(new ModelBox(cube_r46, 28, 42, -0.5F, -1.5F, -1.2F, 1, 3, 1, 0.0F, true));

		cube_r47 = new ModelRenderer(this);
		cube_r47.setRotationPoint(-0.4142F, 1.5F, 1.6642F);
		right_leg.addChild(cube_r47);
		setRotationAngle(cube_r47, 0.7854F, 0.0F, 0.0F);
		cube_r47.cubeList.add(new ModelBox(cube_r47, 0, 27, -1.5F, -2.5F, -1.5F, 3, 5, 3, 0.0F, true));
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float age, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
		if (entity instanceof EntityInfPhoenix) {
			float neck = Math.min(((EntityInfPhoenix) entity).getNeckPhase(), 10);
			left.rotateAngleZ = 0.0785398F * neck;
			right.rotateAngleZ = -0.0785398F * neck;
		}
		right_wing.rotateAngleZ = -MathHelper.cos(age / 2.0F) * 3.1415927F * 0.2F;
		left_wing.rotateAngleZ = MathHelper.cos(age / 2.0F) * 3.1415927F * 0.2F;
		right_wing2.rotateAngleZ = -MathHelper.cos(age / 2.0F) * 3.1415927F * 0.1F;
		left_wing2.rotateAngleZ = MathHelper.cos(age / 2.0F) * 3.1415927F * 0.1F;
		tentacle_left.rotateAngleX = (float) Math.sin(age * 0.25f) * 0.8f;
		tentacle_left2.rotateAngleX = -(float) Math.sin(age * 0.5f) * 0.8f;
		tentacle_left3.rotateAngleX = (float) Math.sin(age) * 0.8f;
		tentacle_mid.rotateAngleX = -(float) Math.cos(age * 0.125f) * 0.8f;
		tentacle_mid2.rotateAngleX = (float) Math.cos(age * 0.25f) * 0.8f;
		tentacle_mid3.rotateAngleX = -(float) Math.cos(age * 0.5f) * 0.8f;
		tentacle_right.rotateAngleX = -(float) Math.sin(age * 0.25f) * 0.8f;
		tentacle_right2.rotateAngleX = (float) Math.sin(age * 0.5f) * 0.8f;
		tentacle_right3.rotateAngleX = -(float) Math.sin(age) * 0.8f;
		super.setRotationAngles(limbSwing, limbSwingAmount, age, netHeadYaw, headPitch, scaleFactor, entity);
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