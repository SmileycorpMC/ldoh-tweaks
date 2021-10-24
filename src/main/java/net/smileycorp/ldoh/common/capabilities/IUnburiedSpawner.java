package net.smileycorp.ldoh.common.capabilities;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.ldoh.common.ModContent;

import com.Fishmod.mod_LavaCow.entities.tameable.EntityUnburied;

public interface IUnburiedSpawner {

	public boolean canSpawnEntity();

	public void addEntity(EntityUnburied entity);
	
	public void removeEntity(EntityUnburied entity);
	
	public NBTTagCompound writeToNBT(NBTTagList nbt);
	
	public void readFromNBT(NBTTagList nbt);

	public static class Storage implements IStorage<IUnburiedSpawner> {

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
	
	public static class Implementation implements IUnburiedSpawner {

		private final List<EntityUnburied> entities = new ArrayList<EntityUnburied>();
		
		private final EntityPlayer player;
		
		public Implementation(EntityPlayer player) {
			this.player = player;
		}
		
		@Override
		public boolean canSpawnEntity() {
			if (player == null) return false;
			List<EntityUnburied> toRemove = new ArrayList<EntityUnburied>();
			for (EntityUnburied entity : entities) {
				if (entity == null) toRemove.add(entity);
				else if (player.getDistance(entity) > 45 || entity.isDead || entity.isAddedToWorld()) {
					toRemove.add(entity);
					if (entity.isDead)entity.setDead();
				}
			}
			entities.removeAll(toRemove);
			return entities.size() <= 15;
		}

		@Override
		public void addEntity(EntityUnburied entity) {
			entities.add(entity);
		}

		@Override
		public void removeEntity(EntityUnburied entity) {
			entities.remove(entity);
		}

		@Override
		public NBTTagCompound writeToNBT(NBTTagList nbt) {
			for (EntityUnburied entity : entities) {
				if (entity != null) {
					if (!(player.getDistance(entity) > 45 || entity.isDead || entity.isAddedToWorld())) {
						nbt.appendTag(new NBTTagInt(entity.getEntityId()));
					}
				}
			}
			return null;
		}

		@Override
		public void readFromNBT(NBTTagList nbt) {
			if (player!=null)  {
				for (NBTBase tag : nbt) {
					if (tag instanceof NBTTagInt) {
						Entity entity = player.world.getEntityByID(((NBTTagInt) tag).getInt());
						if (entity instanceof EntityUnburied)entities.add((EntityUnburied) entity);
					}
				}
			}
		}

	}
	
	public static class Provider implements ICapabilitySerializable<NBTTagList> {
		
		protected final IUnburiedSpawner instance;
		
		public Provider(EntityPlayer player) {
			instance = new Implementation(player);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == ModContent.UNBURIED_SPAWNER;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return capability == ModContent.UNBURIED_SPAWNER ? ModContent.UNBURIED_SPAWNER.cast(instance) : null;
		}

		@Override
		public NBTTagList serializeNBT() {
			return (NBTTagList) ModContent.UNBURIED_SPAWNER.getStorage().writeNBT(ModContent.UNBURIED_SPAWNER, instance, null);
		}

		@Override
		public void deserializeNBT(NBTTagList nbt) {
			ModContent.UNBURIED_SPAWNER.getStorage().readNBT(ModContent.UNBURIED_SPAWNER, instance, null, nbt);
		}

}
 
}
