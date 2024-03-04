package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.util.Random;

public interface IAmbushEvent {

    void enablePostgame();

    boolean shouldSpawn(EntityPlayer player);

    void spawnAmbush(EntityPlayer player);

    void spawnAmbush(EntityPlayer player, Type type, int phase);

    NBTTagCompound writeToNBT(NBTTagCompound nbt);

    void readFromNBT(NBTTagCompound nbt);

    class Storage implements IStorage<IAmbushEvent> {

        @Override
        public NBTBase writeNBT(Capability<IAmbushEvent> capability, IAmbushEvent instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();
            instance.writeToNBT(nbt);
            return nbt;
        }

        @Override
        public void readNBT(Capability<IAmbushEvent> capability, IAmbushEvent instance, EnumFacing side, NBTBase nbt) {
            instance.readFromNBT((NBTTagCompound) nbt);
        }


    }

    class Provider implements ICapabilitySerializable<NBTTagCompound> {

        protected final IAmbushEvent instance = new AmbushEvent();

        public Provider() {
        }

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.AMBUSH;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.AMBUSH ? LDOHCapabilities.AMBUSH.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) LDOHCapabilities.AMBUSH.getStorage().writeNBT(LDOHCapabilities.AMBUSH, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            LDOHCapabilities.AMBUSH.getStorage().readNBT(LDOHCapabilities.AMBUSH, instance, null, nbt);
        }

    }

    enum Type {
        ZOMBIE, ENEMY, ALLY, PARASITE, MUTANT, NONE;

        public static Type randomType(EntityPlayer player, Random rand) {
            if (player.getTeam().getName().equals("RED") || player.getTeam().getName().equals("BLU")) {
                if (rand.nextInt(3) == 0) return ALLY;
                if (rand.nextInt(4) == 0) return ENEMY;
            }
            return rand.nextInt(3) == 0 ? PARASITE : rand.nextInt(2) == 0 ? ZOMBIE : MUTANT;
        }
    }

}
