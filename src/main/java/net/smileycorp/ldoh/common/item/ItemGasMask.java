package net.smileycorp.ldoh.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.smileycorp.ldoh.common.ModDefinitions;

import org.lwjgl.opengl.GL11;

public class ItemGasMask extends ItemHat {
	
	public ItemGasMask() {
		super("Gas_Mask");
		setMaxDamage(210);
	}
	
	@Override
    public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks){
		Minecraft mc = Minecraft.getMinecraft();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        mc.getTextureManager().bindTexture(ModDefinitions.getResource("textures/misc/gas_mask.png"));
		GlStateManager.pushMatrix();
		GlStateManager.scale(2, 2, 2);
		Gui.drawModalRectWithCustomSizedTexture(-8, -30, 0, 0, mc.displayWidth, mc.displayHeight, 256, 192);
		GlStateManager.popMatrix();
		GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}
}
