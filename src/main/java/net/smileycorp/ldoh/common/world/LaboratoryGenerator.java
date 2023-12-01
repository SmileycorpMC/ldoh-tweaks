package net.smileycorp.ldoh.common.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.util.DirectionUtils;

import java.util.Random;

public class LaboratoryGenerator {

    public static BlockPos getPosition(World world) {
        Random rand = new Random(world.getSeed());
        Vec3d dir = DirectionUtils.getRandomDirectionVecXZ(rand);
        int distance = 1000 + rand.nextInt(2000);
        return new BlockPos(dir.x * distance, 0, dir.z * distance);
    }

}
