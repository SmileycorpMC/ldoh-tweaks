package net.smileycorp.ldoh.client.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.entity.IEnemyMachine;
import net.smileycorp.ldoh.common.util.TurretUpgrade;

import java.awt.*;

/**
 * turret - Undefined
 * Created using Tabula 7.1.0
 */
public class ModelTurret extends ModelBase {
    public ModelRenderer base;
    public ModelRenderer axel;
    public ModelRenderer rightpivot;
    public ModelRenderer leftpivot;
    public ModelRenderer shield_left;
    public ModelRenderer shield_right;
    public ModelRenderer gun_back;
    public ModelRenderer gun_mount;
    public ModelRenderer ammo_box;
    public ModelRenderer ammo_belt;
    public ModelRenderer sensor;
    public ModelRenderer gun_middle;
    public ModelRenderer gun_bottom;
    public ModelRenderer gun_muzzle;
    public ModelRenderer gun_arch;
    public ModelRenderer gun_front;
    public ModelRenderer barrel_0;
    public ModelRenderer barrel_3;
    public ModelRenderer barrel_1;
    public ModelRenderer barrel_2;
    public ModelRenderer barrel_4;
    public ModelRenderer barrel_5;

    public ModelTurret() {
        textureWidth = 64;
        textureHeight = 64;
        leftpivot = new ModelRenderer(this, 0, 0);
        leftpivot.setRotationPoint(0.0F, 0.0F, 0.0F);
        leftpivot.addBox(5.0F, -12.0F, -3.0F, 1, 11, 6, 0.0F);
        gun_mount = new ModelRenderer(this, 20, 55);
        gun_mount.setRotationPoint(0.0F, 0.0F, 0.0F);
        gun_mount.addBox(-2.0F, 1.0F, -2.0F, 4, 6, 1, 0.0F);
        gun_arch = new ModelRenderer(this, 34, 60);
        gun_arch.setRotationPoint(0.0F, 5.7F, -11.8F);
        gun_arch.addBox(-1.0F, -1.5F, 0.0F, 2, 2, 1, 0.0F);
        setRotateAngle(gun_arch, 0.7853981633974483F, 0.0F, 0.0F);
        ammo_box = new ModelRenderer(this, 0, 42);
        ammo_box.setRotationPoint(0.0F, 0.0F, 1.0F);
        ammo_box.addBox(-4.0F, 1.5F, -0.5F, 3, 4, 7, 0.0F);
        gun_middle = new ModelRenderer(this, 20, 40);
        gun_middle.setRotationPoint(0.0F, 3.0F, -2.0F);
        gun_middle.addBox(-0.5F, -0.5F, -11.5F, 1, 1, 12, 0.0F);
        base = new ModelRenderer(this, 0, 17);
        base.setRotationPoint(0.0F, 24.0F, 0.0F);
        base.addBox(-5.0F, -2.0F, -5.0F, 10, 2, 10, 0.0F);
        shield_right = new ModelRenderer(this, 56, 0);
        shield_right.setRotationPoint(0.0F, -11.0F, -0.5F);
        shield_right.addBox(2.0F, -4.0F, -1.0F, 3, 11, 1, 0.0F);
        barrel_3 = new ModelRenderer(this, 34, 41);
        barrel_3.setRotationPoint(0.0F, 1.0F, 0.0F);
        barrel_3.addBox(-0.5F, -0.5F, -10.0F, 1, 1, 10, 0.0F);
        setRotateAngle(barrel_3, 0.0F, 0.0F, 0.7853981633974483F);
        barrel_2 = new ModelRenderer(this, 34, 41);
        barrel_2.setRotationPoint(-0.85F, 0.5F, 0.0F);
        barrel_2.addBox(-0.5F, -0.5F, -10.0F, 1, 1, 10, 0.0F);
        setRotateAngle(barrel_2, 0.0F, 0.0F, 0.2617993877991494F);
        rightpivot = new ModelRenderer(this, 0, 0);
        rightpivot.setRotationPoint(0.0F, 0.0F, 0.0F);
        rightpivot.addBox(-6.0F, -12.0F, -3.0F, 1, 11, 6, 0.0F);
        gun_muzzle = new ModelRenderer(this, 46, 47);
        gun_muzzle.setRotationPoint(0.0F, 4.0F, -1.5F);
        gun_muzzle.addBox(-0.5F, -0.5F, -13.5F, 1, 1, 3, 0.0F);
        setRotateAngle(gun_muzzle, 0.0F, 0.0F, 0.7853981633974483F);
        barrel_4 = new ModelRenderer(this, 34, 41);
        barrel_4.setRotationPoint(0.85F, 0.5F, 0.0F);
        barrel_4.addBox(-0.5F, -0.5F, -10.0F, 1, 1, 10, 0.0F);
        setRotateAngle(barrel_4, 0.0F, 0.0F, 1.0471975511965976F);
        ammo_belt = new ModelRenderer(this, 0, 46);
        ammo_belt.setRotationPoint(-1.6F, 0.0F, 2.0F);
        ammo_belt.addBox(0.0F, 0.0F, 0.0F, 0, 2, 1, 0.0F);
        setRotateAngle(ammo_belt, 0.0F, 0.0F, 0.7853981633974483F);
        gun_back = new ModelRenderer(this, 0, 53);
        gun_back.setRotationPoint(0.0F, 1.0F, 0.0F);
        gun_back.addBox(-2.0F, 0.0F, -1.0F, 4, 3, 6, 0.0F);
        gun_front = new ModelRenderer(this, 34, 56);
        gun_front.setRotationPoint(0.0F, 2.2F, -13.0F);
        gun_front.addBox(-1.0F, -0.5F, 0.0F, 2, 3, 1, 0.0F);
        barrel_1 = new ModelRenderer(this, 34, 41);
        barrel_1.setRotationPoint(-0.85F, -0.5F, 0.0F);
        barrel_1.addBox(-0.5F, -0.5F, -10.0F, 1, 1, 10, 0.0F);
        setRotateAngle(barrel_1, 0.0F, 0.0F, 1.0471975511965976F);
        sensor = new ModelRenderer(this, 20, 49);
        sensor.setRotationPoint(0.0F, 0.0F, 0.0F);
        sensor.addBox(0.0F, -3.0F, -2.0F, 3, 2, 1, 0.0F);
        barrel_5 = new ModelRenderer(this, 34, 41);
        barrel_5.setRotationPoint(0.85F, -0.5F, 0.0F);
        barrel_5.addBox(-0.5F, -0.5F, -10.0F, 1, 1, 10, 0.0F);
        setRotateAngle(barrel_5, 0.0F, 0.0F, 0.2617993877991494F);
        axel = new ModelRenderer(this, 0, 62);
        axel.setRotationPoint(0.0F, 13.0F, -0.5F);
        axel.addBox(-7.0F, 0.0F, 0.0F, 14, 1, 1, 0.0F);
        shield_left = new ModelRenderer(this, 48, 0);
        shield_left.setRotationPoint(0.0F, -11.0F, -0.5F);
        shield_left.addBox(-5.0F, -4.0F, -1.0F, 3, 11, 1, 0.0F);
        setRotateAngle(shield_left, 0.0F, 0.0F, -0.017453292519943295F);
        gun_bottom = new ModelRenderer(this, 30, 53);
        gun_bottom.setRotationPoint(0.0F, 5.5F, 0.0F);
        gun_bottom.addBox(-1.0F, -0.5F, -11.5F, 2, 1, 10, 0.0F);
        barrel_0 = new ModelRenderer(this, 34, 41);
        barrel_0.setRotationPoint(0.0F, -1.0F, 0.0F);
        barrel_0.addBox(-0.5F, -0.5F, -10.0F, 1, 1, 10, 0.0F);
        setRotateAngle(barrel_0, 0.0F, 0.0F, 0.7853981633974483F);
        base.addChild(leftpivot);
        axel.addChild(gun_mount);
        gun_mount.addChild(gun_arch);
        gun_back.addChild(ammo_box);
        gun_mount.addChild(gun_middle);
        base.addChild(shield_right);
        gun_middle.addChild(barrel_3);
        gun_middle.addChild(barrel_2);
        base.addChild(rightpivot);
        gun_mount.addChild(gun_muzzle);
        gun_middle.addChild(barrel_4);
        gun_back.addChild(ammo_belt);
        axel.addChild(gun_back);
        gun_mount.addChild(gun_front);
        gun_middle.addChild(barrel_1);
        gun_back.addChild(sensor);
        gun_middle.addChild(barrel_5);
        base.addChild(shield_left);
        gun_mount.addChild(gun_bottom);
        gun_middle.addChild(barrel_0);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();
        boolean australium = (entity instanceof EntityTurret && ((EntityTurret) entity).hasUpgrade(TurretUpgrade.AUSTRALIUM));
        //tint the turret the australium colour if it has the upgrade
        if (australium) GlStateManager.color(1, 0.831372549f, 0);
        else if (entity == null) GlStateManager.color(0.45f, 0.45f, 0.45f);
        else if (!(entity instanceof EntityPlayer || (entity instanceof IEnemyMachine && ((IEnemyMachine) entity).isEnemy()))) {
            Color colour = null;
            if (entity instanceof IEnemyMachine) {
                //check if the turret is an enemy turret
                if (((IEnemyMachine) entity).isEnemy()) colour = new Color(0x2C3811);
                //if the entity is a player, the turret is an enemy turret in the inventory
            } else if (entity instanceof EntityPlayer) colour = new Color(0x2C3811);
            if (colour == null) {
                if (entity.getTeam() != null) {
                    //get the team colour
                    colour = new Color(Minecraft.getMinecraft().fontRenderer.getColorCode(entity.getTeam().getColor().formattingCode));
                } else {
                    //apply the dark grey colour to the hull if no other colours needed
                    colour = new Color(0x404040);
                }
            }
            GlStateManager.color(colour.getRed() / 255f, colour.getGreen() / 255f, colour.getBlue() / 255f);
        }
        if (entity instanceof EntityTurret) {
            //pivot turret gun based on facing
            axel.rotateAngleX = headPitch * 0.0174533f;
            //rotate turret gun while firing
            gun_middle.rotateAngleZ = ((EntityTurret) entity).getSpin();
            //if entity isn't a turret this is being rendered from the inventory, so apply inventory animation
        } else gun_middle.rotateAngleZ = (0.0261799388f * age);
        base.render(scale);
        //remove australium tint
        if (!australium) GlStateManager.color(1, 1, 1);
        axel.render(scale);
        GlStateManager.color(1, 1, 1);
        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch, float scale, Entity entity) {
        if (entity != null) {
            switch (((EntityTurret) entity).getFacing()) {
                case UP:
                    break;
                //rotate the turret model based on direction it is placed on
                //ai isn't currently updated for directions, so turrets are restricted to floor placement
                case DOWN:
                    GlStateManager.rotate(180, 1, 0, 0);
                    GlStateManager.translate(0, -2.5, 0);
                    break;
                case NORTH:
                    GlStateManager.rotate(-90, 1, 0, 0);
                    GlStateManager.translate(0, -1.25, 1.25);
                    break;
                case SOUTH:
                    GlStateManager.rotate(90, 1, 0, 0);
                    GlStateManager.rotate(180, 0, 1, 0);
                    GlStateManager.translate(0, -1.25, 1.25);
                    break;
                case EAST:
                    GlStateManager.rotate(-90, 0, 1, 0);
                    GlStateManager.rotate(90, 1, 0, 0);
                    GlStateManager.translate(0, -1.25, -1.25);
                    break;
                case WEST:
                    GlStateManager.rotate(90, 0, 1, 0);
                    GlStateManager.rotate(90, 1, 0, 0);
                    GlStateManager.translate(0, -1.25, -1.25);
                    break;
            }
            //rotate turret based on it's facing
            base.rotateAngleY = yaw * 0.0174533f;
            axel.rotateAngleY = yaw * 0.0174533f;
        }
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
