package net.smileycorp.hundreddayz.common.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.smileycorp.hundreddayz.common.ModDefinitions;
import net.smileycorp.hundreddayz.common.item.ItemSpawner;

import com.animania.addons.farm.common.handler.FarmAddonItemHandler;

public class EntityZombieNurse extends EntityZombie {
	
	public EntityZombieNurse(World world) {
		super(world);
	}
	
	@Override
	protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
    }
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(FarmAddonItemHandler.carvingKnife));
	}
	
	@Override
	public boolean shouldBurnInDay() {
		return false;
	}
	
	@Override
	public void onLivingUpdate() {
		 if (!world.isRemote && world.getWorldTime()%20==0){
			 boolean healed = false;
			 for (EntityZombie entity : world.getEntitiesWithinAABB(EntityZombie.class, getEntityBoundingBox().grow(5))) {
				 entity.heal(1f);
				 healed = true;
			 }
			 if (healed) {
				 for (int i = 1; i<=8; i++) {
					int a = Math.round(45*i);
					double rad = Math.toRadians(a);
					double xoff = Math.cos(rad);
					double x = posX + xoff;
					double y = posY;
					double zoff = Math.sin(rad);
					double z = posZ +zoff;
					world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, x, y, z, xoff, 0, zoff);
				}
			 }
		 }
		 super.onLivingUpdate();
	}
	
	@Override
    public ItemStack getPickedResult(RayTraceResult target) {
		return ItemSpawner.getEggFor(this);
    }
	
	@Override
	protected ResourceLocation getLootTable() {
        return ModDefinitions.getResource("entities/nurse_zombie");
    }

}
