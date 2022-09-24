package net.smileycorp.ldoh.common.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.ldoh.common.damage.DamageSourceBarbedWire;
import net.smileycorp.ldoh.common.entity.EntityIncendiaryProjectile;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityParasiteBase;
import com.mrcrayfish.guns.entity.EntityProjectile;

public class DefenseEvents {

	//apply looting to barbed wire kills
	@SubscribeEvent
	public void getLootingModifier(LootingLevelEvent event) {
		DamageSource cause = event.getDamageSource();
		if (cause instanceof DamageSourceBarbedWire) {
			TileBarbedWire tile = ((DamageSourceBarbedWire) cause).getSource();
			if (tile.hasEnchantment(Enchantments.LOOTING)) {
				event.setLootingLevel(tile.getEnchantmentLevel(Enchantments.LOOTING));
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onHurt(LivingHurtEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		Entity attacker = event.getSource().getImmediateSource();
		World world = entity.world;
		if (!world.isRemote) {
			if (attacker instanceof EntityProjectile) {
				float totalDamage = 0;
				for (EntityProjectile projectile : world.getEntitiesWithinAABB(EntityProjectile.class, entity.getEntityBoundingBox().grow(0.1))) {
					if (entity instanceof EntityPlayer) {
						EntityLivingBase shooter = projectile.getShooter();
						if(shooter instanceof EntityPlayer && !((EntityPlayer) shooter).canAttackPlayer((EntityPlayer) entity)) continue;
					}
					float damage = projectile.getDamage();
					if (projectile instanceof EntityIncendiaryProjectile) {
						if (entity instanceof EntityParasiteBase) damage = damage * 3;
						else damage = damage * 0.7f;
					}
					System.out.println(damage);
					totalDamage += damage;
					projectile.setDead();
				}
				if (totalDamage!=0) event.setAmount(totalDamage);
				else System.out.println("ahhhhhh");
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDamage(LivingDamageEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		Entity attacker = event.getSource().getImmediateSource();
		if (attacker instanceof EntityIncendiaryProjectile) {
			entity.setFire(2);
		}
	}

}
