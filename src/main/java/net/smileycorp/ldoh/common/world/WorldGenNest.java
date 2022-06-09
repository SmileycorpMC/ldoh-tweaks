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
import biomesoplenty.common.block.BlockBOPDoubleDecoration.Half;
import biomesoplenty.common.block.BlockBOPDoublePlant;
import biomesoplenty.common.block.BlockBOPDoublePlant.DoublePlantType;

import com.dhanantry.scapeandrunparasites.block.BlockInfestedBush;
import com.dhanantry.scapeandrunparasites.block.BlockInfestedBush.EnumType;
import com.dhanantry.scapeandrunparasites.block.BlockParasiteStain;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityFlog;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;

public class WorldGenNest extends WorldGenerator {

	private IBlockState[] blocks = {SRPBlocks.ParasiteStain.getDefaultState().withProperty(BlockParasiteStain.VARIANT, BlockParasiteStain.EnumType.FEELER),
			SRPBlocks.ParasiteStain.getDefaultState().withProperty(BlockParasiteStain.VARIANT, BlockParasiteStain.EnumType.FLESH)};
	@SuppressWarnings("unchecked")
	@Override
	public boolean generate(World world, Random rand, BlockPos base) {
		int centerY = rand.nextInt(10)+ 10;
		//crater
		for (int i = -8; i <= 8; i++) {
			for (int j = -8; j <= 8; j++) {
				for (int k = -8; k <= 8; k++) {
					BlockPos pos = base.up().add(i, j, k);
					double r = pos.getDistance(base.getX(), base.getY()+1, base.getZ());
					if (r<=7.5) world.setBlockToAir(pos);
					else if(r<8.5)world.setBlockState(pos, (rand.nextInt(2) == 0 ? Blocks.NETHERRACK : BOPBlocks.flesh).getDefaultState(), 18);
				}
			}
		}
		//hole
		for (int i = -20; i <= 20 ; i++) {
			for (int k = -20; k <= 20; k++) {
				BlockPos pos = base.add(i, 0, k);
				int top = world.getTopSolidOrLiquidBlock(pos).getY();
				double r = 7.5;
				int x = 0;
				while (pos.getY() < top) {
					double s = pos.getDistance(base.getX(), pos.getY(), base.getZ());
					if (s <= r) {
						world.setBlockToAir(pos);
					} else if (s <= r + 2) {
						world.setBlockState(pos, (rand.nextInt(2) == 0 ? Blocks.NETHERRACK : BOPBlocks.flesh).getDefaultState(), 18);
					}
					pos = pos.up();
					x++;
					if (x%7 == 0) r +=1;
				}
			}
		}
		//nest
		for (int i = -6; i < 7; i++) {
			for (int j = -6; j < 7; j++) {
				for (int k = -6; k < 7; k++) {
					BlockPos pos = base.add(i, j, k);
					double r = pos.getDistance(base.getX(), base.getY(), base.getZ());
					if (r<=5.5) world.setBlockToAir(pos);
					else if (r<=6.5) placeBlock(world, pos);
				}
			}
		}
		//decoration
		for (int i = 0; i < rand.nextInt(6)+8; i++) {
			BlockPos pos = world.getTopSolidOrLiquidBlock(base.add(rand.nextInt(12) - 6, 0, rand.nextInt(12) - 6));
			if (world.getBlockState(pos.down()).getBlock() == SRPBlocks.ParasiteStain) {
				for (int j = 0; j <= rand.nextInt(3); j++) {
					world.setBlockState(pos, SRPBlocks.InfestedBush.getDefaultState().withProperty(BlockInfestedBush.VARIANT, EnumType.SPINE), 18);
				}
			} else if (world.getBlockState(pos.down()).getBlock() == BOPBlocks.flesh) {
				IBlockState eyebulb = BOPBlocks.double_plant.getDefaultState()
						.withProperty(BlockBOPDoublePlant.VARIANT, DoublePlantType.EYEBULB);
				world.setBlockState(pos, eyebulb, 18);
				world.setBlockState(pos.up(), eyebulb.withProperty(BlockBOPDoublePlant.HALF, Half.UPPER), 18);
			}
		}
		//shulkers
		BlockPos floor = base.down(4);
		for (EnumFacing facing : EnumFacing.VALUES) {
			if (facing.getAxis() == EnumFacing.Axis.Y) continue;
			placeLoot(world, floor.offset(facing, 4));
		}
		//grunts
		for (int i = 0; i <  3; i++) {
			EntityFlog grunt = new EntityFlog(world);
			grunt.setPosition(base.getX(), base.getY()-1, base.getZ());
			grunt.enablePersistence();
			world.spawnEntity(grunt);
		}
		//rupters
		for (int i = 0; i <  7; i++) {
			EntityMudo rupter = new EntityMudo(world);
			rupter.setPosition(base.getX(), base.getY()-1, base.getZ());
			rupter.enablePersistence();
			world.spawnEntity(rupter);
		}
		return true;
	}

	private void placeBlock(World world, BlockPos pos) {
		world.setBlockState(pos, blocks[world.rand.nextInt(blocks.length)], 18);
	}

	private void placeLoot(World world, BlockPos pos) {
		world.setBlockState(pos, Blocks.RED_SHULKER_BOX.getDefaultState(), 18);
		((TileEntityLockableLoot) world.getTileEntity(pos)).setLootTable(ModDefinitions.NEST_CRATE, world.rand.nextLong());
	}

}
