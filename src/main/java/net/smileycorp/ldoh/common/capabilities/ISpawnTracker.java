package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface ISpawnTracker {

    public boolean isSpawned();

    public void setSpawned(boolean isSpawned);

    public static class Storage implements IStorage<ISpawnTracker> {

        @Override
        public NBTBase writeNBT(Capability<ISpawnTracker> capability, ISpawnTracker instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setBoolean("isSpawned", instance.isSpawned());
            return nbt;
        }

        @Override
        public void readNBT(Capability<ISpawnTracker> capability, ISpawnTracker instance, EnumFacing side, NBTBase nbt) {
            instance.setSpawned(((NBTTagCompound) nbt).getBoolean("isSpawned"));
        }


    }

    public static class SpawnTracker implements ISpawnTracker {

        private boolean isSpawned = false;

        @Override
        public boolean isSpawned() {
            return isSpawned;
        }

        @Override
        public void setSpawned(boolean isSpawned) {
            this.isSpawned = isSpawned;
        }

    }

    public class Provider implements ICapabilitySerializable<NBTBase> {

        protected ISpawnTracker instance = LDOHCapabilities.SPAWN_TRACKER.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.SPAWN_TRACKER;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.SPAWN_TRACKER ? LDOHCapabilities.SPAWN_TRACKER.cast(instance) : null;
        }

        @Override
        public NBTBase serializeNBT() {
            return LDOHCapabilities.SPAWN_TRACKER.getStorage().writeNBT(LDOHCapabilities.SPAWN_TRACKER, instance, null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            LDOHCapabilities.SPAWN_TRACKER.getStorage().readNBT(LDOHCapabilities.SPAWN_TRACKER, instance, null, nbt);
        }

    }

}
