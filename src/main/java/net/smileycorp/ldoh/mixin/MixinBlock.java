package net.smileycorp.ldoh.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.smileycorp.ldoh.common.util.ModUtils;
import net.tangotek.tektopia.entities.EntityVillagerTek;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;

@Pseudo
@Mixin(Block.class)
public class MixinBlock {

	@Inject(at=@At("HEAD"), method = "getAiPathNodeType(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/entity/EntityLiving;)Lnet/minecraft/pathfinding/PathNodeType;", remap = false)
	public void getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving entity, CallbackInfoReturnable<PathNodeType> callback) {
		System.out.println("block mixin");
		if (state.getBlock() == Blocks.FIRE && (entity instanceof EntityTF2Character || entity instanceof EntityVillagerTek)) {
			callback.setReturnValue(PathNodeType.BLOCKED);
			callback.cancel();
		}
		if (entity.hasCapability(LDOHCapabilities.VILLAGE_DATA, null)) {
			if (ModUtils.isTooFarFromVillage(entity, world)) {
				callback.setReturnValue(PathNodeType.BLOCKED);
				callback.cancel();
			}
		}
	}

}
