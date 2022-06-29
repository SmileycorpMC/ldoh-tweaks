package net.smileycorp.ldoh.common.world.gen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.smileycorp.ldoh.common.util.EnumBiomeType;

public class BiomeProviderLDOH extends BiomeProvider {

	public BiomeProviderLDOH(World world) {
		super();
		long seed = world.getSeed();

		GenLayer genlayer = new GenLayerIsland(1L);
		genlayer = new GenLayerFuzzyZoom(2000L, genlayer);
		GenLayer genlayeraddisland = new GenLayerAddIsland(1L, genlayer);
		GenLayer genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
		GenLayer genlayeraddisland1 = new GenLayerAddIsland(2L, genlayerzoom);
		genlayeraddisland1 = new GenLayerAddIsland(50L, genlayeraddisland1);
		genlayeraddisland1 = new GenLayerAddIsland(70L, genlayeraddisland1);
		GenLayer genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland1);
		GenLayer genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
		GenLayer genlayeraddisland2 = new GenLayerAddIsland(3L, genlayeraddsnow);
		GenLayer genlayeredge = new GenLayerEdge(2L, genlayeraddisland2, GenLayerEdge.Mode.COOL_WARM);
		genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
		genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
		GenLayer genlayerzoom1 = new GenLayerZoom(2002L, genlayeredge);
		genlayerzoom1 = new GenLayerZoom(2003L, genlayerzoom1);

		GenLayer genlayerLDOH = LDOHWorld.LDOH_WASTELAND.getBiomeLayer(seed, genlayerzoom1, new ChunkGeneratorSettings.Factory().build());

		//GenLayer genlayersmooth1 = new GenLayerSmooth(1000L, genlayerLDOH);
		//GenLayer genlayer3 = new GenLayerVoronoiZoom(10L, genlayersmooth1);
		//genlayer3.initWorldGenSeed(seed);

		genBiomes = genlayerLDOH;
		biomeIndexLayer = genlayerzoom1;
	}

	@Override
	public List<Biome> getBiomesToSpawnIn() {
		List<Biome> biomes = new ArrayList<Biome>();
		for (EnumBiomeType type : EnumBiomeType.values()) {
			biomes.addAll(type.getBiomes());
		}
		return biomes;
	}

}
