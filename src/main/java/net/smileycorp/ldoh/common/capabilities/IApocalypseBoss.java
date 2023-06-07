package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface IApocalypseBoss {

	public void setPlayer(EntityPlayer player);

	public void onHurt(float amount);

	public NBTTagCompound writeToNBT();

	public void readFromNBT(NBTTagCompound nbt);

	public static class Storage implements IStorage<IApocalypseBoss> {

		@Override
		public NBTBase writeNBT(Capability<IApocalypseBoss> capability, IApocalypseBoss instance, EnumFacing side) {
			return instance.writeToNBT();
		}

		@Override
		public void readNBT(Capability<IApocalypseBoss> capability, IApocalypseBoss instance, EnumFacing side, NBTBase nbt) {
			instance.readFromNBT((NBTTagCompound) nbt);
		}

	}

	public static class ApocalypseBoss implements IApocalypseBoss {

		private EntityPlayer player;

		@Override
		public void setPlayer(EntityPlayer player) {
			this.player = player;
		}

		@Override
		public void onHurt(float amount) {
			if (player != null && player.hasCapability(LDOHCapabilities.APOCALYPSE, null) ) {
				player.getCapability(LDOHCapabilities.APOCALYPSE, null).onBossHurt(this, amount);
			}
		}

		@Override
		public NBTTagCompound writeToNBT() {
			return new NBTTagCompound();
		}

		@Override
		public void readFromNBT(NBTTagCompound nbt) {

		}
	}

	public static class Provider implements ICapabilitySerializable<NBTTagCompound> {

		protected final IApocalypseBoss instance = new ApocalypseBoss();

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == LDOHCapabilities.APOCALYPSE_BOSS;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return capability == LDOHCapabilities.APOCALYPSE_BOSS ? LDOHCapabilities.APOCALYPSE_BOSS.cast(instance) : null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return (NBTTagCompound) LDOHCapabilities.APOCALYPSE_BOSS.getStorage().writeNBT(LDOHCapabilities.APOCALYPSE_BOSS, instance, null);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			LDOHCapabilities.APOCALYPSE_BOSS.getStorage().readNBT(LDOHCapabilities.APOCALYPSE_BOSS, instance, null, nbt);
		}

	}

}
