package net.smileycorp.ldoh.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;
import net.smileycorp.ldoh.common.util.EnumAxis;
import net.smileycorp.ldoh.common.util.EnumBarbedWireMat;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;

public class BlockBarbedWire extends Block implements IBlockProperties, ITileEntityProvider {

	public static PropertyEnum<EnumBarbedWireMat> MATERIAL = PropertyEnum.create("material", EnumBarbedWireMat.class);
	public static PropertyEnum<EnumAxis> AXIS = PropertyEnum.create("axis", EnumAxis.class);
	public static Properties.PropertyAdapter<Boolean> IS_ENCHANTED = new Properties.PropertyAdapter<>(PropertyBool.create("is_enchanted"));

	public static final AxisAlignedBB HITBOX_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.1D, 0.9D);

	public BlockBarbedWire() {
		super(Material.ROCK);
		String name = "Barbed_Wire";
		setCreativeTab(LDOHTweaks.CREATIVE_TAB);
		setUnlocalizedName(ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
		setDefaultState(blockState.getBaseState().withProperty(MATERIAL, EnumBarbedWireMat.IRON).withProperty(AXIS, EnumAxis.X));
		setHarvestLevel("pickaxe", 2);
		setHardness(0.3F);
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
		return new TileBarbedWire(EnumBarbedWireMat.byMeta(meta%3));
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		entity.setInWeb();
		if (world.getTileEntity(pos) instanceof TileBarbedWire &! world.isRemote) {
			TileBarbedWire te = (TileBarbedWire) world.getTileEntity(pos);
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
		return new ExtendedBlockState(this, new IProperty[]{MATERIAL, AXIS}, new IUnlistedProperty[]{IS_ENCHANTED});
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if(te != null && te instanceof TileBarbedWire) {
			return ((IExtendedBlockState)state).withProperty(IS_ENCHANTED,((TileBarbedWire) te).isEnchanted());
		}
		return ((IExtendedBlockState)state).withProperty(IS_ENCHANTED, false);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (stack.hasTagCompound()) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (world.getTileEntity(pos) instanceof TileBarbedWire &! placer.world.isRemote) {
				TileBarbedWire tile = ((TileBarbedWire) world.getTileEntity(pos));
				if (nbt.hasKey("durability"))tile.setDurability(nbt.getInteger("durability"));
				if (nbt.hasKey("ench")) {
					for (NBTBase tag : nbt.getTagList("ench", 10)) {
						int level = ((NBTTagCompound)tag).getShort("lvl");
						Enchantment enchant = Enchantment.getEnchantmentByID(((NBTTagCompound)tag).getShort("id"));
						tile.applyEnchantment(enchant, level);
					}
				}
				if (placer instanceof EntityPlayer) tile.setOwner((EntityPlayer) placer);
			}
		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase player, EnumHand hand) {
		EnumAxis axis = EnumAxis.fromVector(player.getLookVec());
		return getStateFromMeta(player.getHeldItem(hand).getMetadata()).withProperty(AXIS, axis);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumAxis axis = meta > 2 ? EnumAxis.Z : EnumAxis.X;
		return getDefaultState().withProperty(MATERIAL, EnumBarbedWireMat.byMeta(meta%3)).withProperty(AXIS, axis);
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
		EnumBarbedWireMat mat = state.getValue(MATERIAL);
		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
			ItemStack drop = ((TileBarbedWire) te).getDrop();
			spawnAsEntity(world, pos, drop);
		} else {
			Item item = mat.getDrop();
			int count = (int) ((double)((TileBarbedWire) te).getDurability() / (double)mat.getDurability() * 7d);
			spawnAsEntity(world, pos, new ItemStack(item, count, 0));
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

	@Override
	public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EntityLiving entity) {
		return (entity instanceof EntityTF2Character || entity instanceof EntityVillagerTek) ? PathNodeType.DAMAGE_CACTUS : super.getAiPathNodeType(state, world, pos, entity);
	}

}
