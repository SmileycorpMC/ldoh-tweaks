package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.ldoh.common.network.PacketHandler;
import net.smileycorp.ldoh.common.network.SyncSleepMessage;
import net.smileycorp.ldoh.common.util.ModUtils;

public interface IExhaustion {

	public boolean isSleeping(EntityLiving entity);

	public void setSleeping(EntityLiving entity, boolean sleep);

	public boolean shouldSleep(EntityLiving entity);

	public boolean isTired(EntityLiving entity);

	public void onUpdate(EntityLiving entity);

	public void syncClients(EntityLiving entity);

	public void syncClient(EntityLiving entity, EntityPlayerMP player);

	public void readNBT(NBTTagCompound compound);

	public void writeNBT(NBTTagCompound compound);

	public static class Storage implements IStorage<IExhaustion> {

		@Override
		public NBTBase writeNBT(Capability<IExhaustion> capability, IExhaustion instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			instance.writeNBT(nbt);
			return nbt;
		}

		@Override
		public void readNBT(Capability<IExhaustion> capability, IExhaustion instance, EnumFacing side, NBTBase nbt) {
			instance.readNBT((NBTTagCompound) nbt);
		}

	}

	public static class Exhaustion implements IExhaustion {

		private int exhaustion;
		private boolean sleeping;
		private boolean tired;

		private EntityAITasks tasks = null;
		private EntityAITasks targetTasks = null;

		@Override
		public boolean isSleeping(EntityLiving entity) {
			if (entity.world.isRemote) return sleeping;
			if (tasks == null || targetTasks == null) return false;
			return !(tasks.taskEntries.isEmpty() || targetTasks.taskEntries.isEmpty());
		}

		@Override
		public void setSleeping(EntityLiving entity, boolean sleep) {
			sleeping = sleep;
			if (!entity.world.isRemote) {
				if (sleep) {
					tasks = entity.tasks;
					targetTasks = entity.targetTasks;
					entity.tasks.taskEntries.clear();
					entity.targetTasks.taskEntries.clear();
				} else {
					if (tasks!=null) {
						entity.tasks.taskEntries.addAll(tasks.taskEntries);
						tasks.taskEntries.clear();
					}
					if (targetTasks!=null) {
						entity.targetTasks.taskEntries.addAll(targetTasks.taskEntries);
						targetTasks.taskEntries.clear();
					}
				}
				syncClients(entity);
			}
		}

		@Override
		public boolean shouldSleep(EntityLiving entity) {
			return exhaustion > 50 && entity.getAttackTarget() == null;
		}

		@Override
		public boolean isTired(EntityLiving entity) {
			return exhaustion >= 900;
		}

		@Override
		public void onUpdate(EntityLiving entity) {
			if (!entity.world.isRemote) {
				if (!(entity.tasks.taskEntries.isEmpty() || entity.targetTasks.taskEntries.isEmpty())) setSleeping(entity, false);
				if (entity.ticksExisted % 20 == 0) {
					if (isSleeping(entity)) {
						exhaustion = exhaustion - 8;
						if (tired &! isTired(entity)) {
							IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
							if (attribute.hasModifier(ModUtils.TIRED_MODIFIER)) attribute.removeModifier(ModUtils.TIRED_MODIFIER);
							tired = false;
						}
						if (exhaustion <= 10) {
							setSleeping(entity, false);
						}
					} else {
						if (sleeping == true) {
							setSleeping(entity, true);
						} else {
							exhaustion++;
							if (isTired(entity)) {
								if (shouldSleep(entity)) {
									setSleeping(entity, true);
								} else {
									if (!tired) {
										tired = true;
										entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(ModUtils.TIRED_MODIFIER);
									}
									int a = Math.floorDiv(exhaustion - 900, 150);
									entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, a));
									entity.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 20, a));
								}
							}
						}
					}
				}
			}
		}

		@Override
		public void syncClients(EntityLiving entity) {
			PacketHandler.NETWORK_INSTANCE.sendToAllTracking(new SyncSleepMessage(entity, sleeping), entity);
		}

		@Override
		public void syncClient(EntityLiving entity, EntityPlayerMP player) {
			PacketHandler.NETWORK_INSTANCE.sendTo(new SyncSleepMessage(entity, sleeping), player);
		}

		@Override
		public void readNBT(NBTTagCompound compound) {
			if (compound.hasKey("exhaustion")) exhaustion = compound.getInteger("exhaustion");
			if (compound.hasKey("sleeping")) sleeping = compound.getBoolean("sleeping");
			if (compound.hasKey("tired")) tired = compound.getBoolean("tired");
		}

		@Override
		public void writeNBT(NBTTagCompound compound) {
			compound.setInteger("exhaustion", exhaustion);
			compound.setBoolean("sleeping", sleeping);
			compound.setBoolean("tired", tired);
		}

	}

	public static class Provider implements ICapabilitySerializable<NBTTagCompound> {

		protected final IExhaustion instance = LDOHCapabilities.EXHAUSTION.getDefaultInstance();

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == LDOHCapabilities.EXHAUSTION;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return capability == LDOHCapabilities.EXHAUSTION ? LDOHCapabilities.EXHAUSTION.cast(instance) : null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return (NBTTagCompound) LDOHCapabilities.EXHAUSTION.getStorage().writeNBT(LDOHCapabilities.EXHAUSTION, instance, null);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			LDOHCapabilities.EXHAUSTION.getStorage().readNBT(LDOHCapabilities.EXHAUSTION, instance, null, nbt);
		}

	}

}
