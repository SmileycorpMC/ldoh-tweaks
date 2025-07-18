package net.smileycorp.ldoh.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncFoodMessage implements IMessage {

    private int entity;
    private ItemStack stack;

    public SyncFoodMessage() {}

    public SyncFoodMessage(Entity entity, ItemStack stack) {
        this.entity = entity.getEntityId();
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entity);
        ByteBufUtils.writeItemStack(buf, stack);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entity = buf.readInt();
        stack = ByteBufUtils.readItemStack(buf);
    }

    public Entity getEntity(World world) {
        return world.getEntityByID(entity);
    }

    public ItemStack getStack() {
        return stack;
    }
}
