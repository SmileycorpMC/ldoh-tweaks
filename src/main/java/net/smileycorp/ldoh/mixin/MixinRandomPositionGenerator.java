package net.smileycorp.ldoh.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.world.BlockStateCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RandomPositionGenerator.class)
public class MixinRandomPositionGenerator {

    @Redirect(method = "moveAboveSolid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    private static IBlockState moveAboveSolid$getBlockState(World world, BlockPos pos) {
        return BlockStateCache.getBlockState(world, pos);
    }

    @Redirect(method = "isWaterDestination", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    private static IBlockState isWaterDestination$getBlockState(World world, BlockPos pos) {
        return BlockStateCache.getBlockState(world, pos);
    }

}
