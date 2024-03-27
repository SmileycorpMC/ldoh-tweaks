package net.smileycorp.ldoh.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.tile.TileFilingCabinet;

public class BlockFilingCabinet extends Block implements ITileEntityProvider, IBlockProperties {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockFilingCabinet() {
        super(Material.IRON);
        setHardness(5.0F);
        setResistance(10.0F);
        setSoundType(SoundType.METAL);
        setHarvestLevel("PICKAXE", 1);
        String name = "Filing_Cabinet";
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        setCreativeTab(LDOHTweaks.CREATIVE_TAB);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote || player.isSneaking())
            return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
        if (world.getTileEntity(pos) instanceof TileFilingCabinet) {
            TileFilingCabinet cabinet = (TileFilingCabinet) world.getTileEntity(pos);
            ItemStack stack = player.getHeldItem(hand);
            if (cabinet.canInsert(stack)) {
                cabinet.insertItem(stack);
                return true;
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
        if (world.isRemote) return;
        if (world.getTileEntity(pos) instanceof TileFilingCabinet) {
            TileFilingCabinet cabinet = (TileFilingCabinet) world.getTileEntity(pos);
            if (cabinet.isEmpty()) return;
            ItemStack stack = player.getHeldItemMainhand();
            ItemStack item = cabinet.getContainedItem();
            if (stack.isEmpty() || (stack.getItem() == item.getItem() && stack.getMetadata() == item.getMetadata()
                    && stack.getTagCompound() == item.getTagCompound())) {
                ItemStack drop = cabinet.extractItem(player.isSneaking() ? stack.getMaxStackSize() : 1);
                if (!player.addItemStackToInventory(drop)) player.dropItem(drop, false);
            }
        }
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (world.getTileEntity(pos) instanceof TileFilingCabinet)
            ((TileFilingCabinet) world.getTileEntity(pos)).dropContents();
        super.breakBlock(world, pos, state);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getIndex();
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileFilingCabinet();
    }

}
