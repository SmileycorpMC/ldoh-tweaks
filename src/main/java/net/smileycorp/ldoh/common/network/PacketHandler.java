package net.smileycorp.ldoh.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.SimpleStringMessage;
import net.smileycorp.ldoh.client.ClientHandler;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.ICuring;
import net.smileycorp.ldoh.common.capabilities.IHunger;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;

public class PacketHandler {

	public static final SimpleNetworkWrapper NETWORK_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModDefinitions.modid);

	public static void initPackets() {
		NETWORK_INSTANCE.registerMessage(ClientSyncHandlerAction.class, SimpleStringMessage.class, 0, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(ClientSyncFood.class, SyncFoodMessage.class, 1, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(ClientSyncHunger.class, SyncHungerMessage.class, 2, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(ClientStartEat.class, StartEatingMessage.class, 3, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(ClientSyncSyringes.class, SyncSyringesMessage.class, 4, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(ClientSyncSleep.class, SyncSleepMessage.class, 5, Side.CLIENT);
	}

	public static class ClientSyncHandlerAction implements IMessageHandler<SimpleStringMessage, IMessage> {

		public ClientSyncHandlerAction() {}

		@Override
		public IMessage onMessage(SimpleStringMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					ClientHandler.displayActionBar(message.getText());
				});
			}
			return null;
		}
	}

	public static class ClientSyncFood implements IMessageHandler<SyncFoodMessage, IMessage> {

		public ClientSyncFood() {}

		@Override
		public IMessage onMessage(SyncFoodMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					Entity entity = message.getEntity(Minecraft.getMinecraft().world);
					if (entity!=null) if (entity.hasCapability(LDOHCapabilities.HUNGER, null)) {
						IHunger hunger = entity.getCapability(LDOHCapabilities.HUNGER, null);
						hunger.setFoodStack(message.getStack());
					}
				});
			}
			return null;
		}
	}

	public static class ClientSyncHunger implements IMessageHandler<SyncHungerMessage, IMessage> {

		public ClientSyncHunger() {}

		@Override
		public IMessage onMessage(SyncHungerMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					Entity entity = message.getEntity(Minecraft.getMinecraft().world);
					if (entity!=null) if (entity.hasCapability(LDOHCapabilities.HUNGER, null)) {
						IHunger hunger = entity.getCapability(LDOHCapabilities.HUNGER, null);
						hunger.setFoodLevel(message.getHunger());
					}
				});
			}
			return null;
		}
	}

	public static class ClientStartEat implements IMessageHandler<StartEatingMessage, IMessage> {

		public ClientStartEat() {}

		@Override
		public IMessage onMessage(StartEatingMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					Entity entity = message.getEntity(Minecraft.getMinecraft().world);
					if (entity!=null) if (entity.hasCapability(LDOHCapabilities.HUNGER, null)) entity.getCapability(LDOHCapabilities.HUNGER, null).startEating((EntityLiving) entity);
				});
			}
			return null;
		}
	}

	public static class ClientSyncSyringes implements IMessageHandler<SyncSyringesMessage, IMessage> {

		public ClientSyncSyringes() {}

		@Override
		public IMessage onMessage(SyncSyringesMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					Entity entity = message.getEntity(Minecraft.getMinecraft().world);
					if (entity!=null) if (entity.hasCapability(LDOHCapabilities.CURING, null)) {
						ICuring curing = entity.getCapability(LDOHCapabilities.CURING, null);
						curing.setSyringeCount(message.getCount());
					}
				});
			}
			return null;
		}
	}

	public static class ClientSyncSleep implements IMessageHandler<SyncSleepMessage, IMessage> {

		public ClientSyncSleep() {}

		@Override
		public IMessage onMessage(SyncSleepMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					Entity entity = message.getEntity(Minecraft.getMinecraft().world);
					if (entity!=null) if (entity.hasCapability(LDOHCapabilities.EXHAUSTION, null)) entity.getCapability(LDOHCapabilities.EXHAUSTION, null).setSleeping((EntityLiving) entity, message.isSleeping());
				});
			}
			return null;
		}
	}

}
