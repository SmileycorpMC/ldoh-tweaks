package net.smileycorp.ldoh.mixin;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.smileycorp.ldoh.common.fluid.LDOHFluids;
import net.smileycorp.ldoh.common.item.LDOHItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FluidUtil.class)
public class MixinFluidUtil {
    
    @Inject(at = @At("HEAD"), method = "getFilledBucket", remap = false, cancellable = true)
    private static void getFilledBucket(FluidStack fluidStack, CallbackInfoReturnable<ItemStack> callback) {
        if (fluidStack.getFluid() == LDOHFluids.EXPERIENCE) callback.setReturnValue(new ItemStack(LDOHItems.EXPERIENCE_BUCKET));
    }
    
}
