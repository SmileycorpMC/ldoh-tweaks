package net.smileycorp.hundreddayz.common.capability;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.smileycorp.hundreddayz.common.capability.TimeProvider.EnumTime;

public interface ITimeDetector {

	public EnumTime getTime();
	
	public void setTime(EnumTime time);

	public static class Storage implements IStorage<ITimeDetector> {

		@Override
		public NBTBase writeNBT(Capability<ITimeDetector> capability, ITimeDetector instance, EnumFacing side) {
			return new NBTTagString(instance.getTime().toString());
		}
	
		@Override
		public void readNBT(Capability<ITimeDetector> capability, ITimeDetector instance, EnumFacing side, NBTBase nbt) {
			instance.setTime(EnumTime.valueOf(((NBTTagString)nbt).getString()));
		}
		
		
	}
	
	public static class Factory implements Callable<ITimeDetector> {

		  @Override
		  public ITimeDetector call() throws Exception {
		    return new Capabilty();
		  }
		  
	}
	
	public static class Capabilty implements ITimeDetector {
		
		private EnumTime time = EnumTime.DAY;
		
		@Override
		public EnumTime getTime() {
			return time;
		}

		@Override
		public void setTime(EnumTime time) {
			this.time = time;
		}

		
	}
 
}
