package net.smileycorp.ldoh.common.world.gen.layer;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.smileycorp.ldoh.common.util.EnumBiomeType;

public class GenLayerLDOHWasteland extends GenLayer {

	public GenLayerLDOHWasteland(long seed, GenLayer parent) {
		super(seed);
		this.parent = parent;
	}

	@Override
	public int[] getInts(int areaX, int areaZ, int areaWidth, int areaHeight)
	{
		int[] base = parent.getInts(areaX, areaZ, areaWidth, areaHeight);
		int[] aint = IntCache.getIntCache(areaWidth * areaHeight);

		for (int i = 0; i < areaWidth; ++i) {
			for (int k = 0; k < areaHeight; ++k) {
				aint[i + k * areaWidth] = getBiome(i, k, aint[i + k * areaWidth]);
			}
		}

		return aint;
	}

	private int getBiome(int x, int z, int seed) {
		int rX = (x + 4) >> 3;
			int rZ = (z + 4) >> 3;
			initChunkSeed(rX, rZ);

			int size = EnumBiomeType.WASTELAND.getBiomes().size();
			return Biome.getIdForBiome(EnumBiomeType.WASTELAND.getBiomes().get(seed % size));
	}

}
