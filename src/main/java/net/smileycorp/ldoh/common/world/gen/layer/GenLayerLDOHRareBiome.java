package net.smileycorp.ldoh.common.world.gen.layer;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.smileycorp.ldoh.common.util.EnumBiomeType;

public class GenLayerLDOHRareBiome extends GenLayer {

	private final GenLayer base;
	private final int chance;
	private final EnumBiomeType type;

	public GenLayerLDOHRareBiome(long seed, GenLayer parent, GenLayer base, int chance, EnumBiomeType type) {
		super(seed);
		this.parent = parent;
		this.base = base;
		this.chance = chance;
		this.type = type;
	}

	@Override
	public int[] getInts(int areaX, int areaZ, int areaWidth, int areaHeight)
	{
		int[] base = this.base.getInts(areaX, areaZ, areaWidth, areaHeight);
		int[] parent = this.parent.getInts(areaX, areaZ, areaWidth, areaHeight);
		int[] aint = IntCache.getIntCache(areaWidth * areaHeight);

		for (int i = 0; i < areaWidth; ++i) {
			for (int k = 0; k < areaHeight; ++k) {
				initChunkSeed(i + areaX, k + areaZ);
				aint[i + k * areaWidth] =  nextInt(chance) == 0 ? getBiome(i, k, aint[i + k * areaWidth] = getBiome(i, k, aint[i + k * areaWidth])) : base[i + k * areaWidth];
			}
		}

		return aint;
	}

	private int getBiome(int x, int z, int seed) {
		int rX = (x + 4) >> 3;
			int rZ = (z + 4) >> 3;
			initChunkSeed(rX, rZ);

			int size = type.getBiomes().size();
			int i = seed;
			return Biome.getIdForBiome(type.getBiomes().get(size % (i % (size*chance)/size)));
	}

}
