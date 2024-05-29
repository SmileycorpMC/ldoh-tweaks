package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.fluid.LDOHFluids;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BottleFluidHandler implements IFluidHandlerItem, ICapabilityProvider {
    
    private ItemStack container;
    
    public BottleFluidHandler(ItemStack container) {
        this.container = container;
    }
    
    @Nonnull
    @Override
    public ItemStack getContainer() {
        return container;
    }
    
    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new FluidTankProperties[] { new FluidTankProperties(getContainedFluid(), Constants.BOTTLE_VOLUME)};
    }
    
    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (container.getItem() == Items.GLASS_BOTTLE) {
            if (resource.getFluid() == FluidRegistry.WATER) container = new ItemStack(Items.POTIONITEM);
            if (resource.getFluid() == LDOHFluids.EXPERIENCE) container = new ItemStack(Items.EXPERIENCE_BOTTLE);
        }
        return Constants.BOTTLE_VOLUME;
    }
    
    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        FluidStack fluid = getContainedFluid();
        if (container.getCount() != 1 || resource == null || resource.amount < Constants.BOTTLE_VOLUME || fluid.isFluidEqual(resource)) return null;
        if (doDrain) container = new ItemStack(Items.GLASS_BOTTLE);
        return fluid;
    }
    
    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (container.getCount() != 1 || maxDrain < Constants.BOTTLE_VOLUME) return null;
        container = new ItemStack(Items.GLASS_BOTTLE);
        return getContainedFluid();
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
    
    public FluidStack getContainedFluid() {
        if (container.getItem() == Items.EXPERIENCE_BOTTLE) return new FluidStack(LDOHFluids.EXPERIENCE, Constants.BOTTLE_VOLUME);
        if (container.getItem() == Items.POTIONITEM) {
            PotionType potion = PotionUtils.getPotionFromItem(container);
            if (potion == PotionTypes.WATER) return new FluidStack(FluidRegistry.WATER, Constants.BOTTLE_VOLUME);
            /*else {
                NBTTagCompound nbt = new NBTTagCompound();
                NBTTagCompound containerTag = container.getTagCompound();
                if (containerTag != null && containerTag.hasKey("Potion")) nbt.setString("Potion", containerTag.getString("Potion"));
               // return new FluidStack(LDOHFluids.POTION, Constants.BOTTLE_VOLUME, nbt);
            }*/
        }
        return null;
    }
    
}
