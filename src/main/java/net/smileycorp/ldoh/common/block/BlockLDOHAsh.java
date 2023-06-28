package net.smileycorp.ldoh.common.block;

import java.util.Random;

import biomesoplenty.common.block.BlockBOPAsh;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLDOHAsh extends BlockBOPAsh {

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(8) == 0) {
			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + random.nextFloat(), pos.getY() + 1.1F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}

}
