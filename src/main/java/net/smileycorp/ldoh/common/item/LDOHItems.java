package net.smileycorp.ldoh.common.item;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public class LDOHItems {

    //items
    public static final Item SPAWNER = new ItemSpawner();
    public static final Item SYRINGE = new ItemSyringe();
    public static final Item CLOTH_FABRIC = new ItemBase("Clothing_Fabric");
    public static final Item DIAMOND_NUGGET = new ItemBase("Diamond_Nugget");
    public static final Item GAS_FILTER = new ItemBase("Gas_Filter").setMaxStackSize(1);
    public static final Item GAS_MASK = new ItemGasMask();
    public static final Item NURSE_HAT = new ItemHat("Nurse_Hat");
    public static final Item MECHANIC_HAT = new ItemHat("Mechanic_Hat");
    public static final Item HARDHAT = new ItemHelmet("Hardhat", 132, 2, 0f);
    public static final Item FIREMAN_HAT = new ItemHelmet("Fire_Hat", 151, 2, 0f);
    public static final Item BONESAW = new ItemWeapon("Bonesaw", 98, 5.5);
    public static final Item FIRE_AXE = new ItemWeapon("Fire_Axe", 186, 9, -3.5F, true);
    public static final Item INCENDIARY_AMMO = new ItemIncendiaryAmmo();

    public static final Item TURRET_UPGRADE = new ItemTurretUpgrade();

    //public static final Item TURRET_TARGETING_UPDRADE = new ItemTurretTargetingUpgrade();

    public static final Item CANDY_CORN = new ItemFoodLDOH("Candy_Corn", 5, 0.6F, false);

    public static final Item EXO_BOOTS = new ItemExosuit("Exo_Boots", EntityEquipmentSlot.FEET);
    public static final Item EXO_LEGS = new ItemExosuit("Exo_Legs", EntityEquipmentSlot.LEGS);
    public static final Item JUMP_KIT = new ItemExosuit("Jump_Kit", EntityEquipmentSlot.CHEST);
    public static final Item NANO_HELM = new ItemExosuit("Nano_Helm", EntityEquipmentSlot.HEAD);

    public static final Item TORN_NOTE = new ItemTornNote();

}
