package net.smileycorp.ldoh.common.util;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import rafradek.TF2weapons.entity.mercenary.EntityDemoman;
import rafradek.TF2weapons.entity.mercenary.EntityEngineer;
import rafradek.TF2weapons.entity.mercenary.EntityHeavy;
import rafradek.TF2weapons.entity.mercenary.EntityMedic;
import rafradek.TF2weapons.entity.mercenary.EntityPyro;
import rafradek.TF2weapons.entity.mercenary.EntityScout;
import rafradek.TF2weapons.entity.mercenary.EntitySniper;
import rafradek.TF2weapons.entity.mercenary.EntitySoldier;
import rafradek.TF2weapons.entity.mercenary.EntitySpy;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;
import rafradek.TF2weapons.item.ItemFromData;
import rafradek.TF2weapons.item.ItemWeapon;
import rafradek.TF2weapons.util.WeaponData;

import com.google.common.base.Predicate;

public enum EnumTFClass {
	DEMOMAN("demoman", EntityDemoman.class, 4),
	ENGINEER("engineer", EntityEngineer.class, 8),
	HEAVY("heavy", EntityHeavy.class, 7),
	MEDIC("medic", EntityMedic.class, 8),
	PYRO("pyro", EntityPyro.class, 7),
	SCOUT("scout", EntityScout.class, 4),
	SNIPER("sniper", EntitySniper.class, 6),
	SOLDIER("soldier", EntitySoldier.class, 5),
	SPY("spy", EntitySpy.class, 6);

	private static Random rand = new Random();

	protected final String name;
	protected final Class<? extends EntityTF2Character> clazz;
	protected final int cost;

	EnumTFClass(String name, Class<? extends EntityTF2Character> clazz, int cost) {
		this.name = name;
		this.clazz = clazz;
		this.cost = cost;
	}

	public String getClassName() {
		return name;
	}

	public boolean testClass(Class<? extends EntityTF2Character> clazz) {
		return this.clazz.isAssignableFrom(clazz);
	}

	public EntityTF2Character createEntity(World world) throws Exception {
		return clazz.getConstructor(World.class).newInstance(world);
	}

	public Class<? extends EntityTF2Character> getEntityClass(){
		return clazz;
	}

	public int getCost() {
		return cost;
	}

	public boolean canUseItem(ItemStack stack) {
		if (stack.getItem() instanceof ItemWeapon) {
			WeaponData data = ItemFromData.getData(stack);
			if (data != null) {
				if (ItemFromData.isItemOfClass(data, name)) {
					return true;
				}
				return false;
			}
			return true;
		}
		return false;
	}

	public static EnumTFClass getRandomClass() {
		return values()[rand.nextInt(values().length)];
	}

	public static EnumTFClass getRandomClass(Predicate<EnumTFClass> predicate) {
		EnumTFClass tfClass = getRandomClass();
		while (!predicate.apply(tfClass)) tfClass = getRandomClass();
		return tfClass;
	}

	public static EnumTFClass getClass(EntityTF2Character entity) {
		for (EnumTFClass tfClass : values()) if (tfClass.testClass(entity.getClass())) return tfClass;
		return null;
	}
}
