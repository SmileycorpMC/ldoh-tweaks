package net.smileycorp.ldoh.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBucket.class)
public abstract class MixinItemBucket {

	@Shadow
	private Block containedBlock;

	@Inject(at=@At("HEAD"), method = "tryPlaceContainedLiquid(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", cancellable = true)
	public void tryPlaceContainedLiquid(EntityPlayer player, World world, BlockPos pos, CallbackInfoReturnable<Boolean> callback) {
		if (containedBlock == Blocks.FLOWING_LAVA) {
			callback.setReturnValue(false);
			callback.cancel();
		}
	}

}
