package net.smileycorp.hundreddayz.common.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class TimeProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ITimeDetector.class)
	public final static Capability<ITimeDetector> TIME_DETECTOR = null;
	
	protected ITimeDetector instance = TIME_DETECTOR.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == TIME_DETECTOR;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == TIME_DETECTOR ? TIME_DETECTOR.cast(instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return TIME_DETECTOR.getStorage().writeNBT(TIME_DETECTOR, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		TIME_DETECTOR.getStorage().readNBT(TIME_DETECTOR, instance, null, nbt);
	}
	
	public static enum EnumTime {
		DAY(0.25d),
		NIGHT(4d);
		
		private double mutliplier;
		
		EnumTime(double multiplier) {
			this.mutliplier=multiplier;
		}
		
		public double getMultiplier() {
			return mutliplier;
		}
	}

}
