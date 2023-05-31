package net.smileycorp.ldoh.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.util.text.translation.I18n;
import net.smileycorp.ldoh.client.entity.RenderTurret;
import net.smileycorp.ldoh.client.entity.model.ModelTurret;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.inventory.ContainerTurret;
import net.smileycorp.ldoh.common.util.ModUtils;

public class GuiTurret extends GuiContainer {

	public static ResourceLocation TEXTURE = ModDefinitions.getResource("textures/gui/turret.png");
	public static final String TRANSLATION_KEY = "entity.hundreddayz.Turret.name";

	protected EntityTurret turret;
	protected ModelBase turretModel = new ModelTurret();
	protected String owner = null;

	public GuiTurret(EntityTurret turret, EntityPlayer player) { //TODO: finish turret ui/functionality upgrade
		super(new ContainerTurret(turret, player));
		this.turret = turret;
		width = 176;
		height = 202;
		owner = turret.getOwnerUsername();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2 - 5;

		//draw background texture
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(x, y, 0, 0, xSize, 202);

		//draw health
		int health = (int) Math.ceil(turret.getHealth());
		for (int i = 0; i<=Math.ceil(health/2); i++) {
			int hx = x + 65 + ((i%10)*10);
			int hy = y + 67 + ((int)Math.floor(i/10)*10);
			int u = (i+1)*2>health ? 9 : 0;
			if ((i*2)<health) drawTexturedModalRect(hx, hy, u, 203, 9, 9);
		}

		//draw gui name
		String text = I18n.translateToLocal(TRANSLATION_KEY);
		fontRenderer.drawString(text, x + 88 - fontRenderer.getStringWidth(text) / 2, y + 6, 4210752);

		//draw upgrades text
		text = new TextComponentTranslation("gui.turret.text.Upgrades").setStyle(new Style().setColor(TextFormatting.DARK_GRAY)).getFormattedText();
		fontRenderer.drawString(text, x + 65, y + 26, 4210752);

		//draw owner name
		if (turret.isEnemy()) {
			text = new TextComponentTranslation("gui.turret.text.Hostile").setStyle(new Style().setColor(TextFormatting.DARK_RED)).getFormattedText();
		}
		else if (owner != null) {
			Team team = turret.getTeam();
			ITextComponent username = new TextComponentString(owner);
			if (team != null) username.setStyle(new Style().setColor(team.getColor()));
			text = I18n.translateToLocal("gui.turret.text.Owner") + username.getFormattedText();
		} else {
			text = I18n.translateToLocal("gui.turret.text.NoOwner");
		}
		fontRenderer.drawString(text, x + 65, y + 38, 4210752);

		//draw target name
		if (!turret.isActive()) text = I18n.translateToLocal("gui.turret.text.Disabled");
		else if (!turret.hasTarget()) text = I18n.translateToLocal("gui.turret.text.NoTarget");
		else {
			EntityLivingBase target = turret.getTarget();
			text = target.getDisplayName().getFormattedText();
			fontRenderer.drawString(ModUtils.getPosString(target.getPosition()), x + 65, y + 58, 4210752);
		}
		fontRenderer.drawString(text, x + 65, y + 48, 4210752);

		//draw turret stats
		fontRenderer.drawString(I18n.translateToLocal("gui.turret.text.Range") + turret.getRange(), x + 11, y + 67, 4210752);
		fontRenderer.drawString(I18n.translateToLocal("gui.turret.text.FireRate") + turret.getFireRate(), x + 11, y + 77, 4210752);

		//draw turret entity
		mc.getTextureManager().bindTexture(RenderTurret.TEXTURE);
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.translate(x+36, y+10, 40);
		GlStateManager.rotate(-30, 1, 0, 0);
		GlStateManager.rotate(135, 0, 1, 0);
		turretModel.render(turret, 0, 0, 0, turret.rotationPitch, 0, 2);
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableCull();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

}
