package net.smileycorp.ldoh.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.tile.TileHordeSpawner;

public class BlockHordeSpawner extends Block implements ITileEntityProvider, IBlockProperties {
    
    public BlockHordeSpawner() {
        super(Material.AIR);
        String name = "Horde_Spawner";
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        setCreativeTab(LDOHTweaks.CREATIVE_TAB);
        setHardness(-1F);
        setResistance(-1F);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
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
        return new TileHordeSpawner();
    }

    public static void breakBlock(World world, BlockPos pos) {
        world.setBlockToAir(pos);
        world.removeTileEntity(pos);
    }

}
