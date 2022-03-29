package net.smileycorp.ldoh.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.smileycorp.hordes.infection.CureEntityMessage;
import net.smileycorp.hordes.infection.HordesInfection;
import net.smileycorp.ldoh.common.capabilities.ICuring;
import net.smileycorp.ldoh.common.capabilities.IHunger;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.smileycorp.ldoh.common.network.StartEatingMessage;
import net.smileycorp.ldoh.common.network.SyncFoodMessage;
import net.smileycorp.ldoh.common.network.SyncHungerEffectMessage;
import net.smileycorp.ldoh.common.network.SyncHungerMessage;
import net.smileycorp.ldoh.common.network.SyncMedicCureMessage;
import net.smileycorp.ldoh.common.network.SyncSleepMessage;
import net.smileycorp.ldoh.common.network.SyncSyringesMessage;

public class ClientHandler {

	//show title with the specified text
	public static void displayTitle(String text, int day) {
		Minecraft mc = Minecraft.getMinecraft();
		ITextComponent message = new TextComponentTranslation(text, new Object[]{100-day});
		message.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_RED));
		mc.ingameGUI.displayTitle(message.getFormattedText(), null, 10, 20, 10);
	}

	//show action bar message with the specified text
	public static void displayActionBar(String text) {
		Minecraft mc = Minecraft.getMinecraft();
		ITextComponent message = new TextComponentTranslation(text);
		message.setStyle(new Style().setBold(true).setColor(TextFormatting.YELLOW));
		mc.ingameGUI.setOverlayMessage(message, true);
	}

	//sync entity food item to client
	public static void syncFood(SyncFoodMessage message) {
		Entity entity = message.getEntity(Minecraft.getMinecraft().world);
		if (entity!=null) if (entity.hasCapability(LDOHCapabilities.HUNGER, null)) {
			IHunger hunger = entity.getCapability(LDOHCapabilities.HUNGER, null);
			hunger.setFoodStack(message.getStack());
		}
	}

	//sync entity hunger value to client
	public static void syncHunger(SyncHungerMessage message) {
		Entity entity = message.getEntity(Minecraft.getMinecraft().world);
		if (entity!=null) if (entity.hasCapability(LDOHCapabilities.HUNGER, null)) {
			IHunger hunger = entity.getCapability(LDOHCapabilities.HUNGER, null);
			hunger.setFoodLevel(message.getHunger());
		}
	}

	//sync entity eating to client
	public static void startEating(StartEatingMessage message) {
		Entity entity = message.getEntity(Minecraft.getMinecraft().world);
		if (entity!=null) if (entity.hasCapability(LDOHCapabilities.HUNGER, null)) entity.getCapability(LDOHCapabilities.HUNGER, null).startEating((EntityLiving) entity);
	}

	//sync medic syringe count to client
	public static void syncSyringes(SyncSyringesMessage message) {
		Entity entity = message.getEntity(Minecraft.getMinecraft().world);
		if (entity!=null) if (entity.hasCapability(LDOHCapabilities.CURING, null)) {
			ICuring curing = entity.getCapability(LDOHCapabilities.CURING, null);
			curing.setSyringeCount(message.getCount());
		}
	}

	//sync entity sleeping to client
	public static void syncSleeping(SyncSleepMessage message) {
		Entity entity = message.getEntity(Minecraft.getMinecraft().world);
		if (entity!=null) if (entity.hasCapability(LDOHCapabilities.EXHAUSTION, null))
			entity.getCapability(LDOHCapabilities.EXHAUSTION, null).setSleeping((EntityLiving) entity, message.isSleeping());
	}

	//sync medic curing an entity to client
	public static void syncMedicCure(SyncMedicCureMessage message) {
		Entity entity = message.getEntity(Minecraft.getMinecraft().world);
		if (entity instanceof EntityLiving) {
			((EntityLiving) entity).removePotionEffect(HordesInfection.INFECTED);
			net.smileycorp.hordes.client.ClientHandler.processCureEntityMessage(new CureEntityMessage(entity));
		}
	}

	//sync the hunger potion effect to the client
	public static void syncHungerEffect(SyncHungerEffectMessage message) {
		Entity entity = message.getEntity(Minecraft.getMinecraft().world);
		if (entity!=null) if (entity.hasCapability(LDOHCapabilities.HUNGER, null)) {
			IHunger hunger = entity.getCapability(LDOHCapabilities.HUNGER, null);
			hunger.setHungerEffect((EntityLiving) entity, message.hasHunger());
		}
	}

}
