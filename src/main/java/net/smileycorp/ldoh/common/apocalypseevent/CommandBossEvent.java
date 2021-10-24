package net.smileycorp.ldoh.common.apocalypseevent;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.ModDefinitions;

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
		World world = sender.getEntityWorld();
		server.addScheduledTask(() -> {
			WorldSaveApocalypseEvent data = WorldSaveApocalypseEvent.get(world);
			for (ApocalypseBossEvent event : data.getEvents()) {
				if (!event.isActive(world)) {
					event.startEvent();
				}
			}
				
		});
		notifyCommandListener(sender, this, "commands."+ModDefinitions.modid+".StartBossEvent.success", new Object[0]);
	}
 
}
