package net.smileycorp.hundreddayz.common.block;

import net.minecraft.util.IStringSerializable;

public enum EnumBarbedWireMat implements IStringSerializable {
	
	IRON("Iron", 0.5f, 300), GOLD("Gold", 2f, 100), DIAMOND("Diamond", 1f, 900);
	
	final String name;
	final float damage;
	final int durability;
	
	EnumBarbedWireMat(String name, float damage, int durability) {
		this.name=name;
		this.damage=damage;
		this.durability=durability;
	}

	@Override
	public String getName() {
		return name.toLowerCase();
	}
	
	public String getUnlocalisedName() {
		return name;
	}
	
	public static EnumBarbedWireMat byMeta(int meta) {
		EnumBarbedWireMat[] values = values();
		if (meta >= values.length) meta = 0;
		return values[meta];
	}
	
	public float getDamage() {
		return damage;
	}
	
	public int getDurability() {
		return durability;
	}
	
}