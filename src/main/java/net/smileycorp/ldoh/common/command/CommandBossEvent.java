package net.smileycorp.ldoh.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.smileycorp.ldoh.common.ModContent;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.IApocalypse;

public class CommandBossEvent extends CommandBase {

	@Override
	public String getName() {
		return "startBossEvent";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands."+ModDefinitions.modid+".StartBossEvent.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		server.addScheduledTask(() -> {
			for (EntityPlayer player : server.getPlayerList().getPlayers()) {
				if (player.hasCapability(ModContent.APOCALYPSE, null)) {
					IApocalypse apocalypse = player.getCapability(ModContent.APOCALYPSE, null);
					apocalypse.startEvent();
				}
			}
		});
		notifyCommandListener(sender, this, "commands."+ModDefinitions.modid+".StartBossEvent.success", new Object[0]);
	}

}
