package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class AbstractStorage<T> implements Capability.IStorage<T> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<T> capability, T t, EnumFacing enumFacing) {
        return new NBTTagCompound();
    }

    @Override
    public void readNBT(Capability<T> capability, T t, EnumFacing enumFacing, NBTBase nbtBase) {}

}
