package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.util.Random;

public interface IMiniRaid {

    void enablePostgame();

    boolean shouldSpawnRaid(EntityPlayer player);

    void spawnRaid(EntityPlayer player);

    void spawnRaid(EntityPlayer player, RaidType type, int phase);

    NBTTagCompound writeToNBT(NBTTagCompound nbt);

    void readFromNBT(NBTTagCompound nbt);

    class Storage implements IStorage<IMiniRaid> {

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

    class Provider implements ICapabilitySerializable<NBTTagCompound> {

        protected final IMiniRaid instance = new MiniRaid();

        public Provider() {
        }

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

    enum RaidType {
        ZOMBIE, ENEMY, ALLY, PARASITE, NONE;

        public static RaidType randomType(EntityPlayer player, Random rand) {
            if (player.getTeam().getName().equals("RED") || player.getTeam().getName().equals("BLU")) {
                if (rand.nextInt(3) == 0) return ALLY;
                if (rand.nextInt(2) == 0) return ENEMY;
            }
            return rand.nextInt(2) == 0 ? PARASITE : ZOMBIE;
        }
    }

}
