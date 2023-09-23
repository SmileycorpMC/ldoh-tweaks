package net.smileycorp.ldoh.common.entity;

import buildcraft.core.BCCoreItems;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.item.LDOHItems;

public class EntityZombieTechnician extends EntityProfessionZombie {

	public EntityZombieTechnician(World world) {
		super(world);
	}

	@Override
	protected void setEquipment() {
		setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(BCCoreItems.wrench));
		setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(LDOHItems.HARDHAT));
	}

	@Override
	protected String getStage() {
		return "build_stage";
	}

}
