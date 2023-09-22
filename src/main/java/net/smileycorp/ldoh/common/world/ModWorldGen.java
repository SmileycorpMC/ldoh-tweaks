package net.smileycorp.ldoh.common.world;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.common.block.BlockBOPDirt;
import biomesoplenty.common.block.BlockBOPDirt.BOPDirtType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.smileycorp.ldoh.common.block.LDOHBlocks;
import net.smileycorp.ldoh.common.tile.TileHordeSpawner;
import net.smileycorp.ldoh.common.util.EnumBiomeType;
import net.smileycorp.ldoh.common.world.WorldGenSurface.EnumVariant;
import rafradek.TF2weapons.TF2weapons;

import java.util.Random;

public class ModWorldGen implements IWorldGenerator {

	@SuppressWarnings("unchecked")
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.getDimensionType() != DimensionType.OVERWORLD) return;
		//generate horde spawning blocks
		int x = (chunkX << 4) +rand.nextInt(16);
		int z = (chunkZ << 4) + rand.nextInt(16);
		Biome biome = world.getBiomeProvider().getBiomes(null, x, z, 1, 1, false)[0];
		if (rand.nextInt(EnumBiomeType.CITY.matches(biome) ? 7 : EnumBiomeType.BADLANDS.matches(biome) ? 13
				: EnumBiomeType.OCEAN.matches(biome) ? 24 : 18) == 0) {
			BlockPos pos = new BlockPos(x, Math.min(world.getHeight(x, z), 60), z);
			while (!world.isAirBlock(pos)) pos = pos.up();
			world.setBlockState(pos, LDOHBlocks.HORDE_SPAWNER.getDefaultState(), 18);
			((TileHordeSpawner)world.getTileEntity(pos)).setNatural();
		}
		BlockPos chunkpos = new BlockPos(x, 0 , z);
		//adds our custom oregen to biomes other than the apocalyptic desert
		if (!EnumBiomeType.DESERT.matches(biome)) {
			genOre(world, chunkpos, rand, Blocks.COAL_ORE);
			genOre(world, chunkpos, rand, Blocks.IRON_ORE);
			genOre(world, chunkpos, rand, Blocks.GOLD_ORE);
			genSpecialOre(world, chunkpos, rand, TF2weapons.blockCopperOre, 3, 28, 10, 7);
			genSpecialOre(world, chunkpos, rand, TF2weapons.blockLeadOre, 3, 28, 6, 2);
		} else {
			genSpecialOre(world, chunkpos, rand, TF2weapons.blockCopperOre, 3, 65, 20, 7);
			genSpecialOre(world, chunkpos, rand, TF2weapons.blockLeadOre, 3, 36, 8, 2);
		}
		if (EnumBiomeType.OCEAN.matches(biome) && rand.nextInt(15) == 0) {
			EnumVariant variant = EnumVariant.values()[new Random().nextInt(EnumVariant.values().length)];
			genSurfaceBlock(world, rand, chunkX, chunkZ, variant.state1, variant.state2);
		} else if (EnumBiomeType.BADLANDS.matches(biome) && rand.nextInt(25) == 0) {
			genSurfaceBlock(world, rand, chunkX, chunkZ, Blocks.SOUL_SAND.getDefaultState(), BOPBlocks.dirt.getDefaultState()
					.withProperty(BlockBOPDirt.VARIANT, BOPDirtType.LOAMY).withProperty(BlockBOPDirt.COARSE, true));
		}
		if (EnumBiomeType.BADLANDS.matches(biome)) genNest(world, rand, chunkX, chunkZ);
	}

	private void genSpecialOre(World world, BlockPos chunkpos, Random rand, Block block, int minHeight, int maxHeight, int count, int size) {
		WorldGenerator generator = new WorldGenMinable(block.getDefaultState(), size);
		int hdelta = maxHeight - minHeight;
		for (int j = 0; j < count; ++j) {
			BlockPos blockpos = chunkpos.add(rand.nextInt(16), minHeight + rand.nextInt(hdelta), rand.nextInt(16));
			generator.generate(world, rand, blockpos);
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

	private void genSurfaceBlock(World world, Random rand, int chunkX, int chunkZ, IBlockState state1, IBlockState state2) {
		int x = (chunkX << 4) +rand.nextInt(16);
		int z = (chunkZ << 4) + rand.nextInt(16);
		WorldGenSurface gen = new WorldGenSurface(state1, state2);
		gen.generate(world, rand, new BlockPos(x, world.getChunkFromChunkCoords(chunkX, chunkZ).getHeightValue(x&15, z&15)-1, z));
	}

	private void genNest(World world, Random rand, int chunkX, int chunkZ) {
		if (rand.nextInt(255) == 0) {
			int x = (chunkX << 4) +rand.nextInt(16);
			int z = (chunkZ << 4) + rand.nextInt(16);
			new WorldGenNest().generate(world, rand, new BlockPos(x, rand.nextInt(10)+ 10, z));
		}
	}

}
