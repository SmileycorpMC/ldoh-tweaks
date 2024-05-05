package net.smileycorp.ldoh.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncHungerMessage implements IMessage {

    private int entity, hunger;

    public SyncHungerMessage() {}

    public SyncHungerMessage(Entity entity, int hunger) {
        this.entity = entity.getEntityId();
        this.hunger = hunger;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entity);
        buf.writeInt(hunger);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entity = buf.readInt();
        hunger = buf.readInt();
    }

    public Entity getEntity(World world) {
        return world.getEntityByID(entity);
    }

    public int getHunger() {
        return hunger;
    }

}
