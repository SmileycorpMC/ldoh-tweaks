package net.smileycorp.ldoh.mixin;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.smileycorp.ldoh.common.fluid.LDOHFluids;
import net.smileycorp.ldoh.common.item.LDOHItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;

@Mixin(value = FluidBucketWrapper.class)
public class MixinFluidBucketWrapper {
    
    @Shadow(remap = false) @Nonnull protected ItemStack container;
    
    @Inject(at = @At("HEAD"), method = "canFillFluidType", remap = false, cancellable = true)
    private void canFillFluidType(FluidStack fluidStack, CallbackInfoReturnable<Boolean> callback) {
        System.out.println("poopa stinka1");
        if (fluidStack.getFluid() == LDOHFluids.EXPERIENCE) callback.setReturnValue(true);
    }
    
    @Inject(at = @At("HEAD"), method = "getFluid", remap = false, cancellable = true)
    private void getFluid(CallbackInfoReturnable<FluidStack> callback) {
        System.out.println("poopa stinka2");
        if (container.getItem() == LDOHItems.EXPERIENCE_BUCKET) callback.setReturnValue(new FluidStack(LDOHFluids.EXPERIENCE, Fluid.BUCKET_VOLUME));
    }
    
    
}
