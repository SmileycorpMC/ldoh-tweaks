package net.smileycorp.hundreddayz.common;

import java.util.Random;

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

import com.legacy.wasteland.world.WastelandWorld;

public class ModWorldGen implements IWorldGenerator {

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		int x = chunkX * 16 +rand.nextInt(16);
		int z = chunkZ * 16 + rand.nextInt(16);
		if (rand.nextInt(15)==0) {
			BlockPos pos = new BlockPos(x, world.getHeight(x, x)+1, z);
			world.setBlockState(pos, ModContent.HORDE_SPAWNER.getDefaultState());
		}
		BlockPos chunkpos = new BlockPos(x, 0 , z);
		Biome biome = world.getBiome(chunkpos);
		if (biome != WastelandWorld.apocalypse_desert) {
			genOre(world, chunkpos, rand, Blocks.COAL_ORE);
			genOre(world, chunkpos, rand, Blocks.IRON_ORE);
			genOre(world, chunkpos, rand, Blocks.GOLD_ORE);
		}
	}
	
	protected void genOre(World world, BlockPos chunkpos, Random rand, Block block){
		ChunkGeneratorSettings.Factory factory = new ChunkGeneratorSettings.Factory();
		factory.coalMaxHeight = 30;
		factory.ironMaxHeight = 30;
		factory.goldMaxHeight = 30;
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
