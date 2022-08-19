package net.smileycorp.ldoh.common.world.gen;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiomeEdge;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.smileycorp.ldoh.common.util.EnumBiomeType;
import net.smileycorp.ldoh.common.world.gen.layer.GenLayerLDOHRareBiome;
import net.smileycorp.ldoh.common.world.gen.layer.GenLayerLDOHWasteland;

public class WorldTypeLDOH extends WorldType {

	public WorldTypeLDOH() {
		super("ldoh_wasteland");
	}

	@Override
	public BiomeProvider getBiomeProvider(World world) {
		return new BiomeProviderLDOH(world);
	}

	@Override
	public IChunkGenerator getChunkGenerator(World world, String options) {
		ChunkGeneratorSettings.Factory factory = new ChunkGeneratorSettings.Factory();
		factory.biomeSize = 3;
		return new ChunkGeneratorLDOH(world, world.getSeed(), factory.toString());
	}


	@Override
	public GenLayer getBiomeLayer(long worldSeed, GenLayer parent, ChunkGeneratorSettings chunkSettings) {
		GenLayer layer = new GenLayerLDOHWasteland(200L, parent);
		layer = new GenLayerLDOHRareBiome(1000L, parent, layer, 3, EnumBiomeType.DESERT);
		layer = new GenLayerLDOHRareBiome(1001L, parent, layer, 5, EnumBiomeType.OCEAN);
		layer = new GenLayerLDOHRareBiome(1002L, parent, layer, 7, EnumBiomeType.BADLANDS);
		//layer = new GenLayerLDOHRareBiome(1003L, parent, layer, 14, 1, EnumBiomeType.CITY);
		GenLayer ret3 = new GenLayerBiomeEdge(1000L, layer);
		ret3 = GenLayerZoom.magnify(1000L, ret3, 7);
		return ret3;
	}

}
