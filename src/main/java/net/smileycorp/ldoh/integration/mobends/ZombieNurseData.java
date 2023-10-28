package net.smileycorp.ldoh.integration.mobends;

import goblinbob.mobends.core.client.model.ModelPartTransform;
import goblinbob.mobends.core.math.SmoothOrientation;
import goblinbob.mobends.standard.client.renderer.entity.SwordTrail;
import goblinbob.mobends.standard.data.ZombieDataBase;
import net.smileycorp.ldoh.common.entity.zombie.EntityZombieNurse;

public class ZombieNurseData extends ZombieDataBase<EntityZombieNurse> {

    private final ZombieNurseController controller = new ZombieNurseController();

    public ZombieNurseData(EntityZombieNurse entity) {
        super(entity);
    }

    @Override
    public ZombieNurseController getController() {
        return this.controller;
    }

    @Override
    public void initModelPose() {
        super.initModelPose();

        this.body = new ModelPartTransform();
        this.head = new ModelPartTransform(this.body);
        this.rightArm = new ModelPartTransform(this.body);
        this.leftArm = new ModelPartTransform(this.body);
        this.rightLeg = new ModelPartTransform();
        this.leftLeg = new ModelPartTransform();
        this.rightForeArm = new ModelPartTransform(this.rightArm);
        this.leftForeArm = new ModelPartTransform(this.leftArm);
        this.rightForeLeg = new ModelPartTransform(this.rightLeg);
        this.leftForeLeg = new ModelPartTransform(this.leftLeg);
        this.renderRightItemRotation = new SmoothOrientation();
        this.renderLeftItemRotation = new SmoothOrientation();

        this.swordTrail = new SwordTrail(null);

        this.nameToPartMap.put("body", body);
        this.nameToPartMap.put("head", head);
        this.nameToPartMap.put("leftArm", leftArm);
        this.nameToPartMap.put("rightArm", rightArm);
        this.nameToPartMap.put("leftLeg", leftLeg);
        this.nameToPartMap.put("rightLeg", rightLeg);
        this.nameToPartMap.put("leftForeArm", leftForeArm);
        this.nameToPartMap.put("rightForeArm", rightForeArm);
        this.nameToPartMap.put("leftForeLeg", leftForeLeg);
        this.nameToPartMap.put("rightForeLeg", rightForeLeg);
        this.nameToPartMap.put("renderRightItemRotation", renderRightItemRotation);
        this.nameToPartMap.put("renderLeftItemRotation", renderLeftItemRotation);

        this.body.position.set(0F, 12F, 0F);
        this.head.position.set(0F, -12F, 0F);
        this.rightArm.position.set(-5F, -10F, 0F);
        this.leftArm.position.set(5F, -10f, 0f);
        this.rightLeg.position.set(0F, 12.0F, 0.0F);
        this.leftLeg.position.set(0F, 12.0F, 0.0F);
        this.rightForeArm.position.set(0F, 4F, 2F);
        this.leftForeArm.position.set(0F, 4F, 2F);
        this.leftForeLeg.position.set(0, 6.0F, -2.0F);
        this.rightForeLeg.position.set(0, 6.0F, -2.0F);
    }

    @Override
    public void onTicksRestart() {
        // No behaviour
    }

}
