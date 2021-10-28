package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.ldoh.common.ModContent;

public interface IHunger {

	public void addStats(int foodLevelIn, float foodSaturationModifier);

	public void addStats(ItemFood foodItem, ItemStack stack);

	public void onUpdate(EntityLiving entity);

	public void readNBT(NBTTagCompound compound);

	public void writeNBT(NBTTagCompound compound);

	public int getFoodLevel();

	public boolean needFood();

	public void addExhaustion(float exhaustion);

	public float getSaturationLevel();

	public void setFoodLevel(int foodLevelIn);

	public ItemStack tryPickupFood(ItemStack stack);

	public boolean isEating();

	@SideOnly(Side.CLIENT)
	public void setFoodSaturationLevel(float foodSaturationLevelIn);

	public static class Storage implements IStorage<IHunger> {

		@Override
		public NBTBase writeNBT(Capability<IHunger> capability, IHunger instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			instance.writeNBT(nbt);
			return nbt;
		}

		@Override
		public void readNBT(Capability<IHunger> capability, IHunger instance, EnumFacing side, NBTBase nbt) {
			instance.readNBT((NBTTagCompound) nbt);
		}


	}

	public static class Hunger implements IHunger {

		protected final FoodStats food = new FoodStats();
		protected ItemStack foodSlot = ItemStack.EMPTY;
		protected ItemStack heldItem = ItemStack.EMPTY;

		protected int eatingTicks = 0;

		@Override
		public void addStats(int foodLevelIn, float foodSaturationModifier) {
			food.addStats(foodLevelIn, foodSaturationModifier);
		}

		@Override
		public void addStats(ItemFood foodItem, ItemStack stack) {
			food.addStats(foodItem, stack);
		}

		@Override
		public void onUpdate(EntityLiving entity) {
			if (entity.world.isRemote) {

			} else {
				EnumDifficulty enumdifficulty = entity.world.getDifficulty();
				food.prevFoodLevel = food.foodLevel;

				if (food.foodExhaustionLevel > 4.0F)
				{
					food.foodExhaustionLevel -= 4.0F;

					if (food.foodSaturationLevel > 0.0F)
					{
						food.foodSaturationLevel = Math.max(food.foodSaturationLevel - 1.0F, 0.0F);
					}
					else if (enumdifficulty != EnumDifficulty.PEACEFUL)
					{
						food.foodLevel = Math.max(food.foodLevel - 1, 0);
					}
				}

				boolean flag = entity.world.getGameRules().getBoolean("naturalRegeneration");

				if (flag && food.foodSaturationLevel > 0.0F && food.foodLevel >= 20)
				{
					++food.foodTimer;

					if (food.foodTimer >= 10)
					{
						float f = Math.min(food.foodSaturationLevel, 6.0F);
						entity.heal(f / 6.0F);
						food.addExhaustion(f);
						food.foodTimer = 0;
					}
				}
				else if (flag && food.foodLevel >= 18)
				{
					++food.foodTimer;

					if (food.foodTimer >= 80)
					{
						entity.heal(1.0F);
						food.addExhaustion(6.0F);
						food.foodTimer = 0;
					}
				}
				else if (food.foodLevel <= 0)
				{
					++food.foodTimer;

					if (food.foodTimer >= 80)
					{
						if (entity.getHealth() > 10.0F || enumdifficulty == EnumDifficulty.HARD || entity.getHealth() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL)
						{
							entity.attackEntityFrom(DamageSource.STARVE, 1.0F);
						}

						food.foodTimer = 0;
					}
				}
				else
				{
					food.foodTimer = 0;
				}

			}
			if (eatingTicks > 0) {
				if (eatingTicks-- == 0) {
					entity.stopActiveHand();
					entity.setHeldItem(EnumHand.MAIN_HAND, heldItem);
					if (!entity.world.isRemote) {
						ItemFood foodItem = (ItemFood) foodSlot.getItem();
						if (foodItem.potionId != null && entity.world.rand.nextFloat() < foodItem.potionEffectProbability) {
							entity.addPotionEffect(new PotionEffect(foodItem.potionId));
						}
						food.addStats((ItemFood) foodSlot.getItem(), foodSlot);
						foodSlot.shrink(1);
						heldItem = ItemStack.EMPTY;
					}
				}
			}
			if (!foodSlot.isEmpty()) {
				if (food.getFoodLevel() <= 20 - ((ItemFood) foodSlot.getItem()).getHealAmount(foodSlot)) {
					eatingTicks = 32;
					heldItem = entity.getHeldItem(EnumHand.MAIN_HAND);
					entity.setHeldItem(EnumHand.MAIN_HAND, foodSlot);
					entity.setActiveHand(EnumHand.MAIN_HAND);
				}
			}
		}

