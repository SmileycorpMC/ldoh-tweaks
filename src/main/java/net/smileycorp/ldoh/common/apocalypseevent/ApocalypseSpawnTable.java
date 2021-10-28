package net.smileycorp.ldoh.common.apocalypseevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.smileycorp.atlas.api.recipe.WeightedOutputs;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityBanoAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityCanraAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityEmanaAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityHullAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityNoglaAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityRanracAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityShycoAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.ancient.EntityOronco;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityVenkrolSV;

public class ApocalypseSpawnTable {

	private static WeightedOutputs<Class<? extends EntityParasiteBase>> adaptedtable;

	public static void init() {
		Map<Class<? extends EntityParasiteBase>, Integer> adaptedmap = new HashMap<Class<? extends EntityParasiteBase>, Integer>();
		adaptedmap.put(EntityShycoAdapted.class, 1);
		adaptedmap.put(EntityCanraAdapted.class, 1);
		adaptedmap.put(EntityNoglaAdapted.class, 1);
		adaptedmap.put(EntityHullAdapted.class, 1);
		adaptedmap.put(EntityEmanaAdapted.class, 1);
		adaptedmap.put(EntityBanoAdapted.class, 1);
		adaptedmap.put(EntityRanracAdapted.class, 1);
		adaptedtable = new WeightedOutputs<Class<? extends EntityParasiteBase>>(adaptedmap);
	}

	public static List<Class<? extends EntityParasiteBase>> getSpawnsForWave(int wave, Random rand) {
		List<Class<? extends EntityParasiteBase>> spawnlist = new ArrayList<Class<? extends EntityParasiteBase>>();
		if (wave % 1 == 0) {
			spawnlist.add(EntityOronco.class);
			for (int i = 0; i < 3 + Math.round(wave*0.3); i++) {
				spawnlist.addAll(adaptedtable.getResults(rand));
			}
			if (wave == 6) {
				spawnlist.add(EntityOronco.class);
			}
		}
		else if (wave % 1 == 1) {
			for (int i = 0; i < 3 + Math.round(wave*1.4); i++) {
				spawnlist.add(EntityVenkrolSV.class);
			}
		}
		return spawnlist;
	}
}
