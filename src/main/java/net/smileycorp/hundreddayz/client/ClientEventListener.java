package net.smileycorp.hundreddayz.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.hundreddayz.common.ModDefinitions;

@EventBusSubscriber(modid=ModDefinitions.modid, value=Side.CLIENT)
public class ClientEventListener {
	
	public static String title = "";
	public static int starttime = 0;

	public static void displayTitle(String text, int day) {
		Minecraft mc = Minecraft.getMinecraft();
		ITextComponent message = new TextComponentTranslation(text, new Object[]{100-day});
		message.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_RED));
		mc.ingameGUI.displayTitle(message.getFormattedText(), null, 10, 20, 10);;
	}
	
	public static void displayActionBar(String text) {
		Minecraft mc = Minecraft.getMinecraft();
		ITextComponent message = new TextComponentTranslation(text);
		message.setStyle(new Style().setBold(true).setColor(TextFormatting.YELLOW));
		mc.ingameGUI.setOverlayMessage(message, true);
	}
	
	/*@SubscribeEvent
	public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
		starttime=0;
	}*/

	/*@SubscribeEvent
	public void drawTitle(RenderGameOverlayEvent.Text event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (starttime > 0) {
			ScaledResolution resoloution = event.getResolution();
			int width = resoloution.getScaledWidth();
			int height = resoloution.getScaledHeight();
			mc.fontRenderer.drawSplitString(title, width/2, height/2, width/2, 0x440002);
			starttime--;
		}
	}*/
	
	/*@SubscribeEvent
	public void drawParticle(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		World world = player.world;
		if (player.posY < 50 && player.posY >= 30) {
			for (int i = -8; i<9; i++) {
				for (int j = -8; j<9; j++) {
					world.spawnParticle(EnumParticleTypes.CLOUD, player.posX+i, 30, player.posX+j, 0f, 1f, 0f);
				}
			}
		} else if (player.posY < 30) {
			world.spawnParticle(EnumParticleTypes.CLOUD, player.posX, player.posY+1, player.posX, 0f, 1f, 0f);
		}
	}*/
	
}
