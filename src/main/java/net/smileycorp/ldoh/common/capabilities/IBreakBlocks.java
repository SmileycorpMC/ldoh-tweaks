package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.ldoh.common.ModContent;

public interface IBreakBlocks {

	public boolean canBreakBlocks();
	
	public void enableBlockBreaking(boolean canBreak);

	public static class Storage implements IStorage<IBreakBlocks> {

		@Override
		public NBTBase writeNBT(Capability<IBreakBlocks> capability, IBreakBlocks instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("canBreakBlocks", instance.canBreakBlocks());
			return nbt;
		}
	
		@Override
		public void readNBT(Capability<IBreakBlocks> capability, IBreakBlocks instance, EnumFacing side, NBTBase nbt) {
			instance.enableBlockBreaking(((NBTTagCompound)nbt).getBoolean("canBreakBlocks"));
		}
		
		
	}
	
	public static class Implementation implements IBreakBlocks {
		
		private boolean canBreakBlocks = false;
		
		@Override
		public boolean canBreakBlocks() {
			return canBreakBlocks;
		}

		@Override
		public void enableBlockBreaking(boolean canBreak) {
			canBreakBlocks = canBreak;
		}

	}
	
	public static class Provider implements ICapabilitySerializable<NBTBase> {
		
		protected IBreakBlocks instance = new Implementation();

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == ModContent.BLOCK_BREAKING;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return capability == ModContent.BLOCK_BREAKING ? ModContent.BLOCK_BREAKING.cast(instance) : null;
		}

		@Override
		public NBTBase serializeNBT() {
			return ModContent.BLOCK_BREAKING.getStorage().writeNBT(ModContent.BLOCK_BREAKING, instance, null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt) {
			ModContent.BLOCK_BREAKING.getStorage().readNBT(ModContent.BLOCK_BREAKING, instance, null, nbt);
		}

}
 
}
