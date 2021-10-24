package net.smileycorp.ldoh.common.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.item.ItemSpawner;

public class EntityCrawlingZombie extends EntityZombie {
	
	public EntityCrawlingZombie(World world) {
		super(world);
	}

	@Override
	public boolean shouldBurnInDay() {
		return false;
	}
	
	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6D);
	}
    
	@Override
    public ItemStack getPickedResult(RayTraceResult target) {
		return ItemSpawner.getEggFor(this);
    }

}
