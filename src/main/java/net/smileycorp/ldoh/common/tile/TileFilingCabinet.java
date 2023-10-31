package net.smileycorp.ldoh.common.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.smileycorp.atlas.api.util.RecipeUtils;

import javax.annotation.Nullable;

public class TileFilingCabinet extends TileEntity implements IInventory {

    protected ItemStack item = ItemStack.EMPTY;
    protected int count = 0;
    protected String customName;
    protected final IItemHandler inventory = new InvWrapper(this);

    @Override
    public String getName() {
        return hasCustomName() ? customName : "container.ldoh.FilingCabinet";
    }

    @Override
    public boolean hasCustomName() {
        return customName != null && !customName.isEmpty();
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) ? (T) inventory
                : super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || super.hasCapability(capability, facing);
    }

    public ItemStack getContainedItem() {
        if (count <= 0 & !item.isEmpty()) item = ItemStack.EMPTY;
        return item.copy();
    }

    public int getCurrentCount() {
        return count;
    }

    public int getMaxCount() {
        if (!item.isEmpty()) {
            return item.getMaxStackSize() * 64;
        }
        return 4096;
    }

    @Override
    public int getSizeInventory() {
        return 64;
    }

    @Override
    public boolean isEmpty() {
        return item.isEmpty() || count == 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (isEmpty() || slot >= 64) return ItemStack.EMPTY;
        int slotCount = getSlotCount(slot);
        if (slotCount <= 0) return ItemStack.EMPTY;
        ItemStack stack = item.copy();
        stack.setCount(slotCount);
        return stack;
    }

    private int getSlotCount(int slot) {
        int lastSlot = Math.floorDiv(count, item.getMaxStackSize());
        if (lastSlot > slot) return item.getMaxStackSize();
        return lastSlot == slot ? count % item.getMaxStackSize() : 0;
    }

    public boolean canInsert(ItemStack stack) {
        return isEmpty() || (stack.getItem() == item.getItem() && stack.getMetadata() == item.getMetadata()
                && stack.getTagCompound() == item.getTagCompound() && count < getMaxCount());
    }

    public ItemStack extractItem(int amount) {
        ItemStack stack = item.copy();
        if (count < amount) amount = count;
        count -= amount;
        if (count == 0) item = ItemStack.EMPTY;
        stack.setCount(amount);
        markDirty();
        return stack;
    }

    public void insertItem(ItemStack stack) {
        int newCount = stack.getCount() + count;
        if (count == 0 && newCount > 0) {
            item = stack.copy();
            item.setCount(1);
        }
        if (newCount > getMaxCount()) {
            stack.setCount(newCount - count);
            count = getMaxCount();
        } else {
            stack.setCount(0);
            count = newCount;
        }
        markDirty();
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        if (isEmpty()) return ItemStack.EMPTY;
        ItemStack stack = item.copy();
        int size = item.getMaxStackSize();
        int total = this.count - count;
        if (total < 0) {
            stack.setCount(count + total);
            item = ItemStack.EMPTY;
            this.count = 0;
            return stack;
        }
        stack.setCount(count);
        this.count = total;
        if (count <= 0) item = ItemStack.EMPTY;
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack stack = item.copy();
        if (count > 64) {
            count -= 64;
            stack.setCount(64);
        } else {
            stack = ItemStack.EMPTY;
        }
        if (count <= 0) item = ItemStack.EMPTY;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot >= 64) return;
        if (isEmpty()) {
            count = stack.getCount();
            item = stack.copy();
            item.setCount(1);
        } else if (RecipeUtils.compareItemStacks(stack, item, true)) {
            count = count + stack.getCount() - getStackInSlot(slot).getCount();
            if (count <= 0) item = ItemStack.EMPTY;
        }
    }

    public void markDirty() {
        IBlockState state = world.getBlockState(pos);
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
        super.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (isEmpty()) return true;
        if (!RecipeUtils.compareItemStacks(stack, item, true)) return false;
        int total = count + stack.getCount();
        return total <= getMaxCount();
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        item = ItemStack.EMPTY;
        count = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
        if (compound.hasKey("items")) {
            NBTTagCompound items = compound.getCompoundTag("items");
            count = items.getInteger("Count");
            items.setByte("Count", (byte) 1);
            item = new ItemStack(items);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
        if (!this.isEmpty()) {
            NBTTagCompound items = item.serializeNBT();
            items.setInteger("Count", count);
            compound.setTag("items", items);
        }
        return compound;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound compound) {
        readFromNBT(compound);
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }

    public void dropContents() {
        if (isEmpty()) return;
        int stackCount = Math.floorDiv(count, item.getMaxStackSize()) - 1;
        if (stackCount > 0) for (int i = 0; i <= stackCount; i++) {
            ItemStack stack = item.copy();
            stack.setCount(i == stackCount ? count % item.getMaxStackSize() : item.getMaxStackSize());
            EntityItem entityitem = new EntityItem(this.world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, stack);
            entityitem.setDefaultPickupDelay();
            world.spawnEntity(entityitem);
        }
    }

}
