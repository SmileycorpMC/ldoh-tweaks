package net.smileycorp.ldoh.common.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.item.ItemSpawner;

public class EntityCrawlingHusk extends EntityHusk {

	public EntityCrawlingHusk(World world) {
		super(world);
		setSize(0.8F, 0.8F);
		width=zombieWidth;
		height=zombieHeight;
		double d0 = (double)width / 2.0D;
		setEntityBoundingBox(new AxisAlignedBB(posX - d0, posY, posZ - d0, posX + d0, posY + (double)height, posZ + d0));
	}

	@Override
	public boolean shouldBurnInDay() {
		return false;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.13D);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return ItemSpawner.getEggFor(this);
	}

	@Override
	public float getEyeHeight() {
		return 0.6F;
	}

}
