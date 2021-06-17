package net.smileycorp.hundreddayz.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.hundreddayz.common.ModDefinitions;

public class BlockHordeSpawner extends Block implements ITileEntityProvider {


	public BlockHordeSpawner() {
		super(Material.AIR);
		String name = "Horde_Spawner";
		setUnlocalizedName(ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
	}
	
    @Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
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
		return new TileEntityHordeSpawner();
	}

	public static void breakBlock(World world, BlockPos pos) {
		world.setBlockToAir(pos);
		world.removeTileEntity(pos);
	}	

}
