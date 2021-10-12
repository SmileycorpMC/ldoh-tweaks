package net.smileycorp.hundreddayz.common.entity;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.hundreddayz.common.ModDefinitions;
import net.smileycorp.hundreddayz.common.item.ItemSpawner;

import com.animania.addons.farm.common.handler.FarmAddonItemHandler;

public class EntityZombieNurse extends EntityZombie {
	
	private Set<WeakReference<EntityZombie>> healTargets = new HashSet<WeakReference<EntityZombie>>();
	
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
		 if (world.getWorldTime()%20==0){
			 for (EntityZombie entity : world.getEntitiesWithinAABB(EntityZombie.class, getEntityBoundingBox().grow(5))) {
				 if (entity.getHealth() < entity.getMaxHealth()) {
					 if (world.isRemote) {
						 if (!healTargets.contains(entity)) healTargets.add(new WeakReference(entity));
						 if (entity.getHealth() >= entity.getMaxHealth()) {
							 healTargets.remove(entity);
						 }
						 for (int i = 0; i < 6; ++i) {
							 world.spawnParticle(EnumParticleTypes.HEART, entity.posX + (rand.nextDouble() - 0.5D) * entity.width,
								entity.posY + rand.nextDouble() * entity.height, entity.posZ + (rand.nextDouble() - 0.5D) * entity.width, 0.0D, 0.3D, 0.0D);
				         }
					 } else {
						 entity.heal(1f); 
					 }
				 }
			 }
		 } if (world.isRemote) {
			 for (WeakReference<EntityZombie> ref : healTargets) {
				 EntityZombie entity = ref.get();
				 if (entity!=this) {
					 Vec3d dir = DirectionUtils.getDirectionVec(this, entity);
					 float v = getDistance(entity)/10;
					 world.spawnParticle(EnumParticleTypes.END_ROD, posX, posY+0.8d, posZ, dir.x*v, dir.y*v, dir.z*v); 
				 }
			 }
			 healTargets.clear();
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
