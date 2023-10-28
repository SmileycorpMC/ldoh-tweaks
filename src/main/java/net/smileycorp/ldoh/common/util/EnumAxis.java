package net.smileycorp.ldoh.common.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.Vec3d;

public enum EnumAxis implements IStringSerializable {
    X,
    Z;

    @Override
    public String getName() {
        return this.toString().toLowerCase();
    }

    public static EnumAxis fromVector(Vec3d vec) {
        return fromFacing(EnumFacing.getFacingFromVector((float) vec.x, 0f, (float) vec.z));
    }

    public static EnumAxis fromFacing(EnumFacing facing) {
        if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) return Z;
        return X;
    }

}