package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface IMiniRaid {

	public boolean shouldSpawnRaid(EntityPlayer player);

	public void spawnRaid(EntityPlayer player);

	public void spawnRaid(EntityPlayer player, RaidType type, int phase);

	public NBTTagCompound writeToNBT(NBTTagCompound nbt);

	public void readFromNBT(NBTTagCompound nbt);

	public static class Storage implements IStorage<IMiniRaid> {

		@Override
		public NBTBase writeNBT(Capability<IMiniRaid> capability, IMiniRaid instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			instance.writeToNBT(nbt);
			return nbt;
		}

		@Override
		public void readNBT(Capability<IMiniRaid> capability, IMiniRaid instance, EnumFacing side, NBTBase nbt) {
			instance.readFromNBT((NBTTagCompound) nbt);
		}


	}

	public static class Provider implements ICapabilitySerializable<NBTTagCompound> {

		protected final IMiniRaid instance = new MiniRaid();

		public Provider() {}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == LDOHCapabilities.MINI_RAID;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return capability == LDOHCapabilities.MINI_RAID ? LDOHCapabilities.MINI_RAID.cast(instance) : null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return (NBTTagCompound) LDOHCapabilities.MINI_RAID.getStorage().writeNBT(LDOHCapabilities.MINI_RAID, instance, null);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			LDOHCapabilities.MINI_RAID.getStorage().readNBT(LDOHCapabilities.MINI_RAID, instance, null, nbt);
		}

	}

	public static enum RaidType {
		ZOMBIE, ENEMY, ALLY, PARASITE, NONE;
	}

}
