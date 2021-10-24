package net.smileycorp.ldoh.common.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.smileycorp.ldoh.common.ModDefinitions;

public class WorldDataSafehouse extends WorldSavedData {

	public static final String DATA = ModDefinitions.modid + "_SpawnBase";
	
	protected Boolean isGenerated = false;
	
	private WorldGenSafehouse safehouse = new WorldGenSafehouse();
	
	public WorldDataSafehouse() {
		this(DATA);
	}
	
	public WorldDataSafehouse(String data) {
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
	
	public void save() {
		markDirty();
	}
	
	public WorldGenSafehouse getSafehouse() {
		return safehouse;
	}
	

	public static WorldDataSafehouse get(World world, boolean expectedNew) {
		WorldDataSafehouse data = (WorldDataSafehouse) world.getMapStorage().getOrLoadData(WorldDataSafehouse.class, WorldDataSafehouse.DATA);
		if (data== null) {
			data = new WorldDataSafehouse();
			world.getMapStorage().setData(DATA, data);
			System.out.print("Creating data");
			if (expectedNew) System.out.println("Created new world, data is expected here");
			else System.out.println("You gotta be shitting me, why are you remaking it");
		}
		return data;
	}
	
}
