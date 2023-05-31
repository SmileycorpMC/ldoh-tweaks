package net.smileycorp.ldoh.common.util;

import net.minecraft.item.ItemStack;
import net.smileycorp.ldoh.common.item.LDOHItems;

public enum TurretUpgrade {

    BLANK("blank", false),
    HOPPING("hopping", false),
    AMMO_OPTIMIZATION("ammo_optimization", false),

    BARREL_SPIN("barrel_spin", true),
    RANGE("range", true),
    REDSTONE_CONTROL("redstone_control", false);

    //TODO: add more upgrades if time and ideas (optional)

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

    public ItemStack getItem() {
       return new ItemStack(LDOHItems.TURRET_UPGRADE, 1, ordinal());
    }

    public static boolean isBlank(int id) {
        return id <= 0 || id >= values().length;
    }

    public static TurretUpgrade get(int id) {
       return (isBlank(id)) ? BLANK : values()[id];
    }

}
