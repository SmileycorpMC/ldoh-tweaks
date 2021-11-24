package net.smileycorp.ldoh.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.smileycorp.ldoh.client.entity.RenderTurret;
import net.smileycorp.ldoh.client.entity.model.ModelTurret;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.inventory.ContainerTurret;

public class GuiTurret extends GuiContainer {

	public static ResourceLocation TEXTURE = ModDefinitions.getResource("textures/gui/turret.png");
	public static final String TRANSLATION_KEY = "entity.hundreddayz.Turret.name";

	protected EntityTurret turret;
	protected ModelBase turretModel = new ModelTurret();
	protected String owner = null;

	public GuiTurret(EntityTurret turret, EntityPlayer player) {
		super(new ContainerTurret(turret, player));
		this.turret = turret;
		width = 176;
		height = 166;
		owner = turret.getOwnerUsername();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        //draw background texture
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        //draw health
        int health = (int) Math.ceil(turret.getHealth());
        for (int i = 0; i<=Math.ceil(health/2); i++) {
			int hx = x + 65 + ((i%10)*10);
			int hy =  y + 31 + ((int)Math.floor(i/10)*10);
			int u = (i+1)*2>health ? 9 : 0;
			if ((i*2)<health) drawTexturedModalRect(hx, hy, u, 166, 9, 9);
		}

        //draw gui name
        String name = I18n.translateToLocal(TRANSLATION_KEY);
        fontRenderer.drawString(name, x + 88 - fontRenderer.getStringWidth(name) / 2, y + 6, 4210752);

        //draw owner name
        Team team = turret.getTeam();
        ITextComponent username = new TextComponentString(owner!=null ? owner : "");
        if (team!=null) username.setStyle(new Style().setColor(team.getColor()));
        String text = I18n.translateToLocal("gui.text.Owner") + username.getFormattedText();
        fontRenderer.drawString(text, x + 115 - fontRenderer.getStringWidth(text) / 2, y + 20, 4210752);

        //draw turret entity
        mc.getTextureManager().bindTexture(RenderTurret.TEXTURE);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.translate(x+36, y+16, 40);
        GlStateManager.rotate(-30, 1, 0, 0);
        GlStateManager.rotate(135, 0, 1, 0);
        turretModel.render(turret, 0, 0, 0, turret.rotationYaw, turret.rotationPitch, 2);
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
