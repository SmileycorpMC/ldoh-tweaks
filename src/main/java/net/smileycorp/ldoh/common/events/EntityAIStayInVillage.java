package net.smileycorp.ldoh.common.events;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.smileycorp.atlas.api.entity.ai.EntityAIGoToPos;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.smileycorp.ldoh.common.util.ModUtils;

public class EntityAIStayInVillage extends EntityAIGoToPos {

	public EntityAIStayInVillage(EntityLiving entity) {
		super(entity, entity.getPosition());
	}

	@Override
	public boolean shouldExecute() {
		if (entity.hasCapability(LDOHCapabilities.VILLAGE_DATA, null)) {
			if (ModUtils.isTooFarFromVillage(entity, entity.world)) {
				BlockPos center = entity.getCapability(LDOHCapabilities.VILLAGE_DATA, null).getVillage().getCenter();
				Vec3d dir = DirectionUtils.getDirectionVec(entity.getPositionVector(), new Vec3d(center));
				pos = DirectionUtils.getClosestLoadedPos(entity.world, entity.getPosition(), dir, 10);
				return true;
			}
		}
		return false;
	}

}
