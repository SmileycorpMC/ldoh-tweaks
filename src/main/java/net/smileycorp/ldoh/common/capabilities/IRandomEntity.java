package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface IRandomEntity {

    public int getId();

    public BlockPos getSpawnPosition();

    public Biome getSpawnBiome();

    public String getName();

    public int getHealth();

    public int getMaxHealth();

    public void setEntity(Entity entity);

    public static class RandomEntity implements IRandomEntity {

        private Entity entity;
        private Biome spawn_biome;
        private BlockPos spawn_pos;

        @Override
        public int getId() {
            UUID uuid = this.entity.getUniqueID();
            long uuidLow = uuid.getLeastSignificantBits();
            int id = (int)(uuidLow & 0x7FFFFFFFL);
            return id;
        }

        @Override
        public BlockPos getSpawnPosition() {
            return spawn_pos;
        }

        @Override
        public Biome getSpawnBiome() {
            return spawn_biome;
        }

        @Override
        public String getName() {
            return entity.hasCustomName() ? entity.getCustomNameTag() : null;
        }

        @Override
        public int getHealth() {
            return entity instanceof EntityLiving ? (int) ((EntityLiving) entity).getHealth() : 0;
        }

        @Override
        public int getMaxHealth() {
           return entity instanceof EntityLiving ? (int) ((EntityLiving) entity).getMaxHealth() : 0;
        }

        @Override
        public void setEntity(Entity entity) {
            this.entity = entity;
            spawn_pos = entity.getPosition();
            Biome biome = entity.world.getBiome(spawn_pos);
        }
    }

    public static class Provider implements ICapabilityProvider {

        private final RandomEntity instance = new RandomEntity();

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing) {
            return capability == LDOHCapabilities.RANDOM_ENTITY;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing enumFacing) {
            return capability == LDOHCapabilities.RANDOM_ENTITY ? LDOHCapabilities.RANDOM_ENTITY.cast(instance) : null;
        }
    }
}
