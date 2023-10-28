package net.smileycorp.ldoh.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncSyringesMessage implements IMessage {

    private int entity, count;

    public SyncSyringesMessage() {
    }

    public SyncSyringesMessage(Entity entity, int count) {
        this.entity = entity.getEntityId();
        this.count = count;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entity);
        buf.writeInt(count);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entity = buf.readInt();
        count = buf.readInt();
    }

    public Entity getEntity(World world) {
        return world.getEntityByID(entity);
    }

    public int getCount() {
        return count;
    }

}