		@Override
		public void readNBT(NBTTagCompound compound) {
			food.readNBT(compound);
			if (compound.hasKey("foodSlot")) foodSlot = new ItemStack((NBTTagCompound) compound.getTag("foodSlot"));
			if (compound.hasKey("heldItem")) heldItem = new ItemStack((NBTTagCompound) compound.getTag("heldItem"));
			if (compound.hasKey("eatingTicks")) eatingTicks = compound.getInteger("eatingTicks");
		}

		@Override
		public void writeNBT(NBTTagCompound compound) {
			food.writeNBT(compound);
			compound.setTag("foodSlot", foodSlot.writeToNBT(new NBTTagCompound()));
			compound.setTag("heldItem", heldItem.writeToNBT(new NBTTagCompound()));
			compound.setInteger("eatingTicks", eatingTicks);
		}

		@Override
		public int getFoodLevel() {
			return food.getFoodLevel();
		}

		@Override
		public boolean needFood() {
			return food.needFood();
		}

		@Override
		public void addExhaustion(float exhaustion) {
			food.addExhaustion(exhaustion);
		}

		@Override
		public float getSaturationLevel() {
			return food.getSaturationLevel();
		}

		@Override
		public void setFoodLevel(int foodLevelIn) {
			food.setFoodLevel(foodLevelIn);
		}

		@Override
		public void setFoodSaturationLevel(float foodSaturationLevelIn) {
			food.setFoodSaturationLevel(foodSaturationLevelIn);
		}

		@Override
		public ItemStack tryPickupFood(ItemStack stack) {
			if (!stack.isEmpty()) {
				if (foodSlot.isEmpty() && stack.getItem() instanceof ItemFood) {
					foodSlot = stack;
					return ItemStack.EMPTY;
				} else if (stack.getItem() == foodSlot.getItem() && foodSlot.getCount() < foodSlot.getMaxStackSize()) {
					int count = stack.getCount() + foodSlot.getCount();
					if (count > foodSlot.getMaxStackSize()) {
						foodSlot.setCount(foodSlot.getMaxStackSize());
						stack.setCount(count - foodSlot.getMaxStackSize());
					} else {
						foodSlot.setCount(count);
						return ItemStack.EMPTY;
					}
				}
			}
			return stack;
		}

		@Override
		public boolean isEating() {
			return eatingTicks > 0 &! foodSlot.isEmpty();
		}

	}

	public static class Provider implements ICapabilitySerializable<NBTTagCompound> {

		protected final IHunger instance = ModContent.HUNGER.getDefaultInstance();

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == ModContent.HUNGER;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return capability == ModContent.HUNGER ? ModContent.HUNGER.cast(instance) : null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return (NBTTagCompound) ModContent.HUNGER.getStorage().writeNBT(ModContent.HUNGER, instance, null);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			ModContent.HUNGER.getStorage().readNBT(ModContent.HUNGER, instance, null, nbt);
		}

	}

	public static enum RaidType {
		ZOMBIE, ENEMY, ALLY, PARASITE;
	}

}
