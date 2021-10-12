package net.smileycorp.hundreddayz.common.apocalypse;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.smileycorp.hundreddayz.common.ModDefinitions;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityParasiteBase;

@EventBusSubscriber(modid=ModDefinitions.modid)
public class ApocalypseEventListener {
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		Entity entity = event.getEntity();
		if (!world.isRemote) {
			if (entity instanceof EntityParasiteBase && world.getWorldTime() >= 2418000) {
				EntityParasiteBase boss = (EntityParasiteBase) entity;
				boss.targetTasks.taskEntries.clear();
				boss.targetTasks.addTask(1, new EntityAIHurtByTarget(boss, true, new Class[] {EntityParasiteBase.class}));
				boss.targetTasks.addTask(2, new EntityAINearestAttackableTarget(boss, EntityPlayer.class, false));
				boss.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(150.0D);
			}
		}
	}
	
	@SubscribeEvent
	public void worldTick(WorldTickEvent event) {
		World world = event.world;
		if (!world.isRemote) {
			int day = (int) Math.floor(world.getWorldTime()/24000);
			int time = Math.round(world.getWorldTime()%24000);
			WorldSaveApocalypseEvent data = WorldSaveApocalypseEvent.get(world);
			/*if (time == 0) {
				if (day == 100) {
					CommonPacketHandler.NETWORK_INSTANCE.sendToAll(new HundredDayzMessage(day, ModDefinitions.finalDayMessage));
				} else if (day == 50 || (day >=75 && day < 100)){
					CommonPacketHandler.NETWORK_INSTANCE.sendToAll(new HundredDayzMessage(day, ModDefinitions.dayCountMessage));
				}
			}*/
			if (time == 18000 && day == 100) {
				for (ApocalypseBossEvent bossEvent: data.getEvents()) {
					if (!bossEvent.isActive(world)) {
						bossEvent.startEvent();
						if (bossEvent.hasChanged()) {
							data.markDirty();
						}
					}
				}
			}
			for (ApocalypseBossEvent bossEvent: data.getEvents()) {
				if (bossEvent.isActive(world)) {
					bossEvent.update(world);
					if (bossEvent.hasChanged()) {
						data.markDirty();
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerJoin(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		World world = player.world;
		if (!player.world.isRemote) {
			WorldSaveApocalypseEvent data = WorldSaveApocalypseEvent.get(world);
			data.getEventForPlayer(player);
			/*if (player instanceof EntityPlayerMP) {
				int day = Math.round(world.getWorldTime()/24000);
				int time = Math.round(world.getWorldTime()%24000);
				if (day < 100) {
					CommonPacketHandler.NETWORK_INSTANCE.sendTo(new HundredDayzMessage(0, ModDefinitions.dayCountMessage), (EntityPlayerMP) player);

				} else if (day==100 && time < 12000) {
					CommonPacketHandler.NETWORK_INSTANCE.sendTo(new HundredDayzMessage(0, ModDefinitions.finalDayMessage), (EntityPlayerMP) player);
				}
			}*/
		}
	}
}
