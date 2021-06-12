package net.smileycorp.hundreddayz.common;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenHorde implements IWorldGenerator {

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (rand.nextInt(15)==0) {
			int x = chunkX * 16 +rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));
			world.setBlockState(pos, ModContent.HORDE_SPAWNER.getDefaultState());
		}
	}

}
