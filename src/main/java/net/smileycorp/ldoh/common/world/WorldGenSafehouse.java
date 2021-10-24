package net.smileycorp.ldoh.common.world;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jds.bibliocraft.blocks.BiblioWoodBlock.EnumWoodType;
import jds.bibliocraft.blocks.BlockSeat;
import net.blay09.mods.cookingforblockheads.block.BlockFridge;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.items.IItemHandler;
import net.smileycorp.ldoh.common.ModContent;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.block.BlockBarbedWire;
import net.smileycorp.ldoh.common.util.EnumAxis;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLog;
import biomesoplenty.common.block.BlockBOPPlanks;

import com.Fishmod.mod_LavaCow.blocks.BlockTombStone;
import com.chaosthedude.realistictorches.blocks.RealisticTorchesBlocks;
import com.mrcrayfish.furniture.blocks.BlockFurniture;
import com.mrcrayfish.furniture.init.FurnitureBlocks;
import com.mrcrayfish.furniture.tileentity.TileEntityTree;
import com.mrcrayfish.guns.block.BlockWorkbench;

public class WorldGenSafehouse extends WorldGenerator {
	
	private LocalDateTime time = LocalDateTime.now();
	
	private boolean isHalloween = false;
	private boolean isChristmas = false;
	private boolean isAprilFools = false;
	
	private BlockPos basepos = null;
	private BlockPos exitpos = null;
	private List<BlockPos> wallpos = new ArrayList<BlockPos>();
	
	private boolean marked = false;
	
	public WorldGenSafehouse() {
		if ((time.getMonth() == Month.OCTOBER && time.getDayOfMonth() >= 24) || (time.getMonth() == Month.SEPTEMBER && time.getDayOfMonth() <= 7)) isHalloween = true;
		else if ((time.getMonth() == Month.DECEMBER && time.getDayOfMonth() >= 18) || (time.getMonth() == Month.JANUARY && time.getDayOfMonth() == 1)) isChristmas = true;
		else if (time.getMonth() == Month.APRIL && time.getDayOfMonth() == 1 || new Random().nextInt(300) == 0) isAprilFools = true;
	}
	
	public boolean markPositions(World world, BlockPos pos, boolean forced) {
		basepos = pos;
		for (int i = -13; i <= 13; i++) {
			for (int k = -13; k <= 13; k++) {
				if (Math.abs(i) == 13 || Math.abs(k) == 13) {
					BlockPos pos0 = world.getTopSolidOrLiquidBlock(pos.east(i).north(k));
					if (!forced) {
						if (pos0.getY() >= pos.getY()+5 || pos0.getY() <= pos.getY()-5) {
							wallpos.clear();
							return false;
						}
					}
					wallpos.add(pos0);
					if (i == 0 && k == 13) {
						exitpos = pos0;
					}
				}
			}
		}
		marked = true;
		return true;
	}
	
	public boolean isMarked() {
		return marked;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos backup) {
		if (!marked) {
			System.out.println("ffs how even");
			markPositions(world, world.getSpawnPoint().down(), true);
		}
		int wh = basepos.getY()+6;
		for (int i = -6; i <= 6; i++) {
			for (int k = -6; k <= 6; k++) {
				BlockPos pos = basepos.east(i).north(k);
				int mi = Math.abs(i);
				int mk = Math.abs(k);
				for (int j = 1; j <= 10; j++) world.setBlockToAir(pos.up(j));
				if (mi <= 5 && mk <= 5) {
					for (int j = 0; j <= 4; j++) {
						if (mi == 5 && mk == 5) {
							world.setBlockState(pos.up(j), BOPBlocks.log_0.getDefaultState()
									.withProperty(((BlockBOPLog)BOPBlocks.log_0).variantProperty, BOPWoods.FIR), 18);
						}
						else if (mi == 5 && mk < 4 && (j == 2 || j == 3)) {
							world.setBlockState(pos.up(j), Blocks.GLASS_PANE.getDefaultState(), 19);
						}
						else if (mi == 5 || mk == 5) {
							setRandomBrick(rand, world, pos.up(j));
						} else {
							world.setBlockToAir(pos.up(j));
						}
					}
					if (mi < 5 && mk < 5) {
						world.setBlockState(pos, BOPBlocks.planks_0.getDefaultState()
							.withProperty(((BlockBOPPlanks)BOPBlocks.planks_0).variantProperty, BOPWoods.FIR), 18);
					}
				}
				if (mi <= 6 && mk <= 6) {
					int rh = 10-mi;
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
					world.setBlockState(pos.up(rh), state0, 18);
					if (!(mi==6 || (mi==5 && mk!=6)))world.setBlockState(pos.up(rh-1), state1, 18);
					if (mk == 5 && rh > 5) {
						for (int j = 5; j < rh; j++) {
							setRandomBrick(rand, world, pos.up(j));
						}
					}
				}
			}
		}
		for (BlockPos pos : wallpos) {
			while (!world.getBlockState(pos.down()).isFullBlock()) pos = pos.down();
			int h = wh - pos.getY();
			for (int j = 0; j <= h; j++) {
				int mi = Math.abs(basepos.getX()-pos.getX());
				int mk = Math.abs(basepos.getZ()-pos.getZ());
				if (j == 0 || (mi == mk)) {
					setRandomBrick(rand, world, pos.up(j));
					if (mi == 0 && basepos.getZ()-pos.getZ() == 13 && exitpos == null) {
						exitpos = pos;
					}
				} else if (j==h) {
					IBlockState state = ModContent.BARBED_WIRE.getDefaultState();
					if (mi == 13) state = state.withProperty(BlockBarbedWire.AXIS, EnumAxis.Z);
					world.setBlockState(pos.up(j), state, 18);
				} else {
					world.setBlockState(pos.up(j), Blocks.IRON_BARS.getDefaultState(), 19);
				}
			} 
		}
		decorateBase(world, rand);
		return true;
	}
	
