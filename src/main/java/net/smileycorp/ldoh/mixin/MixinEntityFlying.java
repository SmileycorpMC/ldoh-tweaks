package net.smileycorp.ldoh.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityFlying;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.world.BlockStateCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityFlying.class)
public class MixinEntityFlying {

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState travel$getBlockState(World world, BlockPos pos) {
        return BlockStateCache.getBlockState(world, pos);
    }

}
