package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.network.PacketHandler;
import net.smileycorp.ldoh.common.network.SyncSyringesMessage;

public interface ICuring {

    ItemStack tryPickupSyringe(ItemStack stack, EntityLiving entity);

    int getSyringeCount();

    void setSyringeCount(int count);

    void syncClients(EntityLiving entity);

    void syncClient(EntityLiving entity, EntityPlayerMP player);

    void readNBT(NBTTagCompound compound);

    void writeNBT(NBTTagCompound compound);

    class Storage implements IStorage<ICuring> {

        @Override
        public NBTBase writeNBT(Capability<ICuring> capability, ICuring instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();
            instance.writeNBT(nbt);
            return nbt;
        }

        @Override
        public void readNBT(Capability<ICuring> capability, ICuring instance, EnumFacing side, NBTBase nbt) {
            instance.readNBT((NBTTagCompound) nbt);
        }


    }

    class Curing implements ICuring {

        private int syringeCount = 0;

        @Override
        public void readNBT(NBTTagCompound compound) {
            if (compound.hasKey("syringeCount")) syringeCount = compound.getInteger("syringeCount");
        }

        @Override
        public void writeNBT(NBTTagCompound compound) {
            compound.setInteger("syringeCount", syringeCount);
        }

        @Override
        public int getSyringeCount() {
            return syringeCount;
        }

        @Override
        public void setSyringeCount(int count) {
            syringeCount = count;
        }

        @Override
        public ItemStack tryPickupSyringe(ItemStack stack, EntityLiving entity) {
            if (stack.getItem() == LDOHItems.SYRINGE && stack.getMetadata() == 2 & !stack.isEmpty()) {
                if (syringeCount == 0) {
                    syringeCount = stack.getCount();
                    syncClients(entity);
                    return ItemStack.EMPTY;
                } else {
                    int count = stack.getCount() + syringeCount;
                    if (count > 64) {
                        syringeCount = 64;
                        stack.setCount(count - 64);
                    } else {
                        syringeCount = (count);
                        syncClients(entity);
                        return ItemStack.EMPTY;
                    }
                }
            }
            syncClients(entity);
            return stack;
        }

        @Override
        public void syncClients(EntityLiving entity) {
            PacketHandler.NETWORK_INSTANCE.sendToAllTracking(new SyncSyringesMessage(entity, syringeCount), entity);
        }

        @Override
        public void syncClient(EntityLiving entity, EntityPlayerMP player) {
            PacketHandler.NETWORK_INSTANCE.sendTo(new SyncSyringesMessage(entity, syringeCount), player);
        }

    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {

        protected final ICuring instance = LDOHCapabilities.CURING.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.CURING;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.CURING ? LDOHCapabilities.CURING.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) LDOHCapabilities.CURING.getStorage().writeNBT(LDOHCapabilities.CURING, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            LDOHCapabilities.CURING.getStorage().readNBT(LDOHCapabilities.CURING, instance, null, nbt);
        }

    }

}