	private void decorateBase(World world, Random rand) {
		BlockPos pos = basepos.up();
		if (exitpos == null) {
			CrashReport report = CrashReport.makeCrashReport(new Exception("Like actually there's no way for you to be a null value, how the fuck are you crashing here, there's litterally three checks to make sure you can't be null at this point. How the fuck are you null at this point"), "Fuck you I'm crashing the game myself, legitimately how the fuck did you do this.");
			throw new ReportedException(report);
		}
		BlockPos wallpos = pos.south(4);
		world.setBlockState(wallpos.west(4), net.blay09.mods.cookingforblockheads.block.ModBlocks.fridge.getDefaultState().withProperty(BlockFridge.FACING, 
				EnumFacing.NORTH), 18);
		IItemHandler fridgeInv = ((TileFridge) world.getTileEntity(wallpos.west(4))).getCombinedItemHandler();
		LootTableManager manager = world.getLootTableManager();
		for (ItemStack stack : manager.getLootTableFromLocation(ModDefinitions.SAFEHOUSE_FRIDGE).generateLootForPools(rand, new LootContext(0, (WorldServer) world, manager, null, null, null))) {
			while (true) {
				int slot = rand.nextInt(fridgeInv.getSlots());
				if (fridgeInv.isItemValid(slot, stack)) {
					fridgeInv.insertItem(slot, stack, false);
					break;
				}
			}
		}
		world.setBlockState(wallpos.west(3), com.mrcrayfish.guns.init.ModBlocks.WORKBENCH.getDefaultState().withProperty(BlockWorkbench.FACING, EnumFacing.SOUTH), 18);
		for (int i = 1; i <= 2; i++) {
			for (int j = 0; j <= 1; j++) {
				BlockPos chest = wallpos.west(i).up(j);
				world.setBlockState(chest, Blocks.CHEST.getDefaultState(), 19);
				((TileEntityLockableLoot) world.getTileEntity(chest)).setLootTable(ModDefinitions.SAFEHOUSE_CHEST, rand.nextLong());
			}
		}
		world.setBlockState(wallpos, Blocks.CRAFTING_TABLE.getDefaultState(), 18);
		world.setBlockState(wallpos.east(), FurnitureBlocks.DESK_CABINET_SPRUCE.getDefaultState().withProperty(BlockFurniture.FACING, EnumFacing.SOUTH), 18);
		((TileEntityLockableLoot) world.getTileEntity(wallpos.east())).setLootTable(ModDefinitions.SAFEHOUSE_CABINET, rand.nextLong());
		for (int i = 2; i <= 4; i++) {
			world.setBlockState(wallpos.east(i), FurnitureBlocks.TABLE_ANDESITE.getDefaultState(), 19);
		}
		if (isAprilFools) world.setBlockState(wallpos.east(3).north(), FurnitureBlocks.TOILET.getDefaultState().withProperty(BlockFurniture.FACING, EnumFacing.NORTH), 18);
		else world.setBlockState(wallpos.east(3).north(), FurnitureBlocks.MODERN_CHAIR.getDefaultState().withProperty(BlockFurniture.FACING, EnumFacing.NORTH), 18);
		world.setBlockState(pos.north(5).down(), BOPBlocks.planks_0.getDefaultState()
				.withProperty(((BlockBOPPlanks)BOPBlocks.planks_0).variantProperty, BOPWoods.FIR), 18);
		IBlockState door = BOPBlocks.fir_door.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.SOUTH);
		world.setBlockState(pos.north(5), door.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 18);
		world.setBlockState(pos.north(5).up(), door.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 18);
		if (!isHalloween) {
			IBlockState torch = RealisticTorchesBlocks.torchLit.getDefaultState();
			world.setBlockState(wallpos.east(3).up(2), torch.withProperty(BlockTorch.FACING, EnumFacing.NORTH), 18);
			world.setBlockState(wallpos.west(3).up(2), torch.withProperty(BlockTorch.FACING, EnumFacing.NORTH), 18);
			world.setBlockState(pos.add(3, 2, -4), torch.withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 18);
			world.setBlockState(pos.add(-3, 2, -4), torch.withProperty(BlockTorch.FACING, EnumFacing.SOUTH), 18);
		}
		for (int i = -4; i <= -3; i++) {
			for (int j = -4; j <= -3; j++) {
				world.setBlockState(pos.add(i, 0, j), FurnitureBlocks.TABLE_SPRUCE.getDefaultState(), 19);
			}
		}
		world.setBlockState(pos.add(-4, 0, -2), BlockSeat.instance.getDefaultState().withProperty(BlockSeat.WOOD_TYPE, EnumWoodType.SPRUCE), 18);
		world.setBlockState(pos.add(-2, 0, -3), BlockSeat.instance.getDefaultState().withProperty(BlockSeat.WOOD_TYPE, EnumWoodType.SPRUCE), 18);
		world.setBlockState(pos.add(-3, 1, -3), FurnitureBlocks.PLATE.getDefaultState(), 18);	
		if (exitpos.getY() != 0) {
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
		if (isHalloween) placeHalloweenDecorations(world, rand, pos);
		if (isChristmas) placeChristmasDecorations(world, rand, pos);
	}

