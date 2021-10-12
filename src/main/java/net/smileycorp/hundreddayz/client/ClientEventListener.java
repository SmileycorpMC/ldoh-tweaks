package net.smileycorp.hundreddayz.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.hundreddayz.common.ModContent;
import net.smileycorp.hundreddayz.common.ModDefinitions;
import net.smileycorp.hundreddayz.common.block.BlockBarbedWire;
import net.smileycorp.hundreddayz.common.tile.TileBarbedWire;

@EventBusSubscriber(modid=ModDefinitions.modid, value=Side.CLIENT)
public class ClientEventListener {
	
	public static String title = "";
	public static int starttime = 0;

	public static void displayTitle(String text, int day) {
		Minecraft mc = Minecraft.getMinecraft();
		ITextComponent message = new TextComponentTranslation(text, new Object[]{100-day});
		message.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_RED));
		mc.ingameGUI.displayTitle(message.getFormattedText(), null, 10, 20, 10);
	}
	
	public static void displayActionBar(String text) {
		Minecraft mc = Minecraft.getMinecraft();
		ITextComponent message = new TextComponentTranslation(text);
		message.setStyle(new Style().setBold(true).setColor(TextFormatting.YELLOW));
		mc.ingameGUI.setOverlayMessage(message, true);
	}
	
	@SubscribeEvent
	public void renderOverlay(RenderTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.player;
		if (player != null) {
			World world = player.world;
			RayTraceResult ray = player.rayTrace(4.5f, 1);
			if (ray.typeOfHit == Type.BLOCK) {
				BlockPos pos = ray.getBlockPos();
				IBlockState state = world.getBlockState(pos);
				if (state.getBlock() == ModContent.BARBED_WIRE && world.getTileEntity(pos) instanceof TileBarbedWire) {
					TileBarbedWire tile = (TileBarbedWire) world.getTileEntity(pos);
					int max = state.getValue(BlockBarbedWire.MATERIAL).getDurability();
					int cur = tile.getDurability();
					RenderManager manager = mc.getRenderManager();
					boolean thirdPerson = manager.options.thirdPersonView == 2;
			        float x = (float) (pos.getX()-player.posX + 0.5f);
			        float y = (float) (pos.getY()-player.posY + 1);
			        float z = (float) (pos.getZ()-player.posZ + 0.5f);
			        EntityRenderer.drawNameplate(manager.getFontRenderer(), cur + "/" + max, x, y, z, 0, manager.playerViewY, manager.playerViewX, thirdPerson, false);
				}
			}
		}
	}
	
}
