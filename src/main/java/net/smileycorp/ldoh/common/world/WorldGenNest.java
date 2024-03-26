package net.smileycorp.ldoh.common.world;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.common.block.BlockBOPDoubleDecoration.Half;
import biomesoplenty.common.block.BlockBOPDoublePlant;
import biomesoplenty.common.block.BlockBOPDoublePlant.DoublePlantType;
import com.dhanantry.scapeandrunparasites.block.BlockInfestedBush;
import com.dhanantry.scapeandrunparasites.block.BlockInfestedBush.EnumType;
import com.dhanantry.scapeandrunparasites.block.BlockParasiteStain;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityFlog;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.ldoh.common.Constants;

import java.util.Random;

public class WorldGenNest extends WorldGenerator {

    private IBlockState[] nest_blocks = {Blocks.NETHER_WART_BLOCK.getDefaultState(), SRPBlocks.ParasiteStain.getDefaultState().withProperty(BlockParasiteStain.VARIANT, BlockParasiteStain.EnumType.FEELER),
            SRPBlocks.ParasiteStain.getDefaultState().withProperty(BlockParasiteStain.VARIANT, BlockParasiteStain.EnumType.FLESH)};
    private IBlockState[] wall_blocks = {Blocks.NETHERRACK.getDefaultState(), BOPBlocks.flesh.getDefaultState()};

    @SuppressWarnings("unchecked")
    @Override
    public boolean generate(World world, Random rand, BlockPos base) {
        //crater
        for (int i = -8; i <= 8; i++) {
            for (int j = -8; j <= 8; j++) {
                for (int k = -8; k <= 8; k++) {
                    BlockPos pos = base.up().add(i, j, k);
                    double r = pos.getDistance(base.getX(), base.getY(), base.getZ());
                    if (r <= 7.5) world.setBlockToAir(pos);
                    else if (r < 8.5) placeWallBlock(world, pos);
                }
            }
        }
        //hole
        for (int i = -20; i <= 20; i++) {
            for (int k = -20; k <= 20; k++) {
                BlockPos pos = base.add(i, 0, k);
                int top = world.getTopSolidOrLiquidBlock(pos).getY();
                double r = 7.5;
                int x = 0;
                while (pos.getY() < top) {
                    double s = pos.getDistance(base.getX(), pos.getY(), base.getZ());
                    if (s <= r) {
                        world.setBlockToAir(pos);
                    } else if (s <= r + 2) {
                        placeWallBlock(world, pos);
                    }
                    pos = pos.up();
                    x++;
                    if (x % 7 == 0) r += 1;
                }
            }
        }
        //nest
        for (int i = -6; i < 7; i++) {
            for (int j = -6; j < 7; j++) {
                for (int k = -6; k < 7; k++) {
                    BlockPos pos = base.add(i, j, k);
                    double r = pos.getDistance(base.getX(), base.getY(), base.getZ());
                    if (r <= 5.5) world.setBlockToAir(pos);
                    else if (r <= 6.5) placeNestBlock(world, pos);
                }
            }
        }
        //decoration
        for (int i = 0; i < rand.nextInt(6) + 8; i++) {
            BlockPos pos = world.getTopSolidOrLiquidBlock(base.add(rand.nextInt(12) - 6, 0, rand.nextInt(12) - 6));
            if (world.getBlockState(pos.down()).getBlock() == SRPBlocks.ParasiteStain) {
                for (int j = 0; j <= rand.nextInt(3); j++) {
                    setBlockAndNotifyAdequately(world, pos, SRPBlocks.InfestedBush.getDefaultState().withProperty(BlockInfestedBush.VARIANT, EnumType.SPINE));
                }
            } else if (world.getBlockState(pos.down()).getBlock() == BOPBlocks.flesh) {
                IBlockState eyebulb = BOPBlocks.double_plant.getDefaultState()
                        .withProperty(BlockBOPDoublePlant.VARIANT, DoublePlantType.EYEBULB);
                setBlockAndNotifyAdequately(world, pos, eyebulb);
                setBlockAndNotifyAdequately(world, pos.up(), eyebulb.withProperty(BlockBOPDoublePlant.HALF, Half.UPPER));
            }
        }
        //shulkers
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (facing.getAxis() == EnumFacing.Axis.Y) continue;
            BlockPos pos = base.offset(facing, 4);
            placeLoot(world, pos.down(4), EnumFacing.UP);
            placeLoot(world, pos.up(4), EnumFacing.DOWN);
        }
        //grunts
        for (int i = 0; i < 3; i++) {
            EntityFlog grunt = new EntityFlog(world);
            grunt.setPosition(base.getX(), base.getY() - 1, base.getZ());
            grunt.enablePersistence();
            world.spawnEntity(grunt);
        }
        //rupters
        for (int i = 0; i < 7; i++) {
            EntityMudo rupter = new EntityMudo(world);
            rupter.setPosition(base.getX(), base.getY() - 1, base.getZ());
            rupter.enablePersistence();
            world.spawnEntity(rupter);
        }
        return true;
    }

    private void placeWallBlock(World world, BlockPos pos) {
        setBlockAndNotifyAdequately(world, pos, wall_blocks[world.rand.nextInt(wall_blocks.length)]);
    }

    private void placeNestBlock(World world, BlockPos pos) {
        setBlockAndNotifyAdequately(world, pos, nest_blocks[world.rand.nextInt(nest_blocks.length)]);
    }

    private void placeLoot(World world, BlockPos pos, EnumFacing facing) {
        setBlockAndNotifyAdequately(world, pos, Blocks.RED_SHULKER_BOX.getDefaultState().withProperty(BlockShulkerBox.FACING, facing));
        TileEntityShulkerBox tile = new TileEntityShulkerBox();
        tile.setLootTable(Constants.NEST_CRATE, world.rand.nextLong());
        world.setTileEntity(pos, tile);
    }

}
