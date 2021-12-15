package net.smileycorp.ldoh.common.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.tile.TileTurret;

public class BlockTurret extends BlockDirectional implements IBlockProperties, ITileEntityProvider {

	private static final AxisAlignedBB[] AABBs = {new AxisAlignedBB(0, 0.7, 0, 1, 1, 1), new AxisAlignedBB(0, 0, 0, 1, 0.3, 1),
		new AxisAlignedBB(0, 0, 0.7, 1, 1, 1),  new AxisAlignedBB(0, 0, 0, 1, 1, 0.3), new AxisAlignedBB(0.7, 0, 0, 1, 1, 1),  new AxisAlignedBB(0, 0, 0, 0.3, 1, 1)};

	public BlockTurret() {
		super(Material.IRON);
		String name = "Turret";
		setCreativeTab(LDOHTweaks.CREATIVE_TAB);
		setUnlocalizedName(ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
		setHarvestLevel("pickaxe", 2);
		setHardness(1F);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileTurret();
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (world.getTileEntity(pos) instanceof TileTurret) ((TileTurret) world.getTileEntity(pos)).breakBlock();
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		if(side != EnumFacing.UP) return false;
		return world.isBlockFullCube(pos.offset(side.getOpposite()));
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighbor, BlockPos neighborPos) {
		if (!canPlaceBlockOnSide(world, pos, state.getValue(FACING))) world.destroyBlock(pos, true);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (world.getTileEntity(pos) instanceof TileTurret && placer instanceof EntityPlayer &! placer.world.isRemote) {
			((TileTurret) world.getTileEntity(pos)).spawnEntity((EntityPlayer) placer, state.getValue(FACING), nbt == null ? new NBTTagCompound() : nbt);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntity te = world.getTileEntity(pos);
			if (te != null) {
				if (te instanceof TileTurret) {
					EntityTurret turret = ((TileTurret) te).getEntity();
					if (turret!=null) return turret.applyPlayerInteraction(player, new Vec3d(hitX, hitY, hitZ), hand) == EnumActionResult.SUCCESS;
				}
			}
		}
		return false;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
		return state.withRotation(mirror.toRotation(state.getValue(FACING)));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta%getMaxMeta()]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).ordinal();
	}

	@Override
	public int getMaxMeta() {
		return EnumFacing.VALUES.length;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABBs[state.getValue(FACING).ordinal()];
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	//From BlockFlowerPot, should delay until drops are spawned, before block is broken
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (willHarvest &! world.isRemote) {
			if (world.getTileEntity(pos) instanceof TileTurret) {
				TileTurret tile = (TileTurret) world.getTileEntity(pos);
				ItemStack stack = new ItemStack((Block) this);
				NBTTagCompound nbt = tile.getDropNBT();
				stack.setTagCompound(nbt);
				tile.getEntity().entityDropItem(stack, 0f);
			}
			return true;
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool) {
		super.harvestBlock(world, player, pos, state, te, tool);
		world.setBlockToAir(pos);
	}


}
