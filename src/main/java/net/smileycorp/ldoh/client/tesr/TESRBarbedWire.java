package net.smileycorp.ldoh.client.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;

public class TESRBarbedWire extends TileEntitySpecialRenderer<TileBarbedWire> {

	@Override
	public void render(TileBarbedWire te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		//render only if the player is looking at the block
		if (rendererDispatcher.cameraHitResult != null && te.getPos().equals(rendererDispatcher.cameraHitResult.getBlockPos())) {
			if (!Minecraft.getMinecraft().gameSettings.hideGUI) {
				//render name tag with the health left
				setLightmapDisabled(true);
				int max = te.getMaterial().getDurability();
				int cur = te.getDurability();
				drawNameplate(te, cur + "/" + max, x, y-0.25, z, 12);
				setLightmapDisabled(false);
			}
		}
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}

}
