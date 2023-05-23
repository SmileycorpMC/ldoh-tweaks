package net.smileycorp.ldoh.common.item;

import java.util.UUID;

import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;


public class ItemHelmet extends ItemHat {

	protected final int protection;
	protected final float toughness;

	public static final UUID HELMET_UUID = UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150");

	public ItemHelmet(String name, int durability, int protection, float toughness) {
		super(name);
		setMaxDamage(durability);
		this.protection = protection;
		this.toughness = toughness;
	}

	@Override
	@SuppressWarnings("deprecation")
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.HEAD) {
			multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(HELMET_UUID, "Armor modifier", protection, 0));
			multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(HELMET_UUID, "Armor toughness", toughness, 0));
		}

		return multimap;
	}

}
