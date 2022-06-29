package net.smileycorp.ldoh.common.world.gen;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGeneratorOverworld;

public class ChunkGeneratorLDOH extends ChunkGeneratorOverworld {

	public ChunkGeneratorLDOH(World worldIn, long seed, String generatorOptions) {
		super(worldIn, seed, false, generatorOptions);
		oceanBlock = Blocks.AIR.getDefaultState();
	}

}
