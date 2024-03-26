package net.smileycorp.ldoh.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.capabilities.IApocalypse;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;

public class CommandBossEvent extends CommandBase {

    @Override
    public String getName() {
        return "startBossEvent";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands." + Constants.MODID + ".StartBossEvent.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        server.addScheduledTask(() -> {
            for (EntityPlayer player : server.getPlayerList().getPlayers()) {
                if (player.hasCapability(LDOHCapabilities.APOCALYPSE, null)) {
                    IApocalypse apocalypse = player.getCapability(LDOHCapabilities.APOCALYPSE, null);
                    apocalypse.startEvent();
                }
            }
        });
        notifyCommandListener(sender, this, "commands." + Constants.MODID + ".StartBossEvent.success", new Object[0]);
    }

}
