package net.smileycorp.ldoh.common.block;

import net.minecraft.block.BlockEmptyDrops;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.Constants;

public class BlockLaboratory extends BlockEmptyDrops implements IBlockProperties {

    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 15);

    public BlockLaboratory() {
        super(Material.IRON);
        disableStats();
        setBlockUnbreakable();
        setResistance(6000000);
        setSoundType(SoundType.METAL);
        String name = "Laboratory_Block";
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        setCreativeTab(LDOHTweaks.CREATIVE_TAB);
        setDefaultState(this.getBlockState().getBaseState().withProperty(VARIANT, 0));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        ItemStack stack = placer.getHeldItem(hand);
        return getDefaultState().withProperty(VARIANT, stack.getMetadata());
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, meta);
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT);
    }


    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{VARIANT});
    }

    @Override
    public int getMaxMeta() {
        return 16;
    }

}
