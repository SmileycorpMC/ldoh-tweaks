package net.smileycorp.ldoh.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.smileycorp.atlas.api.entity.ai.EntityAIGoToEntityPos;
import net.smileycorp.ldoh.common.util.ModUtils;

public class EntityMiniRaidAI extends EntityAIGoToEntityPos {

	public EntityMiniRaidAI(EntityLiving entity, Entity target) {
		super(entity, target);
	}

	@Override
	public boolean shouldContinueExecuting() {
		boolean result = super.shouldContinueExecuting() && entity.getDistance(getTarget())>=10;
		if (!result) ModUtils.DELAYED_THREAD_EXECUTOR.execute(()->entity.tasks.removeTask(this));
		return result;
	}

}
