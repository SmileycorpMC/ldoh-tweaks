package net.smileycorp.ldoh.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.ldoh.common.world.BlockStateCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WalkNodeProcessor.class)
public class MixinWalkNodeProcessor {

    @Redirect(method = "getStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState getStart$getBlockState(IBlockAccess access, BlockPos pos) {
        return BlockStateCache.getBlockState(access, pos);
    }
    @Redirect(method = "findPathOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState findPathOptions$getBlockState(IBlockAccess access, BlockPos pos) {
        return BlockStateCache.getBlockState(access, pos);
    }
    @Redirect(method = "getSafePoint", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState getSafePoint$getBlockState(IBlockAccess access, BlockPos pos) {
        return BlockStateCache.getBlockState(access, pos);
    }

    @Redirect(method = "getPathNodeType(Lnet/minecraft/world/IBlockAccess;III)Lnet/minecraft/pathfinding/PathNodeType;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState getPathNodeType$getBlockState(IBlockAccess access, BlockPos pos) {
        return BlockStateCache.getBlockState(access, pos);
    }

    @Redirect(method = "getPathNodeType(Lnet/minecraft/world/IBlockAccess;IIIIIIZZLjava/util/EnumSet;Lnet/minecraft/pathfinding/PathNodeType;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/pathfinding/PathNodeType;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState getPathNodeType1$getBlockState(IBlockAccess access, BlockPos pos) {
        return BlockStateCache.getBlockState(access, pos);
    }

    @Redirect(method = "checkNeighborBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState checkNeighborBlocks$getBlockState(IBlockAccess access, BlockPos pos) {
        return BlockStateCache.getBlockState(access, pos);
    }

    @Redirect(method = "getPathNodeTypeRaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState getPathNodeTypeRaw$getBlockState(IBlockAccess access, BlockPos pos) {
        return BlockStateCache.getBlockState(access, pos);
    }

}
