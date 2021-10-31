package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.init.MobEffects;
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
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.ldoh.common.ModContent;
import squeek.applecore.asm.util.IAppleCoreFoodStats;

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

		protected final FoodStats hunger = new FoodStats();
		protected ItemStack foodSlot = ItemStack.EMPTY;
		protected ItemStack heldItem = ItemStack.EMPTY;

		protected boolean appleCoreInit = false;

		protected int eatingTicks = 0;

		@Override
		public void addStats(int foodLevelIn, float foodSaturationModifier) {
			hunger.addStats(foodLevelIn, foodSaturationModifier);
		}

		@Override
		public void addStats(ItemFood foodItem, ItemStack stack) {
			hunger.addStats(foodItem, stack);
		}

		@Override
		public void onUpdate(EntityLiving entity) {
			if (entity.world.isRemote) {

			} else {
				if  (hunger instanceof IAppleCoreFoodStats &! appleCoreInit) {
					((IAppleCoreFoodStats) hunger).setPlayer(FakePlayerFactory.getMinecraft((WorldServer) entity.world));
					appleCoreInit = true;
				}
				if (entity.isPotionActive(MobEffects.HUNGER)) {
					hunger.addExhaustion(0.005F * entity.getActivePotionEffect(MobEffects.HUNGER).getAmplifier() + 1);
				}
				EnumDifficulty enumdifficulty = entity.world.getDifficulty();
				hunger.prevFoodLevel = hunger.foodLevel;

				if (hunger.foodExhaustionLevel > 4.0F)
				{
					hunger.foodExhaustionLevel -= 4.0F;

					if (hunger.foodSaturationLevel > 0.0F)
					{
						hunger.foodSaturationLevel = Math.max(hunger.foodSaturationLevel - 1.0F, 0.0F);
					}
					else if (enumdifficulty != EnumDifficulty.PEACEFUL)
					{
						hunger.foodLevel = Math.max(hunger.foodLevel - 1, 0);
					}
				}

				boolean flag = entity.world.getGameRules().getBoolean("naturalRegeneration");

				if (flag && hunger.foodSaturationLevel > 0.0F && hunger.foodLevel >= 20)
				{
					++hunger.foodTimer;

					if (hunger.foodTimer >= 10)
					{
						float f = Math.min(hunger.foodSaturationLevel, 6.0F);
						entity.heal(f / 6.0F);
						hunger.addExhaustion(f);
						hunger.foodTimer = 0;
					}
				}
				else if (flag && hunger.foodLevel >= 18)
				{
					++hunger.foodTimer;

					if (hunger.foodTimer >= 80)
					{
						entity.heal(1.0F);
						hunger.addExhaustion(6.0F);
						hunger.foodTimer = 0;
					}
				}
				else if (hunger.foodLevel <= 0)
				{
					++hunger.foodTimer;

					if (hunger.foodTimer >= 80)
					{
						if (entity.getHealth() > 10.0F || enumdifficulty == EnumDifficulty.HARD || entity.getHealth() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL)
						{
							entity.attackEntityFrom(DamageSource.STARVE, 1.0F);
						}

						hunger.foodTimer = 0;
					}
				}
				else
				{
					hunger.foodTimer = 0;
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
						hunger.addStats((ItemFood) foodSlot.getItem(), foodSlot);
						foodSlot.shrink(1);
						heldItem = ItemStack.EMPTY;
					}
				}
			}
			if (!foodSlot.isEmpty()) {
				if (hunger.getFoodLevel() <= 20 - ((ItemFood) foodSlot.getItem()).getHealAmount(foodSlot)) {
					eatingTicks = 32;
					heldItem = entity.getHeldItem(EnumHand.MAIN_HAND);
					entity.setHeldItem(EnumHand.MAIN_HAND, foodSlot);
					entity.setActiveHand(EnumHand.MAIN_HAND);
				}
			}
		}

		@Override
		public void readNBT(NBTTagCompound compound) {
			hunger.readNBT(compound);
			if (compound.hasKey("foodSlot")) foodSlot = new ItemStack((NBTTagCompound) compound.getTag("foodSlot"));
			if (compound.hasKey("heldItem")) heldItem = new ItemStack((NBTTagCompound) compound.getTag("heldItem"));
			if (compound.hasKey("eatingTicks")) eatingTicks = compound.getInteger("eatingTicks");
		}

		@Override
		public void writeNBT(NBTTagCompound compound) {
			hunger.writeNBT(compound);
			compound.setTag("foodSlot", foodSlot.writeToNBT(new NBTTagCompound()));
			compound.setTag("heldItem", heldItem.writeToNBT(new NBTTagCompound()));
			compound.setInteger("eatingTicks", eatingTicks);
		}

		@Override
		public int getFoodLevel() {
			return hunger.getFoodLevel();
		}

		@Override
		public boolean needFood() {
			return hunger.needFood();
		}

		@Override
		public void addExhaustion(float exhaustion) {
			hunger.addExhaustion(exhaustion);
		}

		@Override
		public float getSaturationLevel() {
			return hunger.getSaturationLevel();
		}

		@Override
		public void setFoodLevel(int foodLevelIn) {
			hunger.setFoodLevel(foodLevelIn);
		}

		@Override
		public void setFoodSaturationLevel(float foodSaturationLevelIn) {
			hunger.setFoodSaturationLevel(foodSaturationLevelIn);
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
