package net.smileycorp.ldoh.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.world.BlockStateCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PathNavigateFlying.class)
public class MixinPathNavigateFlying {

    @Redirect(method = "canEntityStandOnPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState canEntityStandOnPos$getBlockState(World world, BlockPos pos) {
        return BlockStateCache.getBlockState(world, pos);
    }

}
