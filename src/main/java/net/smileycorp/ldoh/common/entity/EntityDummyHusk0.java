package net.smileycorp.ldoh.common.entity;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.util.IDummyZombie;

public class EntityDummyHusk0 extends EntityHusk implements IDummyZombie {

	public EntityDummyHusk0(World world) {
		super(world);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		ResourceLocation loc = new ResourceLocation("minecraft", "husk");
		if (EntityList.ENTITY_EGGS.containsKey(loc)) {
			ItemStack stack = new ItemStack(Items.SPAWN_EGG);
			ItemMonsterPlacer.applyEntityIdToItemStack(stack, loc);
			return stack;
		}
		return ItemStack.EMPTY;
	}

}
