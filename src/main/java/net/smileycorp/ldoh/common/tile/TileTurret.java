package net.smileycorp.ldoh.common.tile;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.smileycorp.ldoh.common.entity.EntityTurret;

public class TileTurret extends TileAbstractTurret<EntityTurret> implements IInventory {

    @Override
    public EntityTurret createNewEntity() {
        return new EntityTurret(world);
    }

    public NBTTagCompound getDropNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        if (entity != null) nbt.setTag("entity", entity.saveToItem());
        return nbt;
    }

    @Override
    public String getName() {
        return "Sentry Turret";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }


}
