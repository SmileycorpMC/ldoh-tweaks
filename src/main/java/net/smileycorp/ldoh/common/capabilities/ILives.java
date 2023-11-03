package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface ILives {

    int getLives();

    void setLives(int lives);

    class Lives implements ILives {

        private int lives = 3;

        @Override
        public int getLives() {
            return lives;
        }

        @Override
        public void setLives(int lives) {
            this.lives = lives;
        }

    }

    class Storage implements Capability.IStorage<ILives> {

        @Override
        public NBTBase writeNBT(Capability<ILives> capability, ILives instance, EnumFacing side) {
            return new NBTTagByte((byte) instance.getLives());
        }

        @Override
        public void readNBT(Capability<ILives> capability, ILives instance, EnumFacing side, NBTBase nbt) {
            instance.setLives(((NBTTagByte)nbt).getByte());
        }

    }

    class Provider implements ICapabilitySerializable<NBTTagByte> {

        protected final ILives instance;

        public Provider() {
            instance = new Lives();
        }

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.LIVES;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.LIVES ? LDOHCapabilities.LIVES.cast(instance) : null;
        }

        @Override
        public NBTTagByte serializeNBT() {
            return (NBTTagByte) LDOHCapabilities.LIVES.getStorage().writeNBT(LDOHCapabilities.LIVES, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagByte nbt) {
            LDOHCapabilities.LIVES.getStorage().readNBT(LDOHCapabilities.LIVES, instance, null, nbt);
        }

    }

}
