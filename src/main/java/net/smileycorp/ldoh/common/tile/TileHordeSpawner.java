package net.smileycorp.ldoh.common.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.smileycorp.ldoh.common.block.BlockHordeSpawner;
import net.smileycorp.ldoh.common.util.ModUtils;

public class TileHordeSpawner extends TileEntity implements ITickable {

    private boolean spawned = false;
    private boolean isNatural = false;

    @Override
    public void update() {
        if (!world.isRemote & !spawned) {
            ModUtils.spawnHorde(world, pos, isNatural);
            spawned = true;
            BlockHordeSpawner.breakBlock(world, pos);
        }
    }

    public void setNatural() {
        isNatural = true;
    }

}
