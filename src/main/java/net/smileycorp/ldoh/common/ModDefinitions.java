package net.smileycorp.ldoh.common;

import net.minecraft.util.ResourceLocation;

public class ModDefinitions {

	//mod constants
	public static final String MODID = "hundreddayz";
	public static final String NAME = "Last Days of Humanity Tweaks";
	public static final String VERSIONS = "alpha 1.1.3";
	public static final String DEPENDENCIES = "required-after:tektopia;required-after:atlaslib@1.1.5;required-after:rafradek_tf2_weapons;required-after:hordes@1.1.4;required-after:srparasites;"
			+ "required-after:mod_lavacow;required-after:firstaid;required-after:animania;required-after:wastelands;required-after:biomesoplenty;"
			+ "required-after:cgm;required-after:cfm;required-after:realistictorches;required-after:xlfoodmod;required-after:cookingforblockheads;"
			+ "required-after:bibliocraft;required-after:car;required-after:buildcraftcore;after:biometweaker";
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

	//sounds
	public static final ResourceLocation TF_ENEMY_SOUND = getResource("tf_enemy");
	public static final ResourceLocation TF_ALLY_SOUND = getResource("tf_ally");
	public static final ResourceLocation LANDMINE_BEEP = getResource("landmine_beep");
	public static final ResourceLocation SNORE = getResource("snore");


	//loot tables
	public static final ResourceLocation SAFEHOUSE_CHEST = getResource("chests/safehouse_chest");
	public static final ResourceLocation SAFEHOUSE_CABINET = getResource("chests/safehouse_cabinet");
	public static final ResourceLocation SAFEHOUSE_FRIDGE = getResource("chests/safehouse_fridge");
	public static final ResourceLocation NEST_CRATE = getResource("chests/nest_crate");
	//helper methods
	public static String getName(String name) {
		return MODID + "." + name.replace("_", "");
	}

	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(MODID, name.toLowerCase());
	}

}
