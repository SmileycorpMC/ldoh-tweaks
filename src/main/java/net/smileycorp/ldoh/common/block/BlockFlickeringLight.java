package net.smileycorp.ldoh.common.block;


import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;

public class BlockFlickeringLight extends Block {
    
    public BlockFlickeringLight() {
        super(Material.IRON);
        setHardness(1.0F);
        setResistance(5.0F);
        setSoundType(SoundType.GLASS);
        setHarvestLevel("PICKAXE", 0);
        String name = "Flickering_Light";
        setUnlocalizedName(ModDefinitions.getName(name));
        setRegistryName(ModDefinitions.getResource(name));
        setCreativeTab(LDOHTweaks.CREATIVE_TAB);
    }
    
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (!(world instanceof World)) return 9;
        if (!((World) world).isRemote) return 9;
        return ((World) world).getTotalWorldTime() % 30 > 10 ? 9 : 0;
    }
    
}
