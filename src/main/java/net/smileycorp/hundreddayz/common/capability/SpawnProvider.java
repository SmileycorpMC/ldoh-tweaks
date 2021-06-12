package net.smileycorp.hundreddayz.common.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class SpawnProvider implements ICapabilitySerializable<NBTBase> {
		
		@CapabilityInject(ISpawnTracker.class)
		public final static Capability<ISpawnTracker> SPAWN_TRACKER = null;
		
		protected ISpawnTracker instance = SPAWN_TRACKER.getDefaultInstance();

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == SPAWN_TRACKER;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return capability == SPAWN_TRACKER ? SPAWN_TRACKER.cast(instance) : null;
		}

		@Override
		public NBTBase serializeNBT() {
			return SPAWN_TRACKER.getStorage().writeNBT(SPAWN_TRACKER, instance, null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt) {
			SPAWN_TRACKER.getStorage().readNBT(SPAWN_TRACKER, instance, null, nbt);
		}

}
