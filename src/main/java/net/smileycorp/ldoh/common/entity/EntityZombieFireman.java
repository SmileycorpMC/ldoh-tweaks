package net.smileycorp.ldoh.common.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.item.ItemSpawner;
import net.smileycorp.ldoh.common.item.LDOHItems;
import funwayguy.epicsiegemod.ai.ESM_EntityAIDigging;
import funwayguy.epicsiegemod.ai.ESM_EntityAIGrief;

public class EntityZombieFireman extends EntityZombie {

	public EntityZombieFireman(World world) {
		super(world);
		setSize(0.65F, 2F);
		setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
		setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
		isImmuneToFire = true;
	}

	@Override
	protected void initEntityAI() {
		tasks.addTask(1, new ESM_EntityAIDigging(this));
		tasks.addTask(6, new ESM_EntityAIGrief(this));
		super.initEntityAI();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6D);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(10.0D);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(LDOHItems.FIRE_AXE));
		setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(LDOHItems.FIREMAN_HAT));
		inventoryArmorDropChances[EntityEquipmentSlot.MAINHAND.getIndex()] = 0.1F;
		inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.1F;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (world.getBlockState(getPosition()).getBlock() == Blocks.FIRE) {
			world.setBlockState(getPosition(), Blocks.AIR.getDefaultState());
			playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.75f, 0);
		}
	}

	@Override
	public boolean shouldBurnInDay() {
		return false;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return ItemSpawner.getEggFor(this);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ModDefinitions.getResource("entities/zombie_fireman");
	}

}
