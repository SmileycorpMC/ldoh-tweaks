package net.smileycorp.ldoh.common.util;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import biomesoplenty.api.biome.BOPBiomes;

import com.google.common.base.Optional;
import com.legacy.wasteland.world.WastelandWorld;

@SuppressWarnings("unchecked")
public enum EnumBiomeType {

	WASTELAND(Optional.of(WastelandWorld.apocalypse)),
	DESERT(Optional.of(WastelandWorld.apocalypse_desert)),
	BADLANDS(BOPBiomes.wasteland),
	OCEAN(Optional.of(Biomes.DEEP_OCEAN)),
	CITY(Optional.of(WastelandWorld.apocalypse_city));

	private final Optional<Biome>[] biomes;


	private EnumBiomeType(Optional<Biome>... biomes) {
		this.biomes = biomes;
	}

	public boolean matches(Biome biome) {
		for (Optional<Biome> optional : biomes) {
			if (optional.isPresent()) if (optional.get() == biome) return true;
		}
		return false;
	}

}
