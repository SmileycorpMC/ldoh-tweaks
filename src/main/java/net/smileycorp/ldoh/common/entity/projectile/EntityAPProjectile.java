package net.smileycorp.ldoh.common.entity.projectile;

import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityAPProjectile extends EntityProjectile {

    public EntityAPProjectile(World world) {
        super(world);
    }

    public EntityAPProjectile(World world, EntityLivingBase shooter, ItemGun item, Gun modifiedGun) {
        super(world, shooter, item, modifiedGun);
    }

}
