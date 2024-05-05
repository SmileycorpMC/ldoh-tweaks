package net.smileycorp.ldoh.integration.tektopia;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.ldoh.common.util.ModUtils;
import net.tangotek.tektopia.Village;
import net.tangotek.tektopia.VillageManager;

public interface IVillageData {

    Village getVillage();

    void setVillage(Village village);

    void setVillage(VillageManager villages);

    boolean shouldHaveVillage();

    boolean hasVillage();

    void readNBT(NBTTagCompound nbt);

    void writeNBT(NBTTagCompound nbt);

    class Storage implements IStorage<IVillageData> {

        @Override
        public NBTBase writeNBT(Capability<IVillageData> capability, IVillageData instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();
            instance.writeNBT(nbt);
            return nbt;
        }

        @Override
        public void readNBT(Capability<IVillageData> capability, IVillageData instance, EnumFacing side, NBTBase nbt) {
            instance.readNBT((NBTTagCompound) nbt);
        }

    }

    class Impl implements IVillageData {

        protected BlockPos villagePos;
        protected Village village;

        @Override
        public Village getVillage() {
            return village;
        }

        @Override
        public void setVillage(Village village) {
            this.village = village;
            villagePos = village.getCenter();
        }

        @Override
        public void setVillage(VillageManager villages) {
            village = villages.getNearestVillage(villagePos, 30);
        }

        @Override
        public boolean shouldHaveVillage() {
            return villagePos != null;
        }

        @Override
        public boolean hasVillage() {
            return village == null ? false : village.isLoaded();
        }

        @Override
        public void readNBT(NBTTagCompound nbt) {
            if (nbt.hasKey("village")) villagePos = ModUtils.readPosFromNBT(nbt.getCompoundTag("village"));
        }

        @Override
        public void writeNBT(NBTTagCompound nbt) {
            nbt.setTag("village", ModUtils.writePosToNBT(villagePos));
        }

    }

    class Provider implements ICapabilitySerializable<NBTTagCompound> {

        protected final IVillageData instance = TektopiaUtils.VILLAGE_DATA.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == TektopiaUtils.VILLAGE_DATA;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == TektopiaUtils.VILLAGE_DATA ? TektopiaUtils.VILLAGE_DATA.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) TektopiaUtils.VILLAGE_DATA.getStorage().writeNBT(TektopiaUtils.VILLAGE_DATA, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            TektopiaUtils.VILLAGE_DATA.getStorage().readNBT(TektopiaUtils.VILLAGE_DATA, instance, null, nbt);
        }

    }

}
