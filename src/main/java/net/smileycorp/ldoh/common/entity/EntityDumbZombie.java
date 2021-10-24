package net.smileycorp.ldoh.common.entity;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDumbZombie extends EntityZombie {
	
	public EntityDumbZombie(World world) {
		super(world);
	}

	@Override
	public boolean shouldBurnInDay() {
		return false;
	}
	
	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.13D);
	}
    
    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
    	ResourceLocation loc = new ResourceLocation("minecraft", "zombie");
    	if (EntityList.ENTITY_EGGS.containsKey(loc)) {
            ItemStack stack = new ItemStack(Items.SPAWN_EGG);
            ItemMonsterPlacer.applyEntityIdToItemStack(stack, loc);
            return stack;
        }
    	return ItemStack.EMPTY;
    }

}
