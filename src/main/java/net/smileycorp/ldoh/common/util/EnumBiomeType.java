package net.smileycorp.ldoh.common.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.smileycorp.ldoh.common.world.gen.LDOHWorld;
import biomesoplenty.api.biome.BOPBiomes;

import com.google.common.base.Optional;
import com.legacy.wasteland.world.WastelandWorld;

@SuppressWarnings("unchecked")
public enum EnumBiomeType {

	WASTELAND(Optional.of(WastelandWorld.apocalypse), Optional.of(LDOHWorld.SILTY_WASTELAND), Optional.of(LDOHWorld.LOAMY_WASTELAND), Optional.of(LDOHWorld.SANDY_WASTELAND), Optional.of(LDOHWorld.MUDDY_WASTELAND)),
	DESERT(Optional.of(WastelandWorld.apocalypse_desert)),
	BADLANDS(BOPBiomes.wasteland, Optional.of(LDOHWorld.FROZEN_WASTELAND), Optional.of(LDOHWorld.INFERNAL_WASTELAND)),
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

	public List<Biome> getBiomes() {
		List<Biome> biomes = new ArrayList<Biome>();
		for (Optional<Biome> optional : this.biomes) if (optional.isPresent()) biomes.add(optional.get());
		return biomes;
	}

}
