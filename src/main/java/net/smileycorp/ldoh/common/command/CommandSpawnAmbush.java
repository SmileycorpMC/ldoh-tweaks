package net.smileycorp.ldoh.common.command;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.IAmbushEvent.Type;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandSpawnAmbush extends CommandBase {

    @Override
    public String getName() {
        return "spawnAmbush";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands." + ModDefinitions.MODID + ".SpawnAmbush.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 2) throw new CommandException("commands." + ModDefinitions.MODID + ".SpawnAmbush.usage");
        try {
            Entity entity = sender.getCommandSenderEntity();
            Type type = Type.valueOf(args[0]);
            if (type == null || type == Type.NONE)
                throw new CommandException("commands." + ModDefinitions.MODID + ".SpawnAmbush.invalidValue", new TextComponentTranslation(args[0]));
            if (type == Type.ALLY || type == Type.ENEMY) {
                if (entity.getTeam() == null)
                    throw new CommandException("commands." + ModDefinitions.MODID + ".SpawnAmbush.teamFail", new TextComponentTranslation(args[0]));
                else if (!(entity.getTeam().getName().equals("RED") || entity.getTeam().getName().equals("BLU")))
                    throw new CommandException("commands." + ModDefinitions.MODID + ".SpawnAmbush.teamFail", new TextComponentTranslation(args[0]));
            }
            int level = parseInt(args[1]);
            server.addScheduledTask(() -> {
                EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
                if (player.hasCapability(LDOHCapabilities.AMBUSH, null))
                    player.getCapability(LDOHCapabilities.AMBUSH, null).spawnAmbush(player, type, level);
            });
            notifyCommandListener(sender, this, "commands." + ModDefinitions.MODID + ".SpawnAmbush.success", new TextComponentTranslation(args[0]));
        } catch (NumberInvalidException e) {
            throw new CommandException("commands." + ModDefinitions.MODID + ".SpawnAmbush.invalidValue", new TextComponentTranslation(args[1]));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) return Arrays.asList("ALLY", "ENEMY", "ZOMBIE", "PARASITE");
        else if (args.length == 2) {
            List<String> list = Lists.newArrayList();
            for (int i = 0; i < 12; i++) list.add(String.valueOf(i));
            return list;
        }
        return Lists.newArrayList();
    }

}
