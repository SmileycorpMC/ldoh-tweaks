package net.smileycorp.ldoh.common.world;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLog;
import biomesoplenty.common.block.BlockBOPPlanks;
import com.chaosthedude.realistictorches.blocks.RealisticTorchesBlocks;
import com.mrcrayfish.furniture.blocks.BlockDigitalClock;
import com.mrcrayfish.furniture.blocks.BlockFurniture;
import com.mrcrayfish.furniture.init.FurnitureBlocks;
import com.mrcrayfish.furniture.tileentity.TileEntityTree;
import com.mrcrayfish.guns.block.BlockWorkbench;
import mariot7.xlfoodmod.init.BlockListxlfoodmod;
import net.blay09.mods.cookingforblockheads.block.BlockFridge;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.minecraft.block.*;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockStairs.EnumShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.items.IItemHandler;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.TimedEvents;
import net.smileycorp.ldoh.common.block.BlockBarbedWire;
import net.smileycorp.ldoh.common.block.LDOHBlocks;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;
import net.smileycorp.ldoh.common.tile.TileFilingCabinet;
import net.smileycorp.ldoh.common.util.EnumAxis;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenSafehouse extends WorldGenerator {

	private LocalDateTime time = LocalDateTime.now();

	private boolean isSilly = false;
	private boolean isAnniversary = false;
	private String[] anniversaryMessage = {"", "", "", ""};

	private Block[] cakes = {Blocks.CAKE, BlockListxlfoodmod.chocolate_cake, BlockListxlfoodmod.oreo_cake, BlockListxlfoodmod.cheese_cake, BlockListxlfoodmod.strawberry_cake, BlockListxlfoodmod.pumpkin_cake};

	private BlockPos basepos = null;
	private BlockPos exitpos = null;
	private List<BlockPos> wallpos = new ArrayList<BlockPos>();
	private List<BlockPos> heightmap = new ArrayList<BlockPos>();

	private boolean marked = false;
	private boolean generated = false;

	public WorldGenSafehouse() {
		if ((time.getMonth() == Month.APRIL && time.getDayOfMonth() == 1) || new Random().nextInt(256) == 0) isSilly = true;
		else if ((time.getMonth() == Month.MAY && time.getDayOfMonth() == 21)) {
			isAnniversary = true;
			int years = time.getYear() - 2021;
			anniversaryMessage[1] = "Happy " + years + " " + (years == 1 ? "year" : "years");
			anniversaryMessage[2] = "LDOH First Alpha";
		}
		else if (time.getMonth() == Month.JUNE && time.getDayOfMonth() == 2) {
			isAnniversary = true;
			int years = time.getYear() - 2021;
			anniversaryMessage[0] = "Happy " + years + " " + (years == 1 ? "year" : "years");
			anniversaryMessage[1] = "LDOH First";
			anniversaryMessage[2] = "Public Release";
		}
	}

	public boolean markPositions(World world, BlockPos pos, boolean forced) {
		basepos = pos;
		for (int i = -13; i <= 13; i++) {
			for (int k = -13; k <= 13; k++) {
				if (Math.abs(i) == 13 || Math.abs(k) == 13) {
					BlockPos hpos = world.getTopSolidOrLiquidBlock(pos.east(i).north(k));
					while (world.getBlockState(hpos).getBlock() != Blocks.STONE)  {
						hpos = hpos.down();
					}
					hpos = hpos.up(2);
					if (!forced) {
						if (hpos.getY() >= pos.getY()+5 || hpos.getY() <= pos.getY()-5) {
							wallpos.clear();
							heightmap.clear();
							return false;
						}
					}
					wallpos.add(hpos);
					if (i == 0 && k == 13) {
						exitpos = hpos;
					}
				} else {
					BlockPos hpos = world.getTopSolidOrLiquidBlock(pos.east(i).north(k));
					while (world.getBlockState(hpos).getBlock() != Blocks.STONE)  {
						hpos = hpos.down();
					}
					heightmap.add(hpos.up(2));
				}
			}
		}
		marked = true;
		return true;
	}

	public boolean isMarked() {
		return marked;
	}

	public boolean isGenerated() {
		return generated;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean generate(World world, Random rand, BlockPos backup) {
		if (!marked) {
			markPositions(world, world.getSpawnPoint().down(), true);
		}
		int wh = basepos.getY()+6;
		//clear area of structures
		for (BlockPos pos : heightmap) {
			for (int j = 0; j <= (100 - pos.getY()); j++) {
				setBlockAndNotifyAdequately(world, pos.up(j), Blocks.AIR.getDefaultState());
			}
		}
		//clear room area
		for (int i = -5; i <= 5; i++) {
			for (int k = -1; k <= 5; k++) {
				for (int j = 1; j <= 6; j++) {
					BlockPos pos = basepos.east(i).south(k).up(j);
					setBlockAndNotifyAdequately(world, pos, Blocks.AIR.getDefaultState());
				}
			}
		}
		for (int i = -1; i >= -5; i--) {
			for (int k = -1; k >= -5; k--) {
				for (int j = 1; j <= 6; j++) {
					BlockPos pos = basepos.east(i).south(k).up(j);
					setBlockAndNotifyAdequately(world, pos, Blocks.AIR.getDefaultState());
				}
			}
		}
		//floor
		for (int i = -5; i <= 5; i++) {
			for (int k = -1; k <= 5; k++) {
				BlockPos pos = basepos.east(i).south(k);
				setBlockAndNotifyAdequately(world, pos, BOPBlocks.planks_0.getDefaultState()
						.withProperty(((BlockBOPPlanks)BOPBlocks.planks_0).variantProperty, BOPWoods.FIR) );
				pos = pos.down();
				while (world.getBlockState(pos).getBlock() == Blocks.AIR) {
					setBlockAndNotifyAdequately(world, pos, Blocks.DIRT.getDefaultState());
					pos = pos.down();
				}
			}
		}
		for (int i = -1; i >= -5; i--) {
			for (int k = -1; k >= -5; k--) {
				BlockPos pos = basepos.east(i).south(k);
				setBlockAndNotifyAdequately(world, pos, BOPBlocks.planks_0.getDefaultState()
						.withProperty(((BlockBOPPlanks)BOPBlocks.planks_0).variantProperty, BOPWoods.FIR) );
				pos = pos.down();
				while (world.getBlockState(pos).getBlock() == Blocks.AIR) {
					setBlockAndNotifyAdequately(world, pos, Blocks.DIRT.getDefaultState());
					pos = pos.down();
				}
			}
		}
		//south wall
		for (int i = -5; i <= 5; i++) {
			for (int j = 1; j <= 4; j++) {
				BlockPos pos = basepos.east(i).south(5).up(j);
				int mi = Math.abs(i);
				if (mi == 5) setBlockAndNotifyAdequately(world, pos, BOPBlocks.log_0.getDefaultState()
						.withProperty(((BlockBOPLog)BOPBlocks.log_0).variantProperty, BOPWoods.FIR) );
				else if (j > 1 && j < 4 && mi < 4) setBlockAndNotifyAdequately(world, pos, Blocks.GLASS_PANE.getDefaultState() );
				else setRandomBrick(rand, world, pos);
			}
		}
		//south east wall
		for (int k = -1; k <= 4; k++) {
			for (int j = 1; j <= 4; j++) {
				BlockPos pos = basepos.east(5).south(k).up(j);
				if (k == -1) setBlockAndNotifyAdequately(world, pos, BOPBlocks.log_0.getDefaultState()
						.withProperty(((BlockBOPLog)BOPBlocks.log_0).variantProperty, BOPWoods.FIR) );
				else if (j > 1 && j < 4 && k > 0 && k < 4 ) setBlockAndNotifyAdequately(world, pos, Blocks.GLASS_PANE.getDefaultState() );
				else setRandomBrick(rand, world, pos);
			}
		}
		//east north wall
		for (int i = -1; i <= 4; i++) {
			for (int j = 1; j <= 4; j++) {
				BlockPos pos = basepos.east(i).south(-1).up(j);
				if (i == -1) setBlockAndNotifyAdequately(world, pos, BOPBlocks.log_0.getDefaultState()
						.withProperty(((BlockBOPLog)BOPBlocks.log_0).variantProperty, BOPWoods.FIR) );
				else setRandomBrick(rand, world, pos);
			}
		}
		//north east wall
		for (int k = -2; k >= -5; k--) {
			for (int j = 1; j <= 4; j++) {
				BlockPos pos = basepos.east(-1).south(k).up(j);
				if (k == -5) setBlockAndNotifyAdequately(world, pos, BOPBlocks.log_0.getDefaultState()
						.withProperty(((BlockBOPLog)BOPBlocks.log_0).variantProperty, BOPWoods.FIR) );
				else if (k == -3 && j <=3 && j >=2) setBlockAndNotifyAdequately(world, pos, Blocks.GLASS_PANE.getDefaultState() );
				else setRandomBrick(rand, world, pos);
			}
		}
		//west north wall
		for (int i = -2; i >= -5; i--) {
			for (int j = 1; j <= 4; j++) {
				BlockPos pos = basepos.east(i).south(-5).up(j);
				if (i == -5) setBlockAndNotifyAdequately(world, pos, BOPBlocks.log_0.getDefaultState()
						.withProperty(((BlockBOPLog)BOPBlocks.log_0).variantProperty, BOPWoods.FIR) );
				else if (i == -3 && j <=3 && j >=2) setBlockAndNotifyAdequately(world, pos, Blocks.GLASS_PANE.getDefaultState() );
				else setRandomBrick(rand, world, pos);
			}
		}
		//west wall
		for (int k = -4; k <= 4; k++) {
			for (int j = 1; j <= 4; j++) {
				BlockPos pos = basepos.east(-5).south(k).up(j);
				if (j > 1 && j < 4 && Math.abs(k) < 4) setBlockAndNotifyAdequately(world, pos, Blocks.GLASS_PANE.getDefaultState() );
				else setRandomBrick(rand, world, pos);
			}
		}
		//outer wall
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
					IBlockState state = LDOHBlocks.BARBED_WIRE.getDefaultState();
					if (mi == 13) state = state.withProperty(BlockBarbedWire.AXIS, EnumAxis.Z);
					setBlockAndNotifyAdequately(world, pos.up(j), state );
					TileBarbedWire te = new TileBarbedWire();
					world.setTileEntity(pos.up(j), te);
					te.damage(rand.nextInt(150));
				} else {
					setBlockAndNotifyAdequately(world, pos.up(j), Blocks.IRON_BARS.getDefaultState() );
				}
			}
		}
		//decorations
		decorateBase(world, rand);

		IBlockState stairs = Blocks.SPRUCE_STAIRS.getDefaultState();
		//north roof
		for (int i = -1; i <= 6; i++) {
			for (int k = -2; k <= 1; k++) {
				BlockPos pos = basepos.south(k).east(i).up(4 + k+2);
				IBlockState state = world.getBlockState(pos);
				if (state.getBlock() == Blocks.AIR || state.getBlock() == Blocks.SPRUCE_STAIRS) {
					setBlockAndNotifyAdequately(world, pos, stairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
				}
				pos = pos.down();
				state = world.getBlockState(pos);
				if ((state.getBlock() == Blocks.AIR || state.getBlock() == Blocks.SPRUCE_STAIRS) && k != -2) {
					if (i == 5) {
						while (state.getBlock() == Blocks.AIR) {
							setRandomBrick(rand, world, pos);
							pos = pos.down();
							state = world.getBlockState(pos);
						}
					}
					else {
						setBlockAndNotifyAdequately(world, pos, stairs.withProperty(BlockStairs.HALF, EnumHalf.TOP));
					}
				}
			}
		}
		for (int k = 0; k <= 1; k++) {
			setBlockAndNotifyAdequately(world, basepos.up(6+k).west(2).south(k), stairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
			setBlockAndNotifyAdequately(world, basepos.up(5+k).west(2).south(k), stairs.withProperty(BlockStairs.HALF, EnumHalf.TOP)
					.withProperty(BlockStairs.HALF, EnumHalf.TOP));
		}
		//east roof
		for (int i = -2; i <= 0; i++) {
			for (int k = -6; k <= -1; k++) {
				BlockPos pos = basepos.south(k).east(i).up(4 - i);
				IBlockState state = world.getBlockState(pos);
				if (state.getBlock() == Blocks.AIR) {
					setBlockAndNotifyAdequately(world, pos, stairs.withProperty(BlockStairs.FACING, EnumFacing.WEST));
				}
				pos = pos.down();
				state = world.getBlockState(pos);
				if (state.getBlock() == Blocks.AIR && i != 0) {
					if (k == -5) {
						while (state.getBlock() == Blocks.AIR) {
							setRandomBrick(rand, world, pos);
							pos = pos.down();
							state = world.getBlockState(pos);
						}
					}
					else {
						setBlockAndNotifyAdequately(world, pos, stairs.withProperty(BlockStairs.HALF, EnumHalf.TOP)
								.withProperty(BlockStairs.FACING, EnumFacing.EAST));
					}
				}
			}
		}
		//west roof
		for (int i = -6; i <= -4; i++) {
			for (int k = -6; k <= 4; k++) {
				BlockPos pos = basepos.south(k).east(i).up(10 + i);
				IBlockState state = world.getBlockState(pos);
				if (state.getBlock() == Blocks.AIR) {
					setBlockAndNotifyAdequately(world, pos, stairs.withProperty(BlockStairs.FACING, EnumFacing.EAST));
				}
				pos = pos.down();
				state = world.getBlockState(pos);
				if (state.getBlock() == Blocks.AIR && i != -6) {
					if (Math.abs(k) == 5) {
						while (state.getBlock() == Blocks.AIR) {
							setRandomBrick(rand, world, pos);
							pos = pos.down();
							state = world.getBlockState(pos);
						}
					}
					else {
						setBlockAndNotifyAdequately(world, pos, stairs.withProperty(BlockStairs.HALF, EnumHalf.TOP)
								.withProperty(BlockStairs.FACING, EnumFacing.WEST));
					}
				}
			}
		}
		setBlockAndNotifyAdequately(world, basepos.up(4).west(6).south(5), stairs.withProperty(BlockStairs.FACING, EnumFacing.EAST));
		for (int k = 1; k <= 3; k++) {
			setBlockAndNotifyAdequately(world, basepos.up(7).west(3).south(k), stairs.withProperty(BlockStairs.FACING, EnumFacing.EAST));
			setBlockAndNotifyAdequately(world, basepos.up(6).west(3).south(k), stairs.withProperty(BlockStairs.FACING, EnumFacing.WEST)
					.withProperty(BlockStairs.HALF, EnumHalf.TOP));
		}
		//south roof
		for (int i = -3; i <= 6; i++) {
			for (int k = 3; k <= 6; k++) {
				BlockPos pos = basepos.south(k).east(i).up(10-k);
				IBlockState state = world.getBlockState(pos);
				if (state.getBlock() == Blocks.AIR) {
					setBlockAndNotifyAdequately(world, pos, stairs);
				}
				pos = pos.down();
				state = world.getBlockState(pos);
				if ((state.getBlock() == Blocks.AIR) && k != 6) {
					if (Math.abs(i) == 5) {
						while (state.getBlock() == Blocks.AIR) {
							setRandomBrick(rand, world, pos);
							pos = pos.down();
							state = world.getBlockState(pos);
						}
					}
					else {
						setBlockAndNotifyAdequately(world, pos, stairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, EnumHalf.TOP));
					}
				}
			}
		}
		for (int i = -6; i <= -4; i++) {
			setBlockAndNotifyAdequately(world, basepos.up(4).east(i).south(6), stairs);
			if (i != -6) setBlockAndNotifyAdequately(world, basepos.up(5).east(i).south(5), stairs);
		}
		//roof top
		for (int k = -6; k <= 0; k++) {
			setBlockAndNotifyAdequately(world, basepos.up(7).west(3).south(k), Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, EnumType.SPRUCE));
			if (k == -5) {
				BlockPos pos = basepos.up(6).west(3).south(k);
				IBlockState state = world.getBlockState(pos);
				while (state.getBlock() == Blocks.AIR) {
					setRandomBrick(rand, world, pos);
					pos = pos.down();
					state = world.getBlockState(pos);
				}
			} else {
				setBlockAndNotifyAdequately(world, basepos.up(6).west(3).south(k), Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, EnumType.SPRUCE));
			}
		}
		for (int i = -2; i <= 6; i++) {
			setBlockAndNotifyAdequately(world, basepos.up(8).east(i).south(2), Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, EnumType.SPRUCE));
			if (i == 5) {
				BlockPos pos = basepos.up(7).east(i).south(2);
				IBlockState state = world.getBlockState(pos);
				while (state.getBlock() == Blocks.AIR) {
					setRandomBrick(rand, world, pos);
					pos = pos.down();
					state = world.getBlockState(pos);
				}
			} else {
				setBlockAndNotifyAdequately(world, basepos.up(7).east(i).south(2), Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, EnumType.SPRUCE));
			}
		}
		generated = true;
		return true;
	}

	@SuppressWarnings("unchecked")
	private void decorateBase(World world, Random rand) {
		final boolean isHalloween = TimedEvents.isHalloween();
		BlockPos pos = basepos.up();
		if (exitpos == null) {
			CrashReport report = CrashReport.makeCrashReport(new Exception("Please report to LDOH discord, I would make a new world if I were you."), "If this crash happens something has gone seriously wrong.");
			throw new ReportedException(report);
		}
		//workbenches and chests
		setBlockAndNotifyAdequately(world, pos.west(4).south(4), com.mrcrayfish.guns.init.ModBlocks.WORKBENCH.getDefaultState()
				.withProperty(BlockWorkbench.FACING, isSilly ? EnumFacing.EAST : EnumFacing.WEST) );
		setBlockAndNotifyAdequately(world, pos.west(4).south(3), Blocks.CRAFTING_TABLE.getDefaultState() );

		for (int i = 0; i <= 1; i++) {
			BlockPos chest = pos.south(4).west(i);
			setBlockAndNotifyAdequately(world, chest, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, isSilly ? EnumFacing.SOUTH : EnumFacing.NORTH) );
			((TileEntityLockableLoot) world.getTileEntity(chest)).setLootTable(ModDefinitions.SAFEHOUSE_CHEST, rand.nextLong());
		}

		//desk
		setBlockAndNotifyAdequately(world, pos.west(4), LDOHBlocks.FILING_CABINET.getDefaultState()
				.withProperty(BlockHorizontal.FACING, isSilly ? EnumFacing.WEST : EnumFacing.EAST) );
		TileFilingCabinet cabinet = (TileFilingCabinet) world.getTileEntity(pos.west(4));
		cabinet.insertItem(new ItemStack(Items.PAPER, rand.nextInt(7) + 10));
		for (int k = 1; k <= 3; k++) {
			setBlockAndNotifyAdequately(world, pos.west(4).north(k), FurnitureBlocks.TABLE_ANDESITE.getDefaultState() );
		}
		setBlockAndNotifyAdequately(world, pos.west(4).north().up(), FurnitureBlocks.DIGITAL_CLOCK.getDefaultState()
				.withProperty(BlockFurniture.FACING, EnumFacing.WEST).withProperty(BlockColored.COLOR, EnumDyeColor.BLACK) );
		if (isSilly) setBlockAndNotifyAdequately(world, pos.west(3).north(2), FurnitureBlocks.TOILET.getDefaultState()
				.withProperty(BlockFurniture.FACING, EnumFacing.EAST) );
		else setBlockAndNotifyAdequately(world, pos.west(3).north(2), FurnitureBlocks.MODERN_CHAIR.getDefaultState()
				.withProperty(BlockFurniture.FACING, EnumFacing.EAST) );
		setBlockAndNotifyAdequately(world, pos.west(4).north(3).up(), Blocks.BREWING_STAND.getDefaultState());
		TileEntityBrewingStand brewingStand = (TileEntityBrewingStand) world.getTileEntity(pos.west(4).north(3).up());
		for (int i = 0; i < 3; i++) brewingStand.setInventorySlotContents(i, new ItemStack(LDOHItems.SYRINGE, 1, rand.nextInt(2)+1));
		setBlockAndNotifyAdequately(world, pos.west(4).north(4), net.blay09.mods.cookingforblockheads.block.ModBlocks.fridge.getDefaultState()
				.withProperty(BlockFridge.FACING, isSilly ? EnumFacing.WEST : EnumFacing.EAST) );
		IItemHandler fridgeInv = ((TileFridge) world.getTileEntity(pos.west(4).north(4))).getCombinedItemHandler();
		LootTableManager manager = world.getLootTableManager();
		for (ItemStack stack : manager.getLootTableFromLocation(ModDefinitions.SAFEHOUSE_MEDICAL_FRIDGE)
				.generateLootForPools(rand, new LootContext(0, (WorldServer) world, manager, null, null, null))) {
			while (true) {
				int slot = rand.nextInt(fridgeInv.getSlots());
				if (fridgeInv.isItemValid(slot, stack)) {
					fridgeInv.insertItem(slot, stack, false);
					break;
				}
			}
		}

		//table
		for (int i = 3; i <= 4; i++) {
			for (int j = 3; j <= 4; j++) {
				setBlockAndNotifyAdequately(world, pos.add(i, 0, j), FurnitureBlocks.TABLE_SPRUCE.getDefaultState() );
			}
		}
		setBlockAndNotifyAdequately(world, pos.add(4, 0, 2), FurnitureBlocks.CHAIR_SPRUCE.getDefaultState().withProperty(BlockFurniture.FACING, EnumFacing.NORTH) );
		setBlockAndNotifyAdequately(world, pos.add(2, 0, 3), FurnitureBlocks.CHAIR_SPRUCE.getDefaultState().withProperty(BlockFurniture.FACING, EnumFacing.WEST) );
		setBlockAndNotifyAdequately(world, pos.add(3, 1, 3), FurnitureBlocks.PLATE.getDefaultState() );

		setBlockAndNotifyAdequately(world, pos.east(4), net.blay09.mods.cookingforblockheads.block.ModBlocks.fridge.getDefaultState().withProperty(BlockFridge.FACING,
				isSilly ? EnumFacing.EAST : EnumFacing.WEST) );
		fridgeInv = ((TileFridge) world.getTileEntity(pos.east(4))).getCombinedItemHandler();
		for (ItemStack stack : manager.getLootTableFromLocation(ModDefinitions.SAFEHOUSE_FRIDGE)
				.generateLootForPools(rand, new LootContext(0, (WorldServer) world, manager, null, null, null))) {
			while (true) {
				int slot = rand.nextInt(fridgeInv.getSlots());
				if (fridgeInv.isItemValid(slot, stack)) {
					fridgeInv.insertItem(slot, stack, false);
					break;
				}
			}
		}

		//torches
		if (!isHalloween) {
			IBlockState torch = RealisticTorchesBlocks.torchLit.getDefaultState();
			setBlockAndNotifyAdequately(world, pos.add(-4, 2, -4), torch.withProperty(BlockTorch.FACING, EnumFacing.EAST) );
			setBlockAndNotifyAdequately(world, pos.add(-4, 2, 4), torch.withProperty(BlockTorch.FACING, EnumFacing.EAST) );
			setBlockAndNotifyAdequately(world, pos.add(4, 2, 4), torch.withProperty(BlockTorch.FACING, EnumFacing.WEST) );
			setBlockAndNotifyAdequately(world, pos.add(4, 2, 0), torch.withProperty(BlockTorch.FACING, EnumFacing.WEST) );
			setBlockAndNotifyAdequately(world, pos.add(-1, 2, 0), torch.withProperty(BlockTorch.FACING, EnumFacing.SOUTH) );
			setBlockAndNotifyAdequately(world, pos.add(-2, 2, -1), torch.withProperty(BlockTorch.FACING, EnumFacing.WEST) );
		}

		//door
		setBlockAndNotifyAdequately(world, pos.north().east(2).down(), BOPBlocks.planks_0.getDefaultState()
				.withProperty(((BlockBOPPlanks)BOPBlocks.planks_0).variantProperty, BOPWoods.FIR) );
		IBlockState door = BOPBlocks.fir_door.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.SOUTH);
		setBlockAndNotifyAdequately(world, pos.north().east(2), door.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER) );
		setBlockAndNotifyAdequately(world, pos.north().east(2).up(), door.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER) );

		//canopy
		BlockPos fencePos = world.getHeight(pos.north(3).east());
		for (int y = fencePos.getY(); y < pos.up(2).getY() ; y++) {
			setBlockAndNotifyAdequately(world, new BlockPos(fencePos.getX(), y, fencePos.getZ()), Blocks.SPRUCE_FENCE.getDefaultState());
		}
		fencePos = world.getHeight(pos.north(3).east(3));
		for (int y = fencePos.getY(); y < pos.up(2).getY() ; y++) {
			setBlockAndNotifyAdequately(world, new BlockPos(fencePos.getX(), y, fencePos.getZ()), Blocks.SPRUCE_FENCE.getDefaultState());
		}
		IBlockState stairs = Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
		setBlockAndNotifyAdequately(world, pos.north(2).east().up(2), stairs.withProperty(BlockStairs.FACING, EnumFacing.EAST));
		setBlockAndNotifyAdequately(world, pos.north(2).east(2).up(2), Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE));
		setBlockAndNotifyAdequately(world, pos.north(2).east(3).up(2), stairs.withProperty(BlockStairs.FACING, EnumFacing.WEST));
		setBlockAndNotifyAdequately(world, pos.north(3).east().up(2), stairs.withProperty(BlockStairs.SHAPE, EnumShape.OUTER_LEFT));
		setBlockAndNotifyAdequately(world, pos.north(3).east(2).up(2), stairs);
		setBlockAndNotifyAdequately(world, pos.north(3).east(3).up(2), stairs.withProperty(BlockStairs.SHAPE, EnumShape.OUTER_RIGHT));

		//crates
		placeCrate(world.getHeight(pos.north(2)), world);
		placeCrate(world.getHeight(pos.north(2)), world);
		placeCrate(world.getHeight(pos.north(3)).east(5), world);
		placeCrate(world.getHeight(pos.north(5)).east(4), world);

		//outer door
		if (exitpos.getY() != 0) {
			for (int i = -1; i<2; i++) {
				for (int j = 0; j<=2; j++) {
					setRandomBrick(rand, world, exitpos.east(i).up(j));
				}
			}
			door = Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.SOUTH);
			setBlockAndNotifyAdequately(world, exitpos, door.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER) );
			setBlockAndNotifyAdequately(world, exitpos.up(), door.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER) );
			IBlockState button = Blocks.STONE_BUTTON.getDefaultState();
			setBlockAndNotifyAdequately(world, exitpos.up().east().north(), button.withProperty(BlockButton.FACING, EnumFacing.NORTH) );
			setBlockAndNotifyAdequately(world, exitpos.up().east().south(), button.withProperty(BlockButton.FACING, EnumFacing.SOUTH) );
		}
		if (isHalloween) placeHalloweenDecorations(world, rand, pos);
		if (TimedEvents.isChristmas()) placeChristmasDecorations(world, rand, pos);
		if (isAnniversary) placeAnniversaryDecorations(world, rand, pos);
	}

	private void placeHalloweenDecorations(World world, Random rand, BlockPos pos) {
		setBlockAndNotifyAdequately(world, pos.add(-2, 0, 4), Blocks.LIT_PUMPKIN.getDefaultState() );
		if (rand.nextInt(3) == 0) setBlockAndNotifyAdequately(world, pos.add(4, 3, 0), Blocks.WEB.getDefaultState() );
		if (rand.nextInt(3) == 0) setBlockAndNotifyAdequately(world, pos.add(4, 3, 4), Blocks.WEB.getDefaultState() );
		if (rand.nextInt(3) == 0) setBlockAndNotifyAdequately(world, pos.add(-4, 3, 4), Blocks.WEB.getDefaultState() );
		if (rand.nextInt(3) == 0) setBlockAndNotifyAdequately(world, pos.add(-4, 3, -4), Blocks.WEB.getDefaultState() );
		if (rand.nextInt(3) == 0) setBlockAndNotifyAdequately(world, pos.add(-2, 3, -4), Blocks.WEB.getDefaultState() );
		BlockPos skullPos = pos.add(-4, 1, 0);
		setBlockAndNotifyAdequately(world, skullPos, Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP));
		TileEntitySkull skull = ((TileEntitySkull)world.getTileEntity(skullPos));
		skull.setType(1);
		skull.rotate(Rotation.CLOCKWISE_90);
		setBlockAndNotifyAdequately(world, pos.add(-4, 1, -1), FurnitureBlocks.CANDLE.getDefaultState() );
		setBlockAndNotifyAdequately(world, pos.add(4, 1, 4), FurnitureBlocks.CANDLE.getDefaultState() );
	}

	private void placeChristmasDecorations(World world, Random rand, BlockPos pos) {
		BlockPos treePos = pos.add(-2, 0, 4);
		setBlockAndNotifyAdequately(world, treePos, FurnitureBlocks.TREE_BOTTOM.getDefaultState() );
		decorateTree(world, treePos);
		setBlockAndNotifyAdequately(world, treePos.up(), FurnitureBlocks.TREE_TOP.getDefaultState() );
		decorateTree(world, treePos.up());
		setBlockAndNotifyAdequately(world, pos.add(2, 2, 0), FurnitureBlocks.WREATH.getDefaultState().withProperty(BlockFurniture.FACING, EnumFacing.NORTH) );
		for (int i = -5; i <= 5; i++) {
			setBlockAndNotifyAdequately(world, pos.add(-6, 2, i), FurnitureBlocks.FAIRY_LIGHT.getDefaultState().withProperty(BlockFurniture.FACING, EnumFacing.EAST) );
			setBlockAndNotifyAdequately(world, pos.add(i, 2, 6), FurnitureBlocks.FAIRY_LIGHT.getDefaultState().withProperty(BlockFurniture.FACING, EnumFacing.NORTH) );
		}
		for (int i = 4; i <= 5; i++) {
			setBlockAndNotifyAdequately(world, pos.add(i, 2, -2), FurnitureBlocks.FAIRY_LIGHT.getDefaultState().withProperty(BlockFurniture.FACING, EnumFacing.SOUTH) );
		}
		for (int i = -5; i <= -2; i++) {
			setBlockAndNotifyAdequately(world, pos.add(0, 2, i), FurnitureBlocks.FAIRY_LIGHT.getDefaultState().withProperty(BlockFurniture.FACING, EnumFacing.WEST) );
		}
	}

	private void placeAnniversaryDecorations(World world, Random rand, BlockPos pos) {
		BlockPos signPos = pos.south(3).west(2);
		setBlockAndNotifyAdequately(world, signPos.south(), FurnitureBlocks.MODERN_TABLE.getDefaultState());
		setBlockAndNotifyAdequately(world, signPos.south().up(), cakes[rand.nextInt(cakes.length)].getDefaultState());
		setBlockAndNotifyAdequately(world, signPos, Blocks.WALL_SIGN.getDefaultState());
		TileEntitySign sign = (TileEntitySign) world.getTileEntity(signPos);
		for (int i = 0; i < 4; i++) sign.signText[i] = new TextComponentString(anniversaryMessage[i]);
	}

	private void decorateTree(World world, BlockPos pos) {
		TileEntityTree tree = (TileEntityTree) world.getTileEntity(pos);
		for (EnumFacing facing : EnumFacing.VALUES) {
			if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) tree.addOrnament(facing, new ItemStack(FurnitureBlocks.FAIRY_LIGHT));
		}
	}

	private void setRandomBrick(Random rand, World world, BlockPos pos) {
		IBlockState state = Blocks.STONEBRICK.getDefaultState();
		int r = rand.nextInt(TimedEvents.isHalloween() ? 3 : 10);
		if (r == 0) {
			state = state.withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
		} else if (r == 1) {
			state = state.withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
		}
		setBlockAndNotifyAdequately(world, pos, state );
	}

	private void placeCrate(BlockPos pos, World world) {
		setBlockAndNotifyAdequately(world, pos, FurnitureBlocks.CRATE_SPRUCE.getDefaultState() );
		((TileEntityLockableLoot)world.getTileEntity(pos)).setLootTable(ModDefinitions.SAFEHOUSE_CRATE, world.rand.nextLong());
	}

}
