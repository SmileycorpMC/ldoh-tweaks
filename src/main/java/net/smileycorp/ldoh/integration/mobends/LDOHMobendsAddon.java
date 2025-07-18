package net.smileycorp.ldoh.integration.mobends;

import goblinbob.mobends.core.addon.AddonAnimationRegistry;
import goblinbob.mobends.core.addon.AddonHelper;
import goblinbob.mobends.core.addon.IAddon;
import goblinbob.mobends.standard.client.renderer.entity.mutated.ZombieRenderer;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.entity.zombie.EntityReaver;
import net.smileycorp.ldoh.common.entity.zombie.EntityZombieFireman;
import net.smileycorp.ldoh.common.entity.zombie.EntityZombieNurse;
import net.smileycorp.ldoh.integration.mobends.fireman.ZombieFiremanData;
import net.smileycorp.ldoh.integration.mobends.fireman.ZombieFiremanMutator;
import net.smileycorp.ldoh.integration.mobends.nurse.ZombieNurseData;
import net.smileycorp.ldoh.integration.mobends.nurse.ZombieNurseMutator;
import net.smileycorp.ldoh.integration.mobends.reaver.ReaverData;
import net.smileycorp.ldoh.integration.mobends.reaver.ReaverMutator;

public class LDOHMobendsAddon implements IAddon {

    public void register() {
        AddonHelper.registerAddon(Constants.MODID, this);
    }

    @Override
    public String getDisplayName() {
        return Constants.NAME;
    }

    @Override
    public void registerContent(AddonAnimationRegistry registry) {
        registry.registerNewEntity(EntityZombieNurse.class, ZombieNurseData::new, ZombieNurseMutator::new, new ZombieRenderer(),
                "head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm",
                "leftLeg", "rightLeg", "leftForeLeg", "rightForeLeg");
        registry.registerNewEntity(EntityZombieFireman.class, ZombieFiremanData::new, ZombieFiremanMutator::new, new ZombieRenderer(),
                "head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm",
                "leftLeg", "rightLeg", "leftForeLeg", "rightForeLeg");
        registry.registerNewEntity(EntityReaver.class, ReaverData::new, ReaverMutator::new, new ZombieRenderer(),
                "head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm",
                "leftLeg", "rightLeg", "leftForeLeg", "rightForeLeg");
    }

}
