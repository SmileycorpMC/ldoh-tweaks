package net.smileycorp.ldoh.common.world;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenSurface extends WorldGenerator {

	protected final IBlockState oreBlock;
	/** The number of blocks to generate. */
	protected final int numberOfBlocks;

	private Set<BlockPos> positions = new HashSet<BlockPos>();

	public WorldGenSurface(Block block, int blockCount) {
		oreBlock = block.getDefaultState();
		numberOfBlocks = blockCount;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos center) {
		float f = rand.nextFloat() * (float)Math.PI;
		double d0 = center.getX() + 8 + MathHelper.sin(f) * numberOfBlocks / 8.0F;
		double d1 = center.getX() + 8 - MathHelper.sin(f) * numberOfBlocks / 8.0F;
		double d2 = center.getZ() + 8 + MathHelper.cos(f) * numberOfBlocks / 8.0F;
		double d3 = center.getZ() + 8 - MathHelper.cos(f) * numberOfBlocks / 8.0F;

		for (int i = 0; i < numberOfBlocks; ++i)
		{
			float f1 = (float)i / (float)numberOfBlocks;
			double d6 = d0 + (d1 - d0) * f1;
			double d8 = d2 + (d3 - d2) * f1;
			double d9 = rand.nextDouble() * numberOfBlocks / 16.0D;
			double d10 = (MathHelper.sin((float)Math.PI * f1) + 1.0F) * d9 + 1.0D;
			int j = MathHelper.floor(d6 - d10 / 2.0D);
			int l = MathHelper.floor(d8 - d10 / 2.0D);
			int i1 = MathHelper.floor(d6 + d10 / 2.0D);
			int k1 = MathHelper.floor(d8 + d10 / 2.0D);

			for (int l1 = j; l1 <= i1; ++l1)
			{
				double d12 = (l1 + 0.5D - d6) / (d10 / 2.0D);

				if (d12 * d12 < 1.0D)
				{
					if (d12 * d12 < 1.0D)
					{
						for (int j2 = l; j2 <= k1; ++j2)
						{
							double d14 = (j2 + 0.5D - d8) / (d10 / 2.0D);

							if (d12 * d12 + d14 * d14 < 1.0D) {
								positions.add(new BlockPos(l1, world.getHeight(l1, j2)-1, j2));
							}
						}
					}
				}
			}
		}
		for (BlockPos pos : positions) {
			if (world.getBlockState(pos).getBlock() == Blocks.GRAVEL) {
				int r = (int) Math.floor(Math.pow(pos.getX() - center.getX(), 2) + Math.pow(pos.getZ() - center.getZ(), 2));
				if (world.rand.nextInt(Math.max(r, 1))>100)  world.setBlockState(pos, oreBlock, 18);
			}
		}
		return true;
	}

}
