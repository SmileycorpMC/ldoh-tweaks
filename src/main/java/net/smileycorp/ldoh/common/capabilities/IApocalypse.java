package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.atlas.api.IOngoingEvent;
import net.smileycorp.ldoh.common.ConfigHandler;

public interface IApocalypse extends IOngoingEvent {

    public void spawnWave(World world);

    public boolean canStart(World world);

    public void startEvent();

    public EntityPlayer getPlayer();

    public void setPlayer(EntityPlayer player);

    public void onBossHurt(IApocalypseBoss capability, float amount);

    public static class Storage implements IStorage<IApocalypse> {

        @Override
        public NBTBase writeNBT(Capability<IApocalypse> capability, IApocalypse instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();
            instance.writeToNBT(nbt);
            return nbt;
        }

        @Override
        public void readNBT(Capability<IApocalypse> capability, IApocalypse instance, EnumFacing side, NBTBase nbt) {
            instance.readFromNBT((NBTTagCompound) nbt);
        }


    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {

        protected final IApocalypse instance;

        public Provider(EntityPlayer player) {
            instance = ConfigHandler.legacyApocalypse ? new LegacyApocalypse(player) : new Apocalypse(player);
        }

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.APOCALYPSE;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.APOCALYPSE ? LDOHCapabilities.APOCALYPSE.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) LDOHCapabilities.APOCALYPSE.getStorage().writeNBT(LDOHCapabilities.APOCALYPSE, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            LDOHCapabilities.APOCALYPSE.getStorage().readNBT(LDOHCapabilities.APOCALYPSE, instance, null, nbt);
        }

    }

}
