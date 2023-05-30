package net.smileycorp.ldoh.common.util;

public enum TurretUpgrade {

    //TODO: texture turret upgrades
    BLANK("blank", false),
    HOPPING("hopping", false),
    AMMO_OPTIMIZATION("ammo_optimization", false),

    BARREL_SPIN("barrel_spin", true), //TODO: add BARREL_SPIN functionality
    RANGE_EXTENSION("range_extension", true), //TODO: add RANGE_EXTENSION functionality
    REDSTONE_CONTROL("redstone_control", false); //TODO: add REDSTONE_CONTROL functionality

    //TODO: add more upgrades if time and ideas

    private final String name;
    private final boolean isStackable;

   TurretUpgrade(String name, boolean isStackable) {
        this.isStackable = isStackable;
       this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isStackable() {
       return isStackable;
    }

    public static boolean isBlank(int id) {
        return id <= 0 || id >= values().length;
    }

    public static TurretUpgrade get(int id) {
       return (isBlank(id)) ? BLANK : values()[id];
    }

}
