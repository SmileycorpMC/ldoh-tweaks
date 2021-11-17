package net.smileycorp.ldoh.client.tesr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.smileycorp.hordes.common.Hordes;
import net.smileycorp.ldoh.client.entity.RenderTurret;
import net.smileycorp.ldoh.client.entity.model.ModelTurret;

public class TESRTurretItem extends TileEntityItemStackRenderer {

	protected ModelTurret turret = new ModelTurret();

	@Override
	public void renderByItem(ItemStack stack) {
		Hordes.logInfo("rendering " + stack);
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(RenderTurret.TEXTURE);
		turret.render(null, 0, 0, 0, 0, 0, 1);
	}

}
