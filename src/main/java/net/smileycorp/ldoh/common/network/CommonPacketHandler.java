package net.smileycorp.ldoh.common.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.SimpleStringMessage;
import net.smileycorp.ldoh.client.ClientHandler;
import net.smileycorp.ldoh.common.ModDefinitions;

public class CommonPacketHandler {
	
	public static final SimpleNetworkWrapper NETWORK_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModDefinitions.modid);
	
	public static void initPackets() {
		NETWORK_INSTANCE.registerMessage(ClientSyncHandlerTitle.class, LDOHMessage.class, 0, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(ClientSyncHandlerAction.class, SimpleStringMessage.class, 1, Side.CLIENT);
	}
	
	public static class ClientSyncHandlerTitle implements IMessageHandler<LDOHMessage, IMessage> {

		public ClientSyncHandlerTitle() {}

		@Override
		public IMessage onMessage(LDOHMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					ClientHandler.displayTitle(message.getText(), message.getDay());
				});
			}
			return null;
		}
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
}
