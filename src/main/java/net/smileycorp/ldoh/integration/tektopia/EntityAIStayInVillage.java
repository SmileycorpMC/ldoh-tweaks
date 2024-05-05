package net.smileycorp.ldoh.integration.tektopia;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.smileycorp.atlas.api.entity.ai.EntityAIGoToPos;
import net.smileycorp.atlas.api.util.DirectionUtils;

public class EntityAIStayInVillage extends EntityAIGoToPos {

    public EntityAIStayInVillage(EntityLiving entity) {
        super(entity, entity.getPosition());
    }

    @Override
    public boolean shouldExecute() {
        if (!entity.hasCapability(TektopiaUtils.VILLAGE_DATA, null)) return false;
        if (TektopiaUtils.isTooFarFromVillage(entity, entity.world)) {
            BlockPos center = entity.getCapability(TektopiaUtils.VILLAGE_DATA, null).getVillage().getCenter();
            Vec3d dir = DirectionUtils.getDirectionVec(entity.getPositionVector(), new Vec3d(center));
            pos = DirectionUtils.getClosestLoadedPos(entity.world, entity.getPosition(), dir, 25);
            return true;
        }
        return false;
    }

}
