package net.smileycorp.ldoh.common.world.gen.biome;

import java.util.Random;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenIceSpike;
import net.smileycorp.ldoh.common.ModDefinitions;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.common.biome.BOPBiome.PropsBuilder;

public class BiomeFrozenWasteland extends Biome {

	private final WorldGenIceSpike iceSpike = new WorldGenIceSpike();

	public BiomeFrozenWasteland() {
		super(buildProperties());

		topBlock = BOPBlocks.hard_ice.getDefaultState();
		fillerBlock = BOPBlocks.hard_ice.getDefaultState();
		decorator.treesPerChunk = -999;
		decorator.flowersPerChunk = -999;
		decorator.grassPerChunk = -999;
		decorator.gravelPatchesPerChunk = -999;
		decorator.sandPatchesPerChunk = -999;

		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(EntityZombie.class, 100, 4, 5));

		setRegistryName(ModDefinitions.getResource("frozen_wasteland"));
	}


	@Override
	public void decorate(World worldIn, Random rand, BlockPos pos)
	{
		for (int i = 0; i < 3; ++i)
		{
			int j = rand.nextInt(16) + 8;
			int k = rand.nextInt(16) + 8;
			this.iceSpike.generate(worldIn, rand, worldIn.getHeight(pos.add(j, 0, k)));
		}


		super.decorate(worldIn, rand, pos);
	}

	private static BiomeProperties buildProperties() {
		PropsBuilder builder = new PropsBuilder("Frozen Wasteland").withGuiColour(0x19EFFF).withTemperature(-5F).withSnowEnabled();
		return builder.build();
	}
}
