package net.smileycorp.ldoh.common.entity.ai;

import net.smileycorp.hordes.infection.HordesInfection;
import net.smileycorp.ldoh.common.ModContent;
import net.smileycorp.ldoh.common.capabilities.ICuring;
import rafradek.TF2weapons.entity.ai.EntityAIUseMedigun;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;

public class AIModifiedMedigun extends EntityAIUseMedigun {

	public AIModifiedMedigun(EntityTF2Character entity) {
		super(entity, 1.0F, 20.0F);
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (pressed && entityHost.hasCapability(ModContent.CURING, null) &! entityHost.world.isRemote) {
			ICuring curing = entityHost.getCapability(ModContent.CURING, null);
			if (curing.getSyringeCount() >= 0 && attackTarget.isPotionActive(HordesInfection.INFECTED)) {
				attackTarget.removeActivePotionEffect(HordesInfection.INFECTED);
				curing.setSyringeCount(curing.getSyringeCount()-1);
				curing.syncClients(entityHost);
			}
		}
	}

}
