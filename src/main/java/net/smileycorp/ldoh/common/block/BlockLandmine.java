package net.smileycorp.ldoh.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.tile.TileLandmine;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;

import javax.annotation.Nullable;

public class BlockLandmine extends Block implements IBlockProperties, ITileEntityProvider {

	public static final PropertyBool PRIMED = PropertyBool.create("primed");
	public static final PropertyBool PRESSED = PropertyBool.create("pressed");

	public static final AxisAlignedBB HITBOX_AABB = new AxisAlignedBB(0.2D, 0.0D, 0.2D, 0.8D, 0.1D, 0.8D);

	public BlockLandmine() {
		super(Material.IRON);
		String name = "Landmine";
		setCreativeTab(LDOHTweaks.CREATIVE_TAB);
		setUnlocalizedName(ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
		setHarvestLevel("pickaxe", 2);
		setHardness(0.3F);
		setDefaultState(blockState.getBaseState().withProperty(PRIMED, false).withProperty(PRESSED, false));
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (!world.isRemote) {
			if (state.getValue(PRIMED) &! state.getValue(PRESSED) && entity instanceof EntityLivingBase) {
				TileEntity te = world.getTileEntity(pos);
				world.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.7F);
				world.setBlockState(pos, state.withProperty(PRESSED, true), 2);
				world.setTileEntity(pos, te);
				world.markBlockRangeForRenderUpdate(pos, pos);
			}
		}
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {PRIMED, PRESSED});
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return HITBOX_AABB;
	}

	@Override
	public boolean isPassable(IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP && world.isBlockFullCube(pos.down());
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if (!canPlaceBlockAt(world, pos)) {
			if (state.getValue(PRIMED)) explode(world, pos);
			else {
				world.destroyBlock(pos, true);
			}
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		world.removeTileEntity(pos);
		if (state.getValue(PRIMED)) explode(world, pos);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(PRIMED, meta%2==1).withProperty(PRESSED, Math.floor(meta/2) == 1);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PRIMED) ? 1 : 0 + 2*(state.getValue(PRESSED) ? 1 : 0) ;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileLandmine();
	}

	@Override
	public int getMaxMeta() {
		return 4;
	}

	@Override
	public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EntityLiving entity) {
		return (entity instanceof EntityTF2Character || entity instanceof EntityVillagerTek) ? PathNodeType.BLOCKED : super.getAiPathNodeType(state, world, pos, entity);
	}

	public static void prime(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			TileEntity te = world.getTileEntity(pos);
			world.setBlockState(pos, state.withProperty(PRIMED, true), 2);
			world.setTileEntity(pos, te);
			world.markBlockRangeForRenderUpdate(pos, pos);
		}
	}

	public static void explode(World world, BlockPos pos) {
		world.setBlockToAir(pos);
		world.removeTileEntity(pos);
		float x = pos.getX() + 0.5f;
		float y = pos.getY();
		float z = pos.getX() + 0.5f;
		for (EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x-3, y-3, z-3, x+3, y+3, z+3))) {
			if (entity.attackable() &! entity.isImmuneToExplosions()) {
				entity.attackEntityFrom(LDOHTweaks.SHRAPNEL_DAMAGE, (float) Math.exp(3.4-entity.getDistance(x, y, z)));
			}
		}
		Explosion explosion = world.createExplosion(null, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5, 4, false);
		for (int i = -1; i <=1; i++) {
			for (int k = -1; k <=1; k++) {
				if (Math.abs(i)!=Math.abs(k) || i == 0) {
					BlockPos pos0 = pos.down().east(i).south(k);
					if (world.getBlockState(pos0).getBlock().getExplosionResistance(world, pos0, null, explosion) < 10f) world.setBlockToAir(pos0);
				}
			}
		}
	}

}
