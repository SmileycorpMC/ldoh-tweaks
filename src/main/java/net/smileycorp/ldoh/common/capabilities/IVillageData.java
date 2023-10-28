package net.smileycorp.ldoh.common.capabilities;

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

    public Village getVillage();

    public void setVillage(Village village);

    public void setVillage(VillageManager villages);

    public boolean shouldHaveVillage();

    public boolean hasVillage();

    public void readNBT(NBTTagCompound nbt);

    public void writeNBT(NBTTagCompound nbt);

    public static class Storage implements IStorage<IVillageData> {

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

    public static class VillageData implements IVillageData {

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

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {

        protected final IVillageData instance = LDOHCapabilities.VILLAGE_DATA.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.VILLAGE_DATA;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.VILLAGE_DATA ? LDOHCapabilities.VILLAGE_DATA.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) LDOHCapabilities.VILLAGE_DATA.getStorage().writeNBT(LDOHCapabilities.VILLAGE_DATA, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            LDOHCapabilities.VILLAGE_DATA.getStorage().readNBT(LDOHCapabilities.VILLAGE_DATA, instance, null, nbt);
        }

    }

}
