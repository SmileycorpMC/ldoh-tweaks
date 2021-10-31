package net.smileycorp.ldoh.common.world;

import java.util.Random;

import mcjty.lostcities.dimensions.world.LostCityChunkGenerator;
import mcjty.lostcities.dimensions.world.lost.BuildingInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.smileycorp.ldoh.common.ModContent;
import biomesoplenty.api.biome.BOPBiomes;

import com.legacy.wasteland.world.WastelandWorld;

public class ModWorldGen implements IWorldGenerator {

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		//generate horde spawning blocks
		int x = (chunkX << 4) +rand.nextInt(16);
		int z = (chunkZ << 4) + rand.nextInt(16);
		if (rand.nextInt(world.getBiomeProvider().getBiome(new BlockPos(x, 0, z)) == BOPBiomes.wasteland.get() ? 13 : 18)==0) {
			BlockPos pos = new BlockPos(x, world.getHeight(x, z)+1, z);
			world.setBlockState(pos, ModContent.HORDE_SPAWNER.getDefaultState(), 18);
		}
		//give an extra chance to generate in cities
		if (chunkGenerator instanceof LostCityChunkGenerator) {
			if (BuildingInfo.isCity(chunkX, chunkZ, (LostCityChunkGenerator) chunkGenerator)) {
				x = (chunkX << 4) +rand.nextInt(16);
				z = (chunkZ << 4) + rand.nextInt(16);
				if (rand.nextInt(20)==0) {
					BlockPos pos = new BlockPos(x, world.getHeight(x, z)+1, z);
					world.setBlockState(pos, ModContent.HORDE_SPAWNER.getDefaultState(), 18);
				}
			}
		}
		BlockPos chunkpos = new BlockPos(x, 0 , z);
		Biome biome = world.getBiome(chunkpos);
		//adds our custom oregen to biomes other than the apocalyptic desert
		if (biome != WastelandWorld.apocalypse_desert) {
			genOre(world, chunkpos, rand, Blocks.COAL_ORE);
			genOre(world, chunkpos, rand, Blocks.IRON_ORE);
			genOre(world, chunkpos, rand, Blocks.GOLD_ORE);
		}
	}

	protected void genOre(World world, BlockPos chunkpos, Random rand, Block block){
		ChunkGeneratorSettings.Factory factory = new ChunkGeneratorSettings.Factory();
		factory.coalMaxHeight = 28;
		factory.ironMaxHeight = 28;
		factory.goldMaxHeight = 28;
		factory.coalCount = (int) Math.round(factory.coalCount/2.5d);
		factory.ironCount = Math.round(factory.ironCount/2);
		ChunkGeneratorSettings provider = factory.build();
		WorldGenerator generator = null;
		int  count = 0;
		if (block == Blocks.COAL_ORE) {
			generator = new WorldGenMinable(block.getDefaultState(), provider.coalSize);
			count = provider.coalCount;
		} else if (block == Blocks.IRON_ORE) {
			generator = new WorldGenMinable(block.getDefaultState(), provider.ironSize);
			count = provider.ironCount;
		} else if (block == Blocks.GOLD_ORE) {
			generator = new WorldGenMinable(block.getDefaultState(), provider.goldSize);
			count = provider.goldCount;
		}
		for (int j = 0; j < count; ++j) {
			BlockPos blockpos = chunkpos.add(rand.nextInt(16), rand.nextInt(30), rand.nextInt(16));
			generator.generate(world, rand, blockpos);
		}
	}

}
