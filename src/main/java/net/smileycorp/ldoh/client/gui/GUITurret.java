package net.smileycorp.ldoh.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.client.entity.model.ModelTurret;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.inventory.ContainerTurret;

public class GUITurret extends GuiContainer {

	public static ResourceLocation TEXTURE = ModDefinitions.getResource("textures/gui/turret.png");

	protected EntityTurret turret;

	protected ModelBase turretModel = new ModelTurret();

	public GUITurret(EntityTurret turret, EntityPlayer player) {
		super(new ContainerTurret(turret, player));
		this.turret = turret;
		width = 176;
		height = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float pt, int mouseX, int mouseY) {
		int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
	}

}
