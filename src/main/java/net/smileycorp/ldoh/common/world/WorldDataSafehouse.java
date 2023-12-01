package net.smileycorp.ldoh.common.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.smileycorp.hordes.common.Constants;
import net.smileycorp.ldoh.common.ConfigHandler;

import java.util.Random;

public class WorldDataSafehouse extends WorldSavedData {

    public static final String DATA = Constants.modid + "_Safehouse";

    private boolean generated = true;
    private boolean basementHidden = true;
    private WorldGenSafehouse safehouse = new WorldGenSafehouse();

    public WorldDataSafehouse() {
        this(DATA);
    }

    public WorldDataSafehouse(String data) {
        super(data);
    }

    public boolean isGenerated() {
        return generated;
    }

    public void generate(World world) {
        BlockPos spawn = world.getSpawnPoint();
        world.getEntities(Entity.class, e -> !(e instanceof EntityPlayer) && spawn.distanceSqToCenter(e.posX, spawn.getY(), e.posZ) < 225)
                .forEach(world::removeEntity);
        Random rand = world.rand;
        if (!ConfigHandler.noSafehouse) {
            if (!safehouse.isMarked()) {
                safehouse.markPositions(world, spawn.down(), true);
            }
            safehouse.generate(world, rand, spawn.down());
        }
        generated = true;
        markDirty();
    }

    public void hideBasement(World world) {
        if (basementHidden) return;
        safehouse.hideBasement(world);
        basementHidden = true;
    }

    public boolean isMarked() {
        return safehouse.isMarked();
    }

    public boolean markPositions(World world, BlockPos pos, boolean forced) {
        if (generated = true) generated = false;
        markDirty();
        return safehouse.markPositions(world, pos, forced);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("generated")) generated = nbt.getBoolean("generated");
        if (nbt.hasKey("basementHidden")) basementHidden = nbt.getBoolean("basementHidden");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("generated", generated);
        nbt.setBoolean("basementHidden", basementHidden);
        return nbt;
    }

    public static WorldDataSafehouse getData(World world) {
        WorldDataSafehouse data = (WorldDataSafehouse) world.getMapStorage().getOrLoadData(WorldDataSafehouse.class, DATA);
        if (data == null) {
            data = new WorldDataSafehouse();
            world.getMapStorage().setData(DATA, data);
            data.markDirty();
        }
        return data;
    }

}
