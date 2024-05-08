package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface ISpawnTracker {

    boolean isSpawned();

    void setSpawned(boolean isSpawned);
    
    boolean isUpdated(World world);
    
    NBTTagCompound save();
    
    void load(NBTTagCompound nbt);

    class Storage implements IStorage<ISpawnTracker> {
    
        @Override
        public NBTBase writeNBT(Capability<ISpawnTracker> capability, ISpawnTracker instance, EnumFacing side) {
            return instance.save();
        }

        @Override
        public void readNBT(Capability<ISpawnTracker> capability, ISpawnTracker instance, EnumFacing side, NBTBase nbt) {
            instance.load((NBTTagCompound) nbt);
        }

    }

    class SpawnTracker implements ISpawnTracker {

        private boolean isSpawned = false;
        private int lastUpdate = 0;

        @Override
        public boolean isSpawned() {
            return isSpawned;
        }

        @Override
        public void setSpawned(boolean isSpawned) {
            this.isSpawned = isSpawned;
        }
    
        @Override
        public boolean isUpdated(World world) {
            int current = (int)(world.getWorldTime() / 12000);
            if (current > lastUpdate) {
                lastUpdate = current;
                return false;
            }
            return true;
        }
    
        @Override
        public NBTTagCompound save() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setBoolean("isSpawned", isSpawned);
            nbt.setInteger("lastUpdate", lastUpdate);
            return nbt;
        }
    
        @Override
        public void load(NBTTagCompound nbt) {
            if (nbt.hasKey("isSpawned")) isSpawned = nbt.getBoolean("isSpawned");
            if (nbt.hasKey("lastUpdate")) lastUpdate = nbt.getInteger("lastUpdate");
        }
    
    }

    class Provider implements ICapabilitySerializable<NBTBase> {

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
