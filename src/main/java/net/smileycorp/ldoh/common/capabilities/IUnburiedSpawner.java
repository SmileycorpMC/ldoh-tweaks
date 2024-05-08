package net.smileycorp.ldoh.common.capabilities;

import com.Fishmod.mod_LavaCow.entities.tameable.EntityUnburied;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public interface IUnburiedSpawner {
    
    boolean canSpawnEntity();
    
    boolean isValidLocation(World world, BlockPos pos);

    void addEntity(EntityUnburied entity);

    void removeEntity(EntityUnburied entity);

    NBTTagCompound writeToNBT(NBTTagList nbt);

   void readFromNBT(NBTTagList nbt);

    class Storage implements IStorage<IUnburiedSpawner> {

        @Override
        public NBTBase writeNBT(Capability<IUnburiedSpawner> capability, IUnburiedSpawner instance, EnumFacing side) {
            NBTTagList nbt = new NBTTagList();
            instance.writeToNBT(nbt);
            return nbt;
        }

        @Override
        public void readNBT(Capability<IUnburiedSpawner> capability, IUnburiedSpawner instance, EnumFacing side, NBTBase nbt) {
            instance.readFromNBT((NBTTagList) nbt);
        }


    }

    class UnburiedSpawner implements IUnburiedSpawner {

        private final List<WeakReference<EntityUnburied>> entities = new ArrayList<WeakReference<EntityUnburied>>();

        private final EntityPlayer player;

        public UnburiedSpawner(EntityPlayer player) {
            this.player = player;
        }

        @Override
        public boolean canSpawnEntity() {
            if (player == null) return false;
            List<WeakReference<EntityUnburied>> toRemove = new ArrayList<WeakReference<EntityUnburied>>();
            for (WeakReference<EntityUnburied> ref : entities) {
                EntityUnburied entity = ref.get();
                if (entity == null) toRemove.add(ref);
                else if (player.getDistance(entity) > 45 || entity.isDead || !entity.isAddedToWorld()) {
                    toRemove.add(ref);
                    if (entity.isDead) entity.setDead();
                }
            }
            entities.removeAll(toRemove);
            return entities.size() <= 15;
        }
    
        @Override
        public boolean isValidLocation(World world, BlockPos pos) {
            if (!(world.isAirBlock(pos) && world.isAirBlock(pos.up()))) return false;
            if (world.canBlockSeeSky(pos) || world.getLightBrightness(pos) >= 0.4) return false;
            IBlockState state = world.getBlockState(pos.down());
            if (state == Blocks.STONE.getDefaultState()) return true;
            Block block = state.getBlock();
            return block == Blocks.HARDENED_CLAY || block == Blocks.STONEBRICK;
        }

        @Override
        public void addEntity(EntityUnburied entity) {
            entities.add(new WeakReference(entity));
        }

        @Override
        public void removeEntity(EntityUnburied entity) {
            List<WeakReference<EntityUnburied>> toRemove = Lists.newArrayList();
            for (WeakReference<EntityUnburied> ref : entities) if (entity == ref.get()) toRemove.add(ref);
            entities.removeAll(toRemove);
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagList nbt) {
            for (WeakReference<EntityUnburied> ref : entities) {
                EntityUnburied entity = ref.get();
                if (entity != null &!entity.isDead && entity.isAddedToWorld()) nbt.appendTag(new NBTTagInt(entity.getEntityId()));
            }
            return null;
        }

        @Override
        public void readFromNBT(NBTTagList nbt) {
            if (player == null) return;
            for (NBTBase tag : nbt) {
                if (tag instanceof NBTTagInt) {
                    Entity entity = player.world.getEntityByID(((NBTTagInt) tag).getInt());
                    if (entity instanceof EntityUnburied)
                        entities.add(new WeakReference<EntityUnburied>((EntityUnburied) entity));
                }
            }
        }

    }

    class Provider implements ICapabilitySerializable<NBTTagList> {

        protected final IUnburiedSpawner instance;

        public Provider(EntityPlayer player) {
            instance = new UnburiedSpawner(player);
        }

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.UNBURIED_SPAWNER;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == LDOHCapabilities.UNBURIED_SPAWNER ? LDOHCapabilities.UNBURIED_SPAWNER.cast(instance) : null;
        }

        @Override
        public NBTTagList serializeNBT() {
            return (NBTTagList) LDOHCapabilities.UNBURIED_SPAWNER.getStorage().writeNBT(LDOHCapabilities.UNBURIED_SPAWNER, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagList nbt) {
            LDOHCapabilities.UNBURIED_SPAWNER.getStorage().readNBT(LDOHCapabilities.UNBURIED_SPAWNER, instance, null, nbt);
        }

    }

}
