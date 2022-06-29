package net.smileycorp.ldoh.common.world.gen.biome;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.world.WorldGenFire;
import biomesoplenty.common.biome.BOPBiome.PropsBuilder;

import com.legacy.wasteland.config.WastelandConfig;
import com.legacy.wasteland.world.biome.decorations.gen.WorldGenRandomRubble;
import com.legacy.wasteland.world.biome.decorations.gen.WorldGenWastelandBigTree;

public class BiomeLDOHDesert extends Biome {

	protected final WorldGenFire fire = new WorldGenFire();
	public WorldGenerator randomRubbleGen = new WorldGenRandomRubble();
	public WorldGenerator deadTreeGen = new WorldGenWastelandBigTree(true);
	private final int grassColour;

	public BiomeLDOHDesert(String name, int grassColour, IBlockState top) {
		super(buildProperties(name.replace("_", " ")));
		topBlock = top;
		fillerBlock = Blocks.STONE.getDefaultState();
		decorator.treesPerChunk = -999;
		decorator.flowersPerChunk = -999;
		decorator.grassPerChunk = -999;
		decorator.gravelPatchesPerChunk = -999;
		decorator.sandPatchesPerChunk = -999;

		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(EntityZombie.class, 100, 4, 5));

		setRegistryName(ModDefinitions.getResource(name.toLowerCase()));
		this.grassColour = grassColour;
	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {
		if(rand.nextInt(WastelandConfig.worldgen.wastelandTreeSpawnRate * 15) == 0) {
			deadTreeGen.generate(world, rand, world.getHeight(pos.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8)));
		}

		if(rand.nextInt(WastelandConfig.worldgen.wastelandRuinRarirty) == 0) {
			randomRubbleGen.generate(world, rand, world.getHeight(pos.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8)));
		}


		for(int size = 0; size < WastelandConfig.worldgen.randomFirePerChunk; ++size) {
			fire.generate(world, rand, world.getHeight(pos.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8)));
		}
		super.decorate(world, rand, pos);
	}

	@Override
	public int getGrassColorAtPos(BlockPos pos) {
		return grassColour;
	}

	private static BiomeProperties buildProperties(String name) {
		PropsBuilder builder = new PropsBuilder(name).withTemperature(0.8F).withBaseHeight(0.1F).withHeightVariation(0.05F).withRainDisabled();
		return builder.build();
	}
}
