package net.smileycorp.ldoh.common.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.IApocalypse;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;

public class ApocalypseEvents {

	//capability manager
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		//spawner instance for boss event
		if (!entity.hasCapability(LDOHCapabilities.APOCALYPSE, null) && entity instanceof EntityPlayer &!(entity instanceof FakePlayer)) {
			event.addCapability(ModDefinitions.getResource("Apocalypse"), new IApocalypse.Provider((EntityPlayer) entity));
		}
	}

	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		if (player != null) {
			World world = player.world;
			if (!world.isRemote && world.getGameRules().getBoolean("doDaylightCycle")) {
				IApocalypse apocalypse = player.getCapability(LDOHCapabilities.APOCALYPSE, null);
				if (apocalypse.canStart(world)) apocalypse.startEvent();
				if (apocalypse.isActive(world)) apocalypse.update(world);
			}
		}
	}

}
