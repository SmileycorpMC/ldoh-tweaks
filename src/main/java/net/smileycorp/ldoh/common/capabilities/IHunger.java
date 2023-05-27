package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.hordes.infection.HordesInfection;
import net.smileycorp.ldoh.common.network.*;

import java.util.Random;

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

	public ItemStack tryPickupFood(ItemStack stack, EntityLiving entity);

	public void setFoodStack(ItemStack stack);

	public boolean isEating();

	public boolean hasHungerEffect();

	public void setHungerEffect(EntityLiving entity, boolean hasHunger);

	public void startEating(EntityLiving entity);

	public ItemStack getFoodSlot();

	public void syncClients(EntityLiving entity);

	public void syncClient(EntityLiving entity, EntityPlayerMP player);

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

		protected ItemStack foodSlot = new ItemStack(Items.BREAD, 8);
		protected ItemStack heldItem = ItemStack.EMPTY;

		protected boolean appleCoreInit = false;
		protected int eatingTicks = 0;

		protected boolean hasHunger = false;

		protected int foodLevel = 20;
		protected float foodSaturationLevel = 5.0F;
		protected float foodExhaustionLevel;
		protected int foodTimer;
		protected int prevFoodLevel = 20;

		@Override
		public void addStats(int foodLevelIn, float foodSaturationModifier) {
			foodLevel = Math.min(foodLevelIn + foodLevel, 20);
			foodSaturationLevel = Math.min(foodSaturationLevel + (float)foodLevelIn * foodSaturationModifier * 2.0F, (float)foodLevel);
		}

		@Override
		public void addStats(ItemFood foodItem, ItemStack stack) {
			addStats(foodItem.getHealAmount(stack), foodItem.getSaturationModifier(stack));
		}

		@Override
		public void onUpdate(EntityLiving entity) {
			if (!entity.world.isRemote) {
				EnumDifficulty enumdifficulty = entity.world.getDifficulty();
				prevFoodLevel = foodLevel;

				if (entity.isPotionActive(MobEffects.HUNGER)) {
					addExhaustion(0.005F * (entity.getActivePotionEffect(MobEffects.HUNGER).getAmplifier() + 1));
					if (!hasHunger)setHungerEffect(entity, true);
				} else if (hasHunger)setHungerEffect(entity, false);
				if (entity.isPotionActive(HordesInfection.INFECTED)) {
					addExhaustion(0.007F * (entity.getActivePotionEffect(HordesInfection.INFECTED).getAmplifier()+1));
				}

				if (foodExhaustionLevel > 4.0F)
				{
					foodExhaustionLevel -= 4.0F;

					if (getSaturationLevel() > 0.0F)
					{
						setFoodSaturationLevel(Math.max(getSaturationLevel() - 1.0F, 0.0F));
					}
					else if (enumdifficulty != EnumDifficulty.PEACEFUL)
					{
						setFoodLevel(Math.max(getFoodLevel() - 1, 0));
					}
				}

				boolean flag = entity.world.getGameRules().getBoolean("naturalRegeneration");

				if (flag && getSaturationLevel() > 0.0F && getFoodLevel() >= 20)
				{
					++foodTimer;

					if (foodTimer >= 10)
					{
						float f = Math.min(getSaturationLevel(), 6.0F);
						entity.heal(f / 6.0F);
						addExhaustion(f);
						foodTimer = 0;
					}
				}
				else if (flag && getFoodLevel() >= 18)
				{
					++foodTimer;

					if (foodTimer >= 80)
					{
						entity.heal(1.0F);
						addExhaustion(6.0F);
						foodTimer = 0;
					}
				}
				else if (foodLevel <= 0)
				{
					++foodTimer;

					if (foodTimer >= 80)
					{
						if (entity.getHealth() > 10.0F || enumdifficulty == EnumDifficulty.HARD || entity.getHealth() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL)
						{
							entity.attackEntityFrom(DamageSource.STARVE, 1.0F);
						}

						foodTimer = 0;
					}
				}
				else
				{
					foodTimer = 0;
				}
			}
			if (eatingTicks > 0) {
				eatingTicks--;
				foodSlot.getItem().onUsingTick(foodSlot, entity, 32-eatingTicks);
				if (entity.world.isRemote) renderEatAnimation(entity);
				if (eatingTicks == 0) {
					entity.stopActiveHand();
					entity.setHeldItem(EnumHand.MAIN_HAND, heldItem);
					if (!entity.world.isRemote) {
						ItemFood foodItem = (ItemFood) foodSlot.getItem();
						if (foodItem.potionId != null && entity.world.rand.nextFloat() < foodItem.potionEffectProbability) {
							entity.addPotionEffect(new PotionEffect(foodItem.potionId));
						}
						setFoodLevel(Math.min(foodItem.getHealAmount(foodSlot) + getFoodLevel(), 20));
						setFoodSaturationLevel(Math.min(getSaturationLevel() + foodItem.getHealAmount(foodSlot) * foodItem.getSaturationModifier(foodSlot) * 2.0F, getFoodLevel()));
						foodSlot.shrink(1);
						heldItem = ItemStack.EMPTY;
						PacketHandler.NETWORK_INSTANCE.sendToAll(new SyncFoodMessage(entity, foodSlot));
					}
				}
			}
			if (!foodSlot.isEmpty() && eatingTicks == 0 &! entity.world.isRemote) {
				if (getFoodLevel() + ((ItemFood) foodSlot.getItem()).getHealAmount(foodSlot) <= 20) {
					startEating(entity);
				}
			}
			if (!entity.world.isRemote && prevFoodLevel != getFoodLevel()) PacketHandler.NETWORK_INSTANCE.sendToAll(new SyncHungerMessage(entity, getFoodLevel()));
		}

		private void renderEatAnimation(EntityLivingBase entity) {
			World world = entity.world;
			Random rand = world.rand;
			if (foodSlot.getItemUseAction() == EnumAction.DRINK) {
				world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_GENERIC_DRINK, entity.getSoundCategory(), 0.5F, rand.nextFloat() * 0.1F + 0.9F, true);
			}

			if (foodSlot.getItemUseAction() == EnumAction.EAT)
			{
				for (int i = 0; i < 5; ++i)
				{
					Vec3d vec3d = new Vec3d(((double)rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
					vec3d = vec3d.rotatePitch(-entity.rotationPitch * 0.017453292F);
					vec3d = vec3d.rotateYaw(-entity.rotationYaw * 0.017453292F);
					double d0 = (double)(-rand.nextFloat()) * 0.6D - 0.3D;
					Vec3d vec3d1 = new Vec3d(((double)rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
					vec3d1 = vec3d1.rotatePitch(-entity.rotationPitch * 0.017453292F);
					vec3d1 = vec3d1.rotateYaw(-entity.rotationYaw * 0.017453292F);
					vec3d1 = vec3d1.addVector(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ);

					if (foodSlot.getHasSubtypes())
					{
						entity.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, Item.getIdFromItem(foodSlot.getItem()), foodSlot.getMetadata());
					}
					else
					{
						entity.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, Item.getIdFromItem(foodSlot.getItem()));
					}
				}
				world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_GENERIC_EAT, entity.getSoundCategory(), 0.5F + 0.5F * (float)rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F, true);
			}

		}

		@Override
		public void readNBT(NBTTagCompound compound) {
			if (compound.hasKey("foodLevel")) foodLevel = compound.getInteger("foodLevel");
			if (compound.hasKey("foodTickTimer")) foodTimer = compound.getInteger("foodTickTimer");
			if (compound.hasKey("foodSaturationLevel")) foodSaturationLevel = compound.getFloat("foodSaturationLevel");
			if (compound.hasKey("foodExhaustionLevel")) foodExhaustionLevel = compound.getFloat("foodExhaustionLevel");
			if (compound.hasKey("foodSlot")) foodSlot = new ItemStack((NBTTagCompound) compound.getTag("foodSlot"));
			if (compound.hasKey("heldItem")) heldItem = new ItemStack((NBTTagCompound) compound.getTag("heldItem"));
			if (compound.hasKey("eatingTicks")) eatingTicks = compound.getInteger("eatingTicks");
		}

		@Override
		public void writeNBT(NBTTagCompound compound) {
			compound.setInteger("foodLevel", foodLevel);
			compound.setInteger("foodTickTimer", foodTimer);
			compound.setFloat("foodSaturationLevel", foodSaturationLevel);
			compound.setFloat("foodExhaustionLevel", foodExhaustionLevel);
			compound.setTag("foodSlot", foodSlot.writeToNBT(new NBTTagCompound()));
			compound.setTag("heldItem", heldItem.writeToNBT(new NBTTagCompound()));
			compound.setInteger("eatingTicks", eatingTicks);
		}

		@Override
		public int getFoodLevel()
		{
			return foodLevel;
		}

		@Override
		public boolean needFood()
		{
			return foodLevel < 20;
		}

		@Override
		public void addExhaustion(float exhaustion)
		{
			foodExhaustionLevel = Math.min(foodExhaustionLevel + exhaustion, 40.0F);
		}

		@Override
		public float getSaturationLevel()
		{
			return foodSaturationLevel;
		}

		@Override
		public void setFoodLevel(int foodLevelIn)
		{
			foodLevel = foodLevelIn;
		}

		@Override
		public void setFoodSaturationLevel(float foodSaturationLevelIn)
		{
			foodSaturationLevel = foodSaturationLevelIn;
		}

		@Override
		public ItemStack tryPickupFood(ItemStack stack, EntityLiving entity) {
			if (!stack.isEmpty()) {
				if (foodSlot.isEmpty() && stack.getItem() instanceof ItemFood) {
					foodSlot = stack;
					PacketHandler.NETWORK_INSTANCE.sendToAll(new SyncFoodMessage(entity, foodSlot));
					return ItemStack.EMPTY;
				} else if (stack.getItem() == foodSlot.getItem() && foodSlot.getCount() < foodSlot.getMaxStackSize()) {
					int count = stack.getCount() + foodSlot.getCount();
					if (count > foodSlot.getMaxStackSize()) {
						foodSlot.setCount(foodSlot.getMaxStackSize());
						stack.setCount(count - foodSlot.getMaxStackSize());
					} else {
						foodSlot.setCount(count);
						PacketHandler.NETWORK_INSTANCE.sendToAll(new SyncFoodMessage(entity, foodSlot));
						return ItemStack.EMPTY;
					}
				}
			}
			PacketHandler.NETWORK_INSTANCE.sendToAll(new SyncFoodMessage(entity, foodSlot));
			return stack;
		}

		@Override
		public boolean isEating() {
			return eatingTicks > 0 &! foodSlot.isEmpty();
		}

		@Override
		public void setFoodStack(ItemStack stack) {
			foodSlot = stack;
		}

		@Override
		public void startEating(EntityLiving entity) {
			eatingTicks = 32;
			heldItem = entity.getHeldItem(EnumHand.MAIN_HAND);
			entity.setHeldItem(EnumHand.MAIN_HAND, foodSlot);
			entity.setActiveHand(EnumHand.MAIN_HAND);
			if (!entity.world.isRemote) PacketHandler.NETWORK_INSTANCE.sendToAll(new StartEatingMessage(entity));
		}

		@Override
		public ItemStack getFoodSlot() {
			return foodSlot;
		}

		@Override
		public void syncClients(EntityLiving entity) {
			PacketHandler.NETWORK_INSTANCE.sendToAllTracking(new SyncFoodMessage(entity, foodSlot), entity);
			PacketHandler.NETWORK_INSTANCE.sendToAllTracking(new SyncHungerMessage(entity, getFoodLevel()), entity);
		}

		@Override
		public void syncClient(EntityLiving entity, EntityPlayerMP player) {
			PacketHandler.NETWORK_INSTANCE.sendTo(new SyncFoodMessage(entity, foodSlot), player);
			PacketHandler.NETWORK_INSTANCE.sendTo(new SyncHungerMessage(entity, getFoodLevel()), player);
		}

		@Override
		public boolean hasHungerEffect() {
			return hasHunger;
		}

		@Override
		public void setHungerEffect(EntityLiving entity, boolean hasHunger) {
			this.hasHunger = hasHunger;
			if (!entity.world.isRemote) {
				PacketHandler.NETWORK_INSTANCE.sendToAllTracking(new SyncHungerEffectMessage(entity, hasHunger), entity);
			}
		}

	}

	public static class Provider implements ICapabilitySerializable<NBTTagCompound> {

		protected final IHunger instance = LDOHCapabilities.HUNGER.getDefaultInstance();

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == LDOHCapabilities.HUNGER;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return capability == LDOHCapabilities.HUNGER ? LDOHCapabilities.HUNGER.cast(instance) : null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return (NBTTagCompound) LDOHCapabilities.HUNGER.getStorage().writeNBT(LDOHCapabilities.HUNGER, instance, null);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			LDOHCapabilities.HUNGER.getStorage().readNBT(LDOHCapabilities.HUNGER, instance, null, nbt);
		}

	}

}
