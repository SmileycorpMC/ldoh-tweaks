package net.smileycorp.ldoh.common.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.smileycorp.atlas.api.entity.ai.EntityAIGoToEntityPos;
import net.smileycorp.ldoh.common.util.ModUtils;

import java.util.concurrent.TimeUnit;

public class AIMiniRaid extends EntityAIGoToEntityPos {

	public AIMiniRaid(EntityLiving entity, Entity target) {
		super(entity, target);
	}

	@Override
	public boolean shouldContinueExecuting() {
		boolean result = super.shouldContinueExecuting() && entity.getDistance(getTarget())>=10;
		if (!result) ModUtils.DELAYED_THREAD_EXECUTOR.schedule(()->entity.tasks.removeTask(this), 20, TimeUnit.MILLISECONDS);
		return result;
	}

}
