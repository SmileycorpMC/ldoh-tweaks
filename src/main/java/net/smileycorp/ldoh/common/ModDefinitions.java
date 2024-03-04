package net.smileycorp.ldoh.common;

import net.minecraft.util.ResourceLocation;

public class ModDefinitions {

	//mod constants
	public static final String MODID = "hundreddayz";
	public static final String NAME = "LDoH Tweaks";
	public static final String VERSION = "1.1.10";
	public static final String DEPENDENCIES = "after:tektopia;required-after:atlaslib@1.1.5;required-after:rafradek_tf2_weapons;required-after:hordes@1.1.4;required-after:srparasites;"
			+ "required-after:mod_lavacow;required-after:firstaid;required-after:animania;required-after:wastelands;required-after:biomesoplenty;"
			+ "required-after:cgm;required-after:cfm;required-after:realistictorches;required-after:xlfoodmod;required-after:cookingforblockheads;"
			+ "required-after:bibliocraft;required-after:car;required-after:buildcraftcore;after:biometweaker;required-after:animania;required-after:followme;required-after:gamestages;"
			+ "required-after:reccomplex;required-before:iguanatweaks;before:vanillafix";
	public static final String LOCATION = "net.smileycorp.ldoh.";
	public static final String CLIENT = LOCATION + "client.ClientProxy";
	public static final String COMMON = LOCATION + "common.CommonProxy";

	//translation keys
	public static final String JOIN_TEAM_MESSAGE = "message.hundreddayz.JoinTeam";
	public static final String POST_JOIN_TEAM_MESSAGE = "message.hundreddayz.PostJoinTeam";
	public static final String OTHER_JOIN_TEAM_MESSAGE = "message.hundreddayz.OtherJoinTeam";
	public static final String DAY_COUNT_MESSAGE = "message.hundreddayz.DayCount";
	public static final String DAY_100_MESSAGE = "message.hundreddayz.FinalDay";
	public static final String APOCALYPSE_MESSAGE = "message.hundreddayz.WorldsEnd";
	public static final String GAS_MESSAGE = "message.hundreddayz.Gas";
	public static final String LAVA_PICKUP_MESSAGE = "message.hundreddayz.Lava.Pickup";
	public static final String LAVA_BREAK_MESSAGE = "message.hundreddayz.Lava.Break";
	public static final String ZOMBIE_EVOLUTION_MESSAGE_0 = "message.hundreddayz.Evolution_0";
	public static final String ZOMBIE_EVOLUTION_MESSAGE_1 = "message.hundreddayz.Evolution_1";
	public static final String VILLAGER_MESSAGE = "message.hundreddayz.VillagerMessage";

	//sounds
	public static final ResourceLocation TF_ENEMY_SOUND = getResource("tf_enemy");
	public static final ResourceLocation TF_ALLY_SOUND = getResource("tf_ally");
	public static final ResourceLocation LANDMINE_BEEP = getResource("landmine_beep");

	//loot tables
	public static final ResourceLocation SAFEHOUSE_CHEST = getResource("chests/safehouse_chest");
	public static final ResourceLocation SAFEHOUSE_CABINET = getResource("chests/safehouse_cabinet");
	public static final ResourceLocation SAFEHOUSE_FRIDGE = getResource("chests/safehouse_fridge");
	public static final ResourceLocation SAFEHOUSE_MEDICAL_FRIDGE = getResource("chests/safehouse_medical_fridge");
	public static final ResourceLocation SAFEHOUSE_CRATE = getResource("chests/safehouse_crate");
	public static final ResourceLocation NEST_CRATE = getResource("chests/nest_crate");
	public static final ResourceLocation MILITARY_CRATE = getResource("chests/military_crate");
	public static final ResourceLocation MILITARY_AMMO = getResource("chests/military_ammo");
	public static final ResourceLocation MILITARY_TREASURE = getResource("chests/military_treasure");
    public static final ResourceLocation TECH_CRATE = getResource("chests/tech_crate");
    public static final ResourceLocation EIFFEL_GOOD_GOOD = getResource("chests/eiffel_good_good");

    //helper methods
	public static String getName(String name) {
		return MODID + "." + name.replace("_", "");
	}

	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(MODID, name.toLowerCase());
	}

}
