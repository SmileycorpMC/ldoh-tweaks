package net.smileycorp.ldoh.client.colour;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.ldoh.common.block.BlockTurret;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.tile.TileTurret;
import net.smileycorp.ldoh.common.util.TurretUpgrade;

import java.awt.*;

public class BlockTurretColour implements IBlockColor {

	//tint turret base australium colour if turret has australium upgrade
	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
		Block block = state.getBlock();
		if (block instanceof BlockTurret && world.getTileEntity(pos) instanceof TileTurret) {
			EntityTurret turret = ((TileTurret) world.getTileEntity(pos)).getEntity();
			if (turret != null && turret.hasUpgrade(TurretUpgrade.AUSTRALIUM)) return 0xFCD400;
		}
		return Color.WHITE.getRGB();
	}



}