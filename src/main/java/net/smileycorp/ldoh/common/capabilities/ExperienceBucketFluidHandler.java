package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.smileycorp.ldoh.common.fluid.LDOHFluids;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ExperienceBucketFluidHandler implements IFluidHandlerItem, ICapabilityProvider {
    
    private ItemStack container;
    
    public ExperienceBucketFluidHandler(ItemStack container) {
        this.container = container;
    }
    
    @Nonnull
    @Override
    public ItemStack getContainer() {
        return container;
    }
    
    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new FluidTankProperties[] { new FluidTankProperties(new FluidStack(LDOHFluids.EXPERIENCE, Fluid.BUCKET_VOLUME), Fluid.BUCKET_VOLUME)};
    }
    
    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return resource.amount;
    }
    
    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (container.getCount() != 1 || resource == null || resource.amount < Fluid.BUCKET_VOLUME || resource.getFluid() != LDOHFluids.EXPERIENCE) return null;
        if (doDrain) container = new ItemStack(Items.BUCKET);
        return new FluidStack(LDOHFluids.EXPERIENCE, Fluid.BUCKET_VOLUME);
    }
    
    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (container.getCount() != 1 || maxDrain < Fluid.BUCKET_VOLUME) return null;
        container = new ItemStack(Items.BUCKET);
        return new FluidStack(LDOHFluids.EXPERIENCE, Fluid.BUCKET_VOLUME);
    }
    
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
    }
    
    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY ? CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(this) : null;
    }
    
}
