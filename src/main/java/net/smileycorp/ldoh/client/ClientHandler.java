package net.smileycorp.ldoh.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.smileycorp.ldoh.client.tesr.TESRBarbedWire;
import net.smileycorp.ldoh.client.tesr.TESRTurretItem;
import net.smileycorp.ldoh.common.block.LDOHBlocks;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;

public class ClientHandler {

	public static final TileEntityItemStackRenderer TURRET_TESR = new TESRTurretItem();

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

	public static void registerTESRS() {
		//register renderer for barbed wire healthbar
		ClientRegistry.bindTileEntitySpecialRenderer(TileBarbedWire.class, new TESRBarbedWire());
		//register turret item renderer
		Item.getItemFromBlock(LDOHBlocks.TURRET).setTileEntityItemStackRenderer(TURRET_TESR);
	}

}
