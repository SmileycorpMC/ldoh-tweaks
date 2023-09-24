package net.smileycorp.ldoh.common.entity;

import de.maxhenkel.car.items.ModItems;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.item.LDOHItems;

public class EntityZombieMechanic extends EntityProfessionZombie {

	public EntityZombieMechanic(World world) {
		super(world);
	}

	@Override
	protected void setEquipment() {
		setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.WRENCH));
		setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(LDOHItems.MECHANIC_HAT));
	}

	@Override
	protected String getStage() {
		return "car_stage";
	}

}
