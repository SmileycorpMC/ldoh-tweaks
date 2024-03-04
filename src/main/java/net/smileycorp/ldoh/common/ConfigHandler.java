package net.smileycorp.ldoh.common;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
	static Configuration config;

	public static boolean legacyDamage;
	public static boolean legacySpawns;
	public static boolean noSafehouse;
	public static boolean noDaySlowdown;
	public static boolean betaSpawnpoint;

	public static boolean legacyApocalypse;

	//load config properties
	public static void syncConfig() {
		try{
			config.load();
			legacyApocalypse = config.get("Legacy Difficulty", "legacyApocalypse", false, "Should the day 100 boss event use the pre 0.5.0 wave based system instead of the single boss and time pausing of 0.5.0?").getBoolean();
			legacyDamage = config.get("Legacy Difficulty", "legacyDamage", false, "Should zombies use the pre 0.4.5 damage, where they always deal 3 damage through armour?").getBoolean();
			legacySpawns = config.get("Legacy Difficulty", "legacySpawns", false, "Should zombies spawn fully evolved, and nurses/firemen not replace spawns?").getBoolean();
			noSafehouse = config.get("Legacy Difficulty", "noSafehouse", false, "Should the safehouse be blocked from spawning?").getBoolean();
			noDaySlowdown = config.get("Legacy Difficulty", "noDaySlowdown", false, "Should the speed reduction during the day be removed?").getBoolean();
			betaSpawnpoint = config.get("Legacy Difficulty", "betaSpawnpoint", false, "Should the safe biome detection be disabled like the beta?").getBoolean();
		} catch(Exception e) {
		} finally {
			if (config.hasChanged()) config.save();
		}

	}

}
