package net.smileycorp.ldoh.common;

import net.minecraft.util.ResourceLocation;

public class ModDefinitions {
	
	public static final String modid = "hundreddayz";
	public static final String name = "Last Days of Humanity Tweaks";
	public static final String version = "alpha 1.1.3";
	public static final String dependencies = "required-after:tektopia;required-after:atlaslib;required-after:rafradek_tf2_weapons;required-after:hordes;required-after:srparasites;"
			+ "required-after:mod_lavacow;required-after:firstaid;required-after:animania;required-after:wastelands;required-after:biomesoplenty;"
			+ "required-after:cgm;required-after:cfm;required-after:realistictorches;required-after:xlfoodmod;required-after:cookingforblockheads;"
			+ "required-after:bibliocraft;required-after:car;required-after:buildcraftcore;after:biometweaker";
	public static final String location = "net.smileycorp.ldoh.";
	public static final String client = location + "client.ClientProxy";
	public static final String common = location + "common.CommonProxy";
	
	public static final String joinTeamMessage = "message.hundreddayz.JoinTeam";
	public static final String postJoinTeamMessage = "message.hundreddayz.PostJoinTeam";
	public static final String otherJoinTeamMessage = "message.hundreddayz.OtherJoinTeam";
	public static final String dayCountMessage = "message.hundreddayz.DayCount";
	public static final String finalDayMessage = "message.hundreddayz.FinalDay";
	public static final String worldsEndMessage = "message.hundreddayz.WorldsEnd";
	public static final String gasMessage = "message.hundreddayz.Gas";
	public static final String lavaPickupMessage = "message.hundreddayz.Lava.Pickup";
	public static final String lavaBreakMessage = "message.hundreddayz.Lava.Break";
	
	public static final ResourceLocation SAFEHOUSE_CHEST = getResource("chests/safehouse_chest");
	public static final ResourceLocation SAFEHOUSE_CABINET = getResource("chests/safehouse_cabinet");
	public static final ResourceLocation SAFEHOUSE_FRIDGE = getResource("chests/safehouse_fridge");
	
	public static String getName(String name) {
		return modid + "." + name.replace("_", "");
	}
	
	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(modid, name.toLowerCase());
	}
	
}
