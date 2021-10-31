package net.smileycorp.ldoh.common.entity;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDummyHusk2 extends EntityHusk {

	public EntityDummyHusk2(World world) {
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
