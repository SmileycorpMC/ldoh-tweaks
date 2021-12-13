package net.smileycorp.ldoh.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;

public class EntityIncendiaryProjectile extends EntityProjectile {

	public EntityIncendiaryProjectile(World world) {
		super(world);
	}

	public EntityIncendiaryProjectile(World world, EntityLivingBase shooter, ItemGun item, Gun modifiedGun) {
		super(world, shooter, item, modifiedGun);
	}

}
