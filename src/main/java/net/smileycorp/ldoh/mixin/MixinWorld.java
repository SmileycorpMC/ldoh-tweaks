package net.smileycorp.ldoh.mixin;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.events.PlayerEvents;
import net.smileycorp.ldoh.common.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {
    
    //this is a gross hack, but it makes sleeping overhaul think cfm beds are vanilla beds
    @Inject(at = @At("TAIL"), method = "getBlockState", cancellable = true)
    public void getBlockState(BlockPos pos, CallbackInfoReturnable<IBlockState> callback) {
        if (PlayerEvents.BED_POS != pos) return;
        IBlockState state = callback.getReturnValue();
        if (ModUtils.isBed(state)) callback.setReturnValue(Blocks.BED.getDefaultState().withProperty(BlockHorizontal.FACING, state.getValue(BlockHorizontal.FACING)));
        PlayerEvents.BED_POS = null;
    }

}
