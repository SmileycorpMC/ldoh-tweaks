package net.smileycorp.ldoh.common.util;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.smileycorp.ldoh.common.item.LDOHItems;

public enum EnumBarbedWireMat implements IStringSerializable {

	IRON("Iron", 0.5f, 300, Items.IRON_NUGGET),
	GOLD("Gold", 3f, 100, Items.GOLD_NUGGET),
	DIAMOND("Diamond", 1f, 900, LDOHItems.DIAMOND_NUGGET);

	final String name;
	final float damage;
	final int durability;
	final Item drop;

	EnumBarbedWireMat(String name, float damage, int durability, Item drop) {
		this.name = name;
		this.damage = damage;
		this.durability = durability;
		this.drop = drop;
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

	public Item getDrop() {
		return drop;
	}

}