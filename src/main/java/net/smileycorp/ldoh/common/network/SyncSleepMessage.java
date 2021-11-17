package net.smileycorp.ldoh.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncSleepMessage implements IMessage {

	private int entity;
	private boolean sleeping;

	public SyncSleepMessage() {}

	public SyncSleepMessage(Entity entity, boolean sleeping) {
		this.entity = entity.getEntityId();
		this.sleeping = sleeping;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entity);
		buf.writeBoolean(sleeping);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entity = buf.readInt();
		sleeping = buf.readBoolean();
	}

	public Entity getEntity(World world) {
		return world.getEntityByID(entity);
	}

	public boolean isSleeping() {
		return sleeping;
	}

}
