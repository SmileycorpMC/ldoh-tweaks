package net.smileycorp.hundreddayz.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class ModDefinitions {
	
	public static final String modid = "hundreddayz";
	public static final String name = "Last Days of Humanity Tweaks";
	public static final String version = "alpha 1.1.2h";
	public static final String dependencies = "required-after:atlaslib;required-after:rafradek_tf2_weapons;required-after:hordes;required-after:srparasites;"
			+ "required-after:mod_lavacow;required-after:firstaid;required-after:animania;required-after:mobends;required-after:wastelands;required-after:biomesoplenty;"
			+ "required-after:cgm;required-after:cfm;required-after:realistictorches;required-after:xlfoodmod;after:biometweaker";
	public static final String location = "net.smileycorp." + modid + ".";
	public static final String client = location + "client.ClientProxy";
	public static final String common = location + "common.CommonProxy";
	
	public static final String joinTeamMessage = "message.hundreddayz.JoinTeam";
	public static final String dayCountMessage = "message.hundreddayz.DayCount";
	public static final String finalDayMessage = "message.hundreddayz.FinalDay";
	public static final String worldsEndMessage = "message.hundreddayz.WorldsEnd";
	public static final String gasMessage = "message.hundreddayz.Gas";
	public static final ResourceLocation SPAWN_CHEST = getResource("chests/spawn_chest");
	public static final ResourceLocation SPAWN_CABINET = getResource("chests/spawn_cabinet");
	
	public static String getName(String name) {
		return modid + "." + name.replace("_", "");
	}
	
	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(modid, name.toLowerCase());
	}
	
	public static void addPlayerToTeam(EntityPlayer player, String team) {
		Scoreboard scoreboard = player.world.getScoreboard();
		scoreboard.addPlayerToTeam(player.getName(), team);
		ITextComponent component = new TextComponentString(team);
		component.setStyle(new Style().setColor(scoreboard.getTeam(team).getColor()));
		ITextComponent message = new TextComponentTranslation(ModDefinitions.joinTeamMessage, new Object[]{component.getFormattedText()});
		
		player.sendMessage(message);
	}
	
}
