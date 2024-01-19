package net.smileycorp.ldoh.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.SwimNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.ldoh.common.world.BlockStateCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SwimNodeProcessor.class)
public class MixinSwimNodeProcessor {

    @Redirect(method = "isFree", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState isFree$getBlockState(IBlockAccess access, BlockPos pos) {
        return BlockStateCache.getBlockState(access, pos);
    }

}
