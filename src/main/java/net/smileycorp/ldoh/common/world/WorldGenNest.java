package net.smileycorp.ldoh.common.world;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.ldoh.common.ModDefinitions;
import biomesoplenty.api.block.BOPBlocks;

import com.dhanantry.scapeandrunparasites.block.BlockParasiteStain;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityFlog;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import com.mrcrayfish.furniture.init.FurnitureBlocks;

public class WorldGenNest extends WorldGenerator {

	private IBlockState[] blocks = {BOPBlocks.flesh.getDefaultState(),
			SRPBlocks.ParasiteStain.getDefaultState().withProperty(BlockParasiteStain.VARIANT, BlockParasiteStain.EnumType.FLESH)};

	@Override
	public boolean generate(World world, Random rand, BlockPos center) {
		for (int i = -4; i < 5; i++) {
			for (int j = -4; j < 5; j++) {
				for (int k = -4; k < 45; k++) {
					BlockPos pos = center.add(i, j, k);
					double r = pos.getDistance(center.getX(), center.getY(), center.getZ());
					if (r<=4) placeBlock(world, pos);
				}
			}
		}
		for (int i = -4; i < 5; i++) {
			for (int j = -4; j < 5; j++) {
				for (int k = -4; k < 45; k++) {
					BlockPos pos = center.add(i, j, k);
					double r = pos.getDistance(center.getX(), center.getY(), center.getZ());
					if (r<=3) world.setBlockState(pos, Blocks.AIR.getDefaultState(), 18);
				}
			}
		}
		BlockPos floor = center.down(3);
		for (EnumFacing facing : EnumFacing.VALUES) {
			if (facing.getAxis() == EnumFacing.Axis.Y) continue;
			world.setBlockState(floor.offset(facing), FurnitureBlocks.CRATE_DARK_OAK.getDefaultState(), 18);
			((TileEntityLockableLoot) world.getTileEntity(floor.offset(facing))).setLootTable(ModDefinitions.NEST_CRATE, world.rand.nextLong());
		}
		for (int i = 0; i < rand.nextInt(3) + 2; i++) {
			EntityFlog grunt = new EntityFlog(world);
			grunt.setPosition(center.getX(), center.getY()-1, center.getZ());
			grunt.enablePersistence();
			world.spawnEntity(grunt);
		}
		return true;
	}

	private void placeBlock(World world, BlockPos pos) {
		world.setBlockState(pos, blocks[world.rand.nextInt(blocks.length)], 18);
	}

}