	private void placeHalloweenDecorations(World world, Random rand, BlockPos pos) {
		world.setBlockState(pos.add(4, 0, -4), Blocks.LIT_PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH), 18);
		int r = rand.nextInt(4);
		BlockPos webPos = r == 0 ? pos.add(4, 3, 4) : r == 1 ? pos.add(4, 3, -4) : r == 2 ? pos.add(-4, 3, -4) : pos.add(-4, 3, 4);
		world.setBlockState(webPos, Blocks.WEB.getDefaultState(), 18);
		BlockPos skullPos = pos.add(4, 1, 4);
		world.setBlockState(skullPos, Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP));
		world.setBlockState(pos.add(1, 1, 4), FurnitureBlocks.CANDLE.getDefaultState(), 18);
		world.setBlockState(pos.add(-4, 1, -4), FurnitureBlocks.CANDLE.getDefaultState(), 18);
		int tx = pos.getX() + 9;
		int tz = pos.getZ() - 9;
		BlockPos tpos = new BlockPos(tx, world.getHeight(tx, tz), tz);
		world.setBlockState(tpos, com.Fishmod.mod_LavaCow.init.Modblocks.TOMBSTONE.getDefaultState().withProperty(BlockTombStone.FACING, EnumFacing.EAST), 18);
	}
	
	private void placeChristmasDecorations(World world, Random rand, BlockPos pos) {
		BlockPos treePos = pos.add(3, 0, -3);
		world.setBlockState(treePos, FurnitureBlocks.TREE_BOTTOM.getDefaultState(), 18);
		decorateTree(world, treePos);
		world.setBlockState(treePos.up(), FurnitureBlocks.TREE_TOP.getDefaultState(), 18);
		decorateTree(world, treePos.up());
		world.setBlockState(pos.add(0, 2, -4), FurnitureBlocks.WREATH.getDefaultState().withProperty(BlockFurniture.FACING, EnumFacing.NORTH), 18);
		for (int i = -6; i <= 6; i++) {
			world.setBlockState(pos.add(6, 2, i), FurnitureBlocks.FAIRY_LIGHT.getDefaultState().withProperty(BlockFurniture.FACING, EnumFacing.WEST), 18);
			world.setBlockState(pos.add(-6, 2, i), FurnitureBlocks.FAIRY_LIGHT.getDefaultState().withProperty(BlockFurniture.FACING, EnumFacing.EAST), 18);
		}
	}
	
	private void decorateTree(World world, BlockPos pos) {
		TileEntityTree tree = (TileEntityTree) world.getTileEntity(pos);
		for (EnumFacing facing : EnumFacing.VALUES) {
			if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) tree.addOrnament(facing, new ItemStack(FurnitureBlocks.FAIRY_LIGHT));
		}
	}

	private void setRandomBrick(Random rand, World world, BlockPos pos) {
		IBlockState state = Blocks.STONEBRICK.getDefaultState();
		int r = rand.nextInt(isHalloween ? 6 : 10);
		if (r == 0) {
			state = state.withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
		} else if (r == 1) {
			state = state.withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
		}
		world.setBlockState(pos, state, 18);
	}

}
