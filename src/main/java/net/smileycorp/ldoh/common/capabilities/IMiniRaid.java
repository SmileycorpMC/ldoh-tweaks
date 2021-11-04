package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.ldoh.common.ModContent;

public interface IMiniRaid {

	public boolean shouldSpawnRaid();

	public void spawnRaid();

	public NBTTagCompound writeToNBT(NBTTagCompound nbt);

	public void readFromNBT(NBTTagCompound nbt);

	public EntityPlayer getPlayer();

	public void setPlayer(EntityPlayer player);

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

		protected final IMiniRaid instance;

		public Provider(EntityPlayer player) {
			instance = new MiniRaid(player);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == ModContent.MINI_RAID;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return capability == ModContent.MINI_RAID ? ModContent.MINI_RAID.cast(instance) : null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return (NBTTagCompound) ModContent.MINI_RAID.getStorage().writeNBT(ModContent.MINI_RAID, instance, null);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			ModContent.MINI_RAID.getStorage().readNBT(ModContent.MINI_RAID, instance, null, nbt);
		}

	}

	public static enum RaidType {
		ZOMBIE, ENEMY, ALLY, PARASITE, NONE;
	}

}
