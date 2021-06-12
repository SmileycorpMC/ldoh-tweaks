package net.smileycorp.hundreddayz.common.entity;

import java.util.Random;

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

public enum EnumTFClass {
	DEMOMAN("demoman", EntityDemoman.class),
	ENGINEER("engineer", EntityEngineer.class),
	HEAVY("heavy", EntityHeavy.class),
	MEDIC("medic", EntityMedic.class),
	PYRO("pyro", EntityPyro.class),
	SCOUT("scout", EntityScout.class),
	SNIPER("sniper", EntitySniper.class),
	SOLDIER("soldier", EntitySoldier.class),
	SPY("spy", EntitySpy.class);
	
	protected final String name;
	protected final Class<? extends EntityTF2Character> clazz;
	
	EnumTFClass(String name, Class<? extends EntityTF2Character> clazz) {
		this.name = name;
		this.clazz=clazz;
	}
	
	public String getUnlocalisedName() {
		return name;
	}
	
	public boolean testClass(Class<? extends EntityTF2Character> clazz) {
		return this.clazz.isAssignableFrom(clazz);
	}

	public static EnumTFClass getRandomClass() {
		return values()[new Random().nextInt(values().length)];
	}

	public EntityTF2Character createEntity(World world) throws Exception {
		return clazz.getConstructor(World.class).newInstance(world);
	}
}
