package net.smileycorp.ldoh.common.entity.infphoenix;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityInfPhoenixSnow extends EntityInfPhoenix {

    public EntityInfPhoenixSnow(World world) {
        super(world);
    }

    @Override
    public String getName() {
        return "snowy";
    }

    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (world.isRemote) return;
        if (world.getBiome(getPosition()).getTemperature(getPosition()) > 1) attackEntityFrom(DamageSource.ON_FIRE, 1);
        for (int l = 0; l < 4; ++l) {
            int i = MathHelper.floor(posX + (double)((float)(l % 2 * 2 - 1) * 0.25F));
            int j = MathHelper.floor(posY);
            int k = MathHelper.floor(posZ + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));
            BlockPos blockpos = new BlockPos(i, j, k);
            if (world.getBlockState(blockpos).getMaterial() == Material.AIR && world.getBiome(blockpos).getTemperature(blockpos) < 0.8F &&
                    Blocks.SNOW_LAYER.canPlaceBlockAt(world, blockpos)) world.setBlockState(blockpos, Blocks.SNOW_LAYER.getDefaultState());
        }
    }

    @Override
    protected boolean isImmune(DamageSource source) {
        return source == DamageSource.DROWN;
    }


}
