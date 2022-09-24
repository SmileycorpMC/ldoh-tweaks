package net.smileycorp.ldoh.common.entity;

import com.Fishmod.mod_LavaCow.entities.flying.EntityVespa;
import com.Fishmod.mod_LavaCow.init.ModMobEffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.item.ItemSpawner;

public class EntityFrigidVespa extends EntityVespa {

	public EntityFrigidVespa(World world) {
		super(world);
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity)
	{
		if (super.attackEntityAsMob(par1Entity))
		{
			if (par1Entity instanceof EntityLivingBase)
			{
				float local_difficulty = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();

				((EntityLivingBase)par1Entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 6 * (int)local_difficulty, 1));
				if(rand.nextInt(5) == 0)
					((EntityLivingBase)par1Entity).addPotionEffect(new PotionEffect(ModMobEffects.INFESTED, 6 * 20 * (int)local_difficulty, 0));
			}

			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return ItemSpawner.getEggFor(this);
	}

}
