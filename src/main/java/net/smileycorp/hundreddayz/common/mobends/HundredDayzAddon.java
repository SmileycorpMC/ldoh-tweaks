package net.smileycorp.hundreddayz.common.mobends;

import goblinbob.mobends.core.addon.AddonAnimationRegistry;
import goblinbob.mobends.core.addon.IAddon;
import goblinbob.mobends.standard.client.renderer.entity.mutated.ZombieRenderer;
import net.smileycorp.hundreddayz.common.ModDefinitions;
import net.smileycorp.hundreddayz.common.entity.EntityZombieNurse;

public class HundredDayzAddon implements IAddon {

	@Override
	public String getDisplayName() {
		return ModDefinitions.name;
	}

	@Override
	public void registerContent(AddonAnimationRegistry registry) {
		registry.registerNewEntity(EntityZombieNurse.class, ZombieNurseData::new, ZombieNurseMutator::new, new ZombieRenderer<EntityZombieNurse>(),
				"head", "body", "leftArm", "rightArm", "leftForeArm", "rightForeArm",
				"leftLeg", "rightLeg", "leftForeLeg", "rightForeLeg");
	}

}
