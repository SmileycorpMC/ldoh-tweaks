package net.smileycorp.ldoh.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.smileycorp.ldoh.common.ModDefinitions;

public class CommandHandDebug extends CommandBase {

    @Override
    public String getName() {
        return "handDebug";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands." + ModDefinitions.MODID + ".handDebug.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        server.addScheduledTask(() -> {
            if (sender.getCommandSenderEntity() instanceof EntityLiving) {
                EntityLiving entity = (EntityLiving) sender.getCommandSenderEntity();
                ItemStack stack = entity.getHeldItemMainhand();
                if (stack != null) {
                    notifyCommandListener(sender, this, stack.toString());
                    notifyCommandListener(sender, this, stack.getItem().getClass().toString());
                }
            }
        });
        //notifyCommandListener(sender, this, "commands."+ModDefinitions.MODID+".StartBossEvent.success", new Object[0]);
    }

}
