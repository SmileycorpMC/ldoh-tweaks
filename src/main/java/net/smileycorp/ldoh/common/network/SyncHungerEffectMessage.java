package net.smileycorp.ldoh.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncHungerEffectMessage implements IMessage {

	private int entity;
	private boolean hasHunger;

	public SyncHungerEffectMessage() {}

	public SyncHungerEffectMessage(Entity entity, boolean hasHunger) {
		this.entity = entity.getEntityId();
		this.hasHunger = hasHunger;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entity);
		buf.writeBoolean(hasHunger);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entity = buf.readInt();
		hasHunger = buf.readBoolean();
	}

	public Entity getEntity(World world) {
		return world.getEntityByID(entity);
	}

	public boolean hasHunger() {
		return hasHunger;
	}

}
