package net.smileycorp.hundreddayz.common.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.smileycorp.hundreddayz.common.ModDefinitions;

public class WorldDataSpawnBase extends WorldSavedData {

	public static final String DATA = ModDefinitions.modid + "_SpawnBase";
	
	protected Boolean isGenerated = false;
	
	public WorldDataSpawnBase() {
		this(DATA);
	}
	
	public WorldDataSpawnBase(String data) {
		super(data);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		isGenerated = nbt.getBoolean("isGenerated");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setBoolean("isGenerated", isGenerated);
		return nbt;
	}
	
	public boolean isGenerated() {
		return isGenerated;
	}
	
	public void setGenerated() {
		isGenerated = true;
		markDirty();
	}

	public static WorldDataSpawnBase get(World world) {
		WorldDataSpawnBase data = (WorldDataSpawnBase) world.getMapStorage().getOrLoadData(WorldDataSpawnBase.class, WorldDataSpawnBase.DATA);
		if (data== null) {
			data = new WorldDataSpawnBase();
			world.getMapStorage().setData(WorldDataSpawnBase.DATA, data);
		}
		return data;
	}
	
	
}
