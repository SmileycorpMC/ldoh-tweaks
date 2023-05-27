package net.smileycorp.ldoh.mixin;

import net.minecraft.block.BlockPumpkin;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockPumpkin.class)
public abstract class MixinBlockPumpkin {

	@Inject(at=@At("HEAD"), method = "trySpawnGolem(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V", cancellable = true)
	public void trySpawnGolem(World world, BlockPos pos, CallbackInfo callback) {
		callback.cancel();
	}

}
