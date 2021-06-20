package net.smileycorp.hundreddayz.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.hundreddayz.common.ModContent;
import net.smileycorp.hundreddayz.common.ModDefinitions;
import net.smileycorp.hundreddayz.common.block.BlockBarbedWire;
import net.smileycorp.hundreddayz.common.block.TileEntityBarbedWire;

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
	
	/*@SubscribeEvent
	public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
		starttime=0;
	}*/

	/*@SubscribeEvent
	public void drawTitle(RenderFogEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.player;
		EntityRenderer render = event.getRenderer();
		if (player.posY < 30) {
			if (!render.isShaderActive()) {
				render.loadShader(ModDefinitions.getResource("shaders/post/gas.json"));
			}
		} else if (render.isShaderActive()) {
			render.stopUseShader();
		}
	}*/
	
	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		World world = player.world;
		if (world.isRemote &!(player == null)) {
			RayTraceResult ray = player.rayTrace(5, 20);
			if (ray != null) {
				if (ray.typeOfHit == RayTraceResult.Type.BLOCK) {
					BlockPos pos = ray.getBlockPos();
					IBlockState state = world.getBlockState(pos);
					if (world.getBlockState(pos).getBlock() == ModContent.BARBED_WIRE && world.getTileEntity(pos) instanceof TileEntityBarbedWire) {
						TileEntityBarbedWire tile = (TileEntityBarbedWire) world.getTileEntity(pos);
						int max = state.getValue(BlockBarbedWire.MATERIAL).getDurability();
						int cur = tile.getDurability();
						Minecraft mc = Minecraft.getMinecraft();
						ITextComponent message = new TextComponentString(cur + "/" + max);
						mc.ingameGUI.setOverlayMessage(message, false);
					}
				}
			}
		}
	}
	
}
