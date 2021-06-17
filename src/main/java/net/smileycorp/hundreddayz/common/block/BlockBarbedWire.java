package net.smileycorp.hundreddayz.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.hundreddayz.common.ModContent;
import net.smileycorp.hundreddayz.common.ModDefinitions;

public class BlockBarbedWire extends Block implements IBlockProperties, ITileEntityProvider {

	public static PropertyEnum<EnumBarbedWireMat> MATERIAL = PropertyEnum.create("material", EnumBarbedWireMat.class);
	public static PropertyEnum<EnumBarbedWireAxis> AXIS = PropertyEnum.create("axis", EnumBarbedWireAxis.class);
	
	public BlockBarbedWire() {
		super(Material.ROCK);
		String name = "Barbed_Wire";
		setCreativeTab(ModContent.CREATIVE_TAB);
		setUnlocalizedName(ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
		setDefaultState(blockState.getBaseState().withProperty(MATERIAL, EnumBarbedWireMat.IRON).withProperty(AXIS, EnumBarbedWireAxis.X));
		setHarvestLevel("pickaxe", 0);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		world.removeTileEntity(pos);
	}
	
    @Override
	public boolean hasTileEntity(IBlockState state) {
        return true;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityBarbedWire(EnumBarbedWireMat.byMeta(meta%3));
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (entity instanceof EntityItem) entity.setDead();
        entity.setInWeb();
        if (world.getTileEntity(pos) instanceof TileEntityBarbedWire &! world.isRemote) {
        	TileEntityBarbedWire te = (TileEntityBarbedWire) world.getTileEntity(pos);
        	if (te.getOrUpdateCooldown() == 0) {
        		te.causeDamage();
        	}
        	if (te.getDurability() <= 0) {
        		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        	}
        }
    }
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[]{MATERIAL, AXIS});
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase player, EnumHand hand) {
		EnumBarbedWireAxis axis = EnumBarbedWireAxis.fromVector(player.getLookVec());
		return getStateFromMeta(player.getHeldItem(hand).getMetadata()).withProperty(AXIS, axis);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumBarbedWireAxis axis = meta > 2 ? EnumBarbedWireAxis.Z : EnumBarbedWireAxis.X;
		return this.getDefaultState().withProperty(MATERIAL, EnumBarbedWireMat.byMeta(meta%3)).withProperty(AXIS, axis);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(AXIS).ordinal() * 3 + state.getValue(MATERIAL).ordinal();
    }
	
	@Override
	public void getSubBlocks(CreativeTabs item, NonNullList<ItemStack> items) {
        for (int i = 0; i<EnumBarbedWireMat.values().length; i++) {
        	items.add(new ItemStack(this, 1, i));
        }
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(MATERIAL).ordinal());
    }
	
	@Override
	public int quantityDropped(Random random) {
		return 0;
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
            java.util.List<ItemStack> items = new java.util.ArrayList<ItemStack>();
            ItemStack drop = new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(MATERIAL).ordinal());
            if (te instanceof TileEntityBarbedWire) {
            	int durability = ((TileEntityBarbedWire) te).getDurability();
            	stack.setItemDamage(durability);
            }
            items.add(drop);
            ForgeEventFactory.fireBlockHarvesting(items, world, pos, state, 0, 1.0f, true, player);
            for (ItemStack item : items){
                spawnAsEntity(world, pos, item);
            }
        }
    }

    @Override
	public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
	public boolean isFullCube(IBlockState state) {
        return false;
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
	public boolean usesCustomItemHandler(){
		return true;
	}

    @Override
    public int getMaxMeta() {
    	return EnumBarbedWireMat.values().length * 2;
    }
    
    public enum EnumBarbedWireAxis implements IStringSerializable {
    	X, 
    	Z;

		@Override
		public String getName() {
			return this.toString().toLowerCase();
		}

		public static EnumBarbedWireAxis fromVector(Vec3d vec) {
			return fromFacing(EnumFacing.getFacingFromVector((float)vec.x, 0f, (float)vec.z));
		}

		public static EnumBarbedWireAxis fromFacing(EnumFacing facing) {
			if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) return Z;
			return X;
		}

	}
  
}
