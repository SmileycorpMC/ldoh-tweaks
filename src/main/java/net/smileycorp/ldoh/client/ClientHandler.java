package net.smileycorp.ldoh.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

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

}
