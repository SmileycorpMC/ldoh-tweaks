package net.smileycorp.ldoh.common;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
    static Configuration config;
    public static boolean noSafehouse;
    public static boolean betaSpawnpoint;

    public static boolean legacyApocalypse;

    //load config properties
    public static void syncConfig() {
        try {
            config.load();
            legacyApocalypse = config.get("Legacy Difficulty", "legacyApocalypse", false, "Should the day 100 boss event use the pre 0.5.0 wave based system instead of the single boss and time pausing of 0.5.0?").getBoolean();
            noSafehouse = config.get("Legacy Difficulty", "noSafehouse", false, "Should the safehouse be blocked from spawning?").getBoolean();
            betaSpawnpoint = config.get("Legacy Difficulty", "betaSpawnpoint", false, "Should the safe biome detection be disabled like the beta?").getBoolean();
        } catch (Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }

    }

}
