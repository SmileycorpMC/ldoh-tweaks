package net.smileycorp.ldoh.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.tile.TileCache;

public class BlockCache extends Block implements ITileEntityProvider, IBlockProperties {

	public BlockCache() {
		super(Material.IRON);
		setHarvestLevel("PICKAXE", 1);
		String name = "Cache";
		setUnlocalizedName(ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
		setCreativeTab(LDOHTweaks.CREATIVE_TAB);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileCache();
	}

}
