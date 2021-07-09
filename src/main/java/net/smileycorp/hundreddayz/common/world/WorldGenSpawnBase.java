package net.smileycorp.hundreddayz.common.world;

import java.util.Random;

import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.hundreddayz.common.ModContent;
import net.smileycorp.hundreddayz.common.ModDefinitions;
import net.smileycorp.hundreddayz.common.block.BlockBarbedWire;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLog;
import biomesoplenty.common.block.BlockBOPPlanks;

import com.chaosthedude.realistictorches.blocks.RealisticTorchesBlocks;
import com.mrcrayfish.furniture.blocks.BlockDeskCabinet;
import com.mrcrayfish.furniture.init.FurnitureBlocks;
import com.mrcrayfish.guns.block.BlockWorkbench;
import com.mrcrayfish.guns.init.ModBlocks;

public class WorldGenSpawnBase extends WorldGenerator {

	@Override
	public boolean generate(World world, Random rand, BlockPos basepos) {
		BlockPos exitpos = basepos;
		for (int i = -13; i <= 13; i++) {
			for (int k = -13; k <= 13; k++) {
				BlockPos pos = basepos.east(i).north(k);
				int mi = Math.abs(i);
				int mk = Math.abs(k);
				if (mi <= 5 && mk <= 5) {
					for (int j = 0; j <= 4; j++) {
						if (mi == 5 && mk == 5) {
							world.setBlockState(pos.up(j), BOPBlocks.log_2.getDefaultState()
									.withProperty(((BlockBOPLog)BOPBlocks.log_2).variantProperty, BOPWoods.PINE), 18);
						}
						else if (mi == 5 && mk < 4 && (j == 2 || j == 3)) {
							world.setBlockState(pos.up(j), Blocks.GLASS_PANE.getDefaultState(), 19);
						}
						else if (mi == 5 || mk == 5) {
							world.setBlockState(pos.up(j), BOPBlocks.planks_0.getDefaultState()
									.withProperty(((BlockBOPPlanks)BOPBlocks.planks_0).variantProperty, BOPWoods.FIR), 18);
						} else {
							world.setBlockToAir(pos.up(j));
						}
					}
					setRandomBrick(rand, world, pos);
				}
				else if (mi == 13 || mk == 13) {
					pos = world.getTopSolidOrLiquidBlock(pos);
					for (int j = 0; j <= 6; j++) {
						if (j == 0 || (mi == mk)) {
							if (mi == 0 && k == 13) {
								exitpos = pos.up(j);
							}
							setRandomBrick(rand, world, pos.up(j));
						} else if (j==6) {
							IBlockState state = ModContent.BARBED_WIRE.getDefaultState();
							if (mi == 13) state = state.withProperty(BlockBarbedWire.AXIS, BlockBarbedWire.EnumBarbedWireAxis.Z);
							world.setBlockState(pos.up(j), state, 18);
						} else {
							world.setBlockState(pos.up(j), Blocks.IRON_BARS.getDefaultState(), 19);
						}
					} 
				}
				if (mi <= 6 && mk <= 6) {
					int h = 10-mi;
					IBlockState state0 = Blocks.SPRUCE_STAIRS.getDefaultState();
					IBlockState state1 = state0.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP);
					if (i == 0) {
						state0 = Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE);
						state1 = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE);
					} else if (i < 0) {
						state0 = state0.withProperty(BlockStairs.FACING, EnumFacing.EAST);
						state1 = state1.withProperty(BlockStairs.FACING, EnumFacing.WEST);
					} else {
						state0 = state0.withProperty(BlockStairs.FACING, EnumFacing.WEST);
						state1 = state1.withProperty(BlockStairs.FACING, EnumFacing.EAST);
					}
					world.setBlockState(pos.up(h), state0, 18);
					if (mi!=6)world.setBlockState(pos.up(h-1), state1, 18);
					if (mk == 5 && h > 5) {
						for (int j = 5; j < h; j++) {
							world.setBlockState(pos.up(j), BOPBlocks.planks_0.getDefaultState()
									.withProperty(((BlockBOPPlanks)BOPBlocks.planks_0).variantProperty, BOPWoods.FIR), 18);
						}
					}
				}
			}
		}
		decorateBase(world, rand, basepos.up(), exitpos);
		return false;
	}
	
	private void decorateBase(World world, Random rand, BlockPos pos, BlockPos exitpos) {
		BlockPos wallpos = pos.south(4);
		world.setBlockState(wallpos.west(4), Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, 
				BlockStoneBrick.EnumType.CHISELED), 18);
		world.setBlockState(wallpos.west(3), ModBlocks.WORKBENCH.getDefaultState().withProperty(BlockWorkbench.FACING, EnumFacing.SOUTH), 18);
		for (int i = 1; i <= 2; i++) {
			for (int j = 0; j <= 1; j++) {
				BlockPos chest = wallpos.west(i).up(j);
				world.setBlockState(chest, Blocks.CHEST.getDefaultState(), 19);
				((TileEntityLockableLoot) world.getTileEntity(chest)).setLootTable(ModDefinitions.SPAWN_CHEST, rand.nextLong());
			}
		}
		world.setBlockState(wallpos, Blocks.CRAFTING_TABLE.getDefaultState(), 18);
		world.setBlockState(wallpos.east(), FurnitureBlocks.DESK_CABINET_SPRUCE.getDefaultState().withProperty(BlockDeskCabinet.FACING, EnumFacing.SOUTH), 18);
		((TileEntityLockableLoot) world.getTileEntity(wallpos.east())).setLootTable(ModDefinitions.SPAWN_CABINET, rand.nextLong());
		for (int i = 2; i <= 4; i++) {
			world.setBlockState(wallpos.east(i), FurnitureBlocks.TABLE_ANDESITE.getDefaultState(), 19);
		}
		IBlockState door = BOPBlocks.fir_door.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.SOUTH);
		world.setBlockState(pos.north(5), door.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 18);
		world.setBlockState(pos.north(5).up(), door.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 18);
		IBlockState torch = RealisticTorchesBlocks.torchLit.getDefaultState();
		world.setBlockState(wallpos.east(3).up(2), torch.withProperty(BlockTorch.FACING, EnumFacing.NORTH), 18);
		world.setBlockState(wallpos.west(3).up(2), torch.withProperty(BlockTorch.FACING, EnumFacing.NORTH), 18);
		world.setBlockState(pos.add(3, 2, -4), torch.withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 18);
		world.setBlockState(pos.add(-3, 2, -4), torch.withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 18);
		for (int i = -1; i<2; i++) {
			for (int j = 0; j<=2; j++) {
				setRandomBrick(rand, world, exitpos.east(i).up(j));
			}
		}
		door = Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.SOUTH);
		world.setBlockState(exitpos, door.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 18);
		world.setBlockState(exitpos.up(), door.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 18);
		IBlockState button = Blocks.STONE_BUTTON.getDefaultState();
		world.setBlockState(exitpos.up().east().north(), button.withProperty(BlockButton.FACING, EnumFacing.NORTH), 18);
		world.setBlockState(exitpos.up().east().south(), button.withProperty(BlockButton.FACING, EnumFacing.SOUTH), 18);
	}

	private static void setRandomBrick(Random rand, World world, BlockPos pos) {
		IBlockState state = Blocks.STONEBRICK.getDefaultState();
		int r = rand.nextInt(10);
		if (r == 0) {
			state = state.withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
		} else if (r == 1) {
			state = state.withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
		}
		world.setBlockState(pos, state, 18);
	}

}
