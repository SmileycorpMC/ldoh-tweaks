package net.smileycorp.ldoh.common.world.gen.biome;

import java.util.Random;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.smileycorp.ldoh.common.ModDefinitions;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.common.biome.BOPBiome.PropsBuilder;

import com.legacy.wasteland.config.WastelandConfig;

public class BiomeInfernalWasteland extends Biome {

	public BiomeInfernalWasteland() {
		super(buildProperties());

		topBlock = BOPBlocks.ash_block.getDefaultState();
		fillerBlock = BOPBlocks.ash_block.getDefaultState();
		decorator.treesPerChunk = -999;
		decorator.flowersPerChunk = -999;
		decorator.grassPerChunk = -999;
		decorator.gravelPatchesPerChunk = -999;
		decorator.sandPatchesPerChunk = -999;

		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(EntityZombie.class, 100, 4, 5));

		setRegistryName(ModDefinitions.getResource("infernal_wasteland"));

	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {
		for(int size = 0; size < WastelandConfig.worldgen.randomFirePerChunk; ++size) {
			BlockPos firepos = world.getHeight(pos.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8));
			if (world.getBlockState(firepos.down()) == topBlock && world.isAirBlock(firepos))
				world.setBlockState(firepos, BOPBlocks.blue_fire.getDefaultState(), 18);
		}
		super.decorate(world, rand, pos);
	}

	private static BiomeProperties buildProperties() {
		PropsBuilder builder = new PropsBuilder("Infernal Wasteland").withTemperature(5F).withRainDisabled();
		return builder.build();
	}
}
