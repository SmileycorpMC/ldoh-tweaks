package net.smileycorp.ldoh.common.item;

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
	public static final Item HARDHAT = new ItemHelmet("Hardhat", 100, 2, 0f);
	public static final Item BONESAW = new ItemWeapon("Bonesaw", 151, 5.5);
	public static final Item TF_PROF_TOKEN = new ItemTFProfessionToken();
	public static final Item INCENDIARY_AMMO = new ItemIncendiaryAmmo();

}
