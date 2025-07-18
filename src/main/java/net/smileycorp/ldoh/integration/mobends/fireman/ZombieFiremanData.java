package net.smileycorp.ldoh.integration.mobends.fireman;

import goblinbob.mobends.core.math.vector.Vec3f;
import goblinbob.mobends.standard.data.ZombieDataBase;
import net.smileycorp.ldoh.common.entity.zombie.EntityZombieFireman;

public class ZombieFiremanData extends ZombieDataBase<EntityZombieFireman> {

    private final ZombieFiremanController controller = new ZombieFiremanController();

    private final float scale = 1.05f;

    public ZombieFiremanData(EntityZombieFireman entity) {
        super(entity);
    }

    @Override
    public void initModelPose() {
        super.initModelPose();
        this.body.scale = new Vec3f(scale, scale, scale);
        this.head.scale = new Vec3f(scale, scale, scale);
        this.rightArm.scale = new Vec3f(scale, scale, scale);
        this.leftArm.scale = new Vec3f(scale, scale, scale);
        this.rightLeg.scale = new Vec3f(scale, scale, scale);
        this.leftLeg.scale = new Vec3f(scale, scale, scale);
        this.rightForeArm.scale = new Vec3f(scale, scale, scale);
        this.leftForeArm.scale = new Vec3f(scale, scale, scale);
        this.leftForeLeg.scale = new Vec3f(scale, scale, scale);
        this.rightForeLeg.scale = new Vec3f(scale, scale, scale);
    }

    @Override
    public ZombieFiremanController getController() {
        return this.controller;
    }

    @Override
    public void onTicksRestart() {
    }

}
