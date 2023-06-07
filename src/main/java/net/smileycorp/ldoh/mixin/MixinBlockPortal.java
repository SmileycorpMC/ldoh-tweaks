package net.smileycorp.ldoh.mixin;

import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockPortal.class)
public abstract class MixinBlockPortal extends BlockBreakable {

	protected MixinBlockPortal(Material p_i45712_1_, boolean p_i45712_2_) {
		super(p_i45712_1_, p_i45712_2_);
	}

	@Inject(at=@At("HEAD"), method = "trySpawnPortal(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", cancellable = true)
	public void trySpawnPortal(World worldIn, BlockPos pos, CallbackInfoReturnable<Boolean> callback) {
		callback.setReturnValue(false);
		callback.cancel();
	}

}
