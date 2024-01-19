package net.smileycorp.ldoh.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.world.BlockStateCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {

    @Shadow @Final public boolean isRemote;

    @Inject(at=@At("TAIL"), method = "updateEntities")
    public void setBlockState(CallbackInfo callback) {
        if (isRemote) return;
        BlockStateCache.clear();
    }

    @Inject(at=@At("HEAD"), method = "getBlockState", cancellable = true)
    public void getBlockState(BlockPos pos, CallbackInfoReturnable<IBlockState> callback) {
        if (isRemote) return;
        callback.setReturnValue(BlockStateCache.getBlockState((World)(Object)this, pos));
    }

}
