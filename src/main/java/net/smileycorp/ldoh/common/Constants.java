package net.smileycorp.ldoh.common;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class Constants {

    //mod constants
    public static final String MODID = "ldoh";
    public static final String NAME = "LDoH";
    public static final String DEPENDENCIES = "after:tektopia;required-after:atlaslib@1.1.5;required-after:rafradek_tf2_weapons;required-after:hordes@1.1.4;required-after:srparasites;"
            + "required-after:mod_lavacow;required-after:firstaid;required-after:animania;required-after:wastelands;required-after:biomesoplenty;"
            + "required-after:cgm;required-after:cfm;required-after:realistictorches;required-after:xlfoodmod;required-after:cookingforblockheads;"
            + "required-after:bibliocraft;required-after:car;required-after:buildcraftcore;after:biometweaker;required-after:animania;required-after:followme;required-after:gamestages;"
            + "required-after:reccomplex;required-before:iguanatweaks;before:vanillafix;required-before:mutantbeasts";
    public static final String LOCATION = "net.smileycorp.ldoh.";
    public static final String CLIENT = LOCATION + "client.ClientProxy";
    public static final String COMMON = LOCATION + "common.CommonProxy";

    //translation keys
    public static final String JOIN_TEAM_MESSAGE = "message.ldoh.JoinTeam";
    public static final String POST_JOIN_TEAM_MESSAGE = "message.ldoh.PostJoinTeam";
    public static final String OTHER_JOIN_TEAM_MESSAGE = "message.ldoh.OtherJoinTeam";
    public static final String GAS_MESSAGE = "message.ldoh.Gas";
    public static final String ZOMBIE_EVOLUTION_MESSAGE_0 = "message.ldoh.Evolution_0";
    public static final String ZOMBIE_EVOLUTION_MESSAGE_1 = "message.ldoh.Evolution_1";
    public static final String VILLAGER_MESSAGE = "message.ldoh.VillagerMessage";

    //sounds
    public static final ResourceLocation TF_ENEMY_SOUND = loc("tf_enemy");
    public static final ResourceLocation TF_ALLY_SOUND = loc("tf_ally");
    public static final ResourceLocation LANDMINE_BEEP = loc("landmine_beep");
    public static final ResourceLocation JUGG_ALARM = loc("jugg_alarm");

    //loot tables
    public static final ResourceLocation SAFEHOUSE_CHEST = loc("chests/safehouse_chest");
    public static final ResourceLocation SAFEHOUSE_CABINET = loc("chests/safehouse_cabinet");
    public static final ResourceLocation SAFEHOUSE_FRIDGE = loc("chests/safehouse_fridge");
    public static final ResourceLocation SAFEHOUSE_MEDICAL_FRIDGE = loc("chests/safehouse_medical_fridge");
    public static final ResourceLocation SAFEHOUSE_CRATE = loc("chests/safehouse_crate");
    public static final ResourceLocation NEST_CRATE = loc("chests/nest_crate");
    public static final ResourceLocation MILITARY_CRATE = loc("chests/military_crate");
    public static final ResourceLocation MILITARY_AMMO = loc("chests/military_ammo");
    public static final ResourceLocation MILITARY_TREASURE = loc("chests/military_treasure");
    public static final ResourceLocation TECH_CRATE = loc("chests/tech_crate");
    public static final ResourceLocation EIFFEL_GOOD_GOOD = loc("chests/eiffel_good_good");
    public static final ResourceLocation VET_CHEST = loc("chests/vet_chest");
    public static final int BOTTLE_VOLUME = Fluid.BUCKET_VOLUME / 8;
    
    //helper methods
    public static String name(String name) {
        return MODID + "." + name.replace("_", "");
    }

    public static ResourceLocation loc(String name) {
        return new ResourceLocation(MODID, name.toLowerCase());
    }

    public static String locName(String name) {
        return loc(name).toString();
    }
}
