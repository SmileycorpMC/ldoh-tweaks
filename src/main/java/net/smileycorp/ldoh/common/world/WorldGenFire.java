package net.smileycorp.ldoh.common.world;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenFire extends WorldGenerator {

	public WorldGenFire() {
		super(true);
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		Biome biome = world.getBiomeProvider().getBiomes(null, pos.getX(), pos.getZ(), 1, 1, false)[0];
		if (world.getBlockState(pos.down()) != biome.topBlock) return false;
		world.setBlockState(pos.down(), Blocks.NETHERRACK.getDefaultState());
		world.setBlockState(pos, Blocks.FIRE.getDefaultState());
		return true;
	}
}
