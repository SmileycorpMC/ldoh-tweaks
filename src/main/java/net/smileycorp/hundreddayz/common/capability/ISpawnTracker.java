package net.smileycorp.hundreddayz.common.capability;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public interface ISpawnTracker {

	public boolean isSpawned();
	
	public void setSpawned(boolean isSpawned);

	public static class Storage implements IStorage<ISpawnTracker> {

		@Override
		public NBTBase writeNBT(Capability<ISpawnTracker> capability, ISpawnTracker instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("isSpawned", instance.isSpawned());
			return nbt;
		}
	
		@Override
		public void readNBT(Capability<ISpawnTracker> capability, ISpawnTracker instance, EnumFacing side, NBTBase nbt) {
				instance.setSpawned(((NBTTagCompound)nbt).getBoolean("isSpawned"));
		}
		
		
	}
	
	public static class Factory implements Callable<ISpawnTracker> {

		  @Override
		  public ISpawnTracker call() throws Exception {
		    return new Capabilty();
		  }
		  
	}
	
	public static class Capabilty implements ISpawnTracker {

		private boolean isSpawned = false;

		@Override
		public boolean isSpawned() {
			return isSpawned;
		}

		@Override
		public void setSpawned(boolean isSpawned) {
			this.isSpawned = isSpawned;
		}

	}
 
}
