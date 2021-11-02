package net.smileycorp.ldoh.common.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.smileycorp.ldoh.common.ModContent;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.IApocalypse;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityParasiteBase;

public class ApocalypseEvents {

	//capability manager
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		//spawner instance for boss event
		if (!entity.hasCapability(ModContent.APOCALYPSE, null) && entity instanceof EntityPlayer &!(entity instanceof FakePlayer)) {
			event.addCapability(ModDefinitions.getResource("Apocalypse"), new IApocalypse.Provider((EntityPlayer) entity));
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		World world = event.getWorld();
		Entity entity = event.getEntity();
		if (!world.isRemote) {
			if (entity instanceof EntityParasiteBase && world.getWorldTime() >= 2418000) {
				EntityParasiteBase boss = (EntityParasiteBase) entity;
				boss.targetTasks.taskEntries.clear();
				boss.targetTasks.addTask(1, new EntityAIHurtByTarget(boss, true, new Class[] {EntityParasiteBase.class}));
				boss.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(boss, EntityPlayer.class, false));
				boss.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(150.0D);
			}
		}
	}

	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		if (player != null) {
			World world = player.world;
			if (!world.isRemote) {
				IApocalypse apocalypse = player.getCapability(ModContent.APOCALYPSE, null);
				if (apocalypse.canStart(world)) apocalypse.startEvent();
				if (apocalypse.isActive(world)) apocalypse.update(world);
			}
		}
	}

}
