package net.smileycorp.ldoh.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.common.Constants;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GUITornNote extends GuiScreen {

    private static final ResourceLocation TEXTURE = Constants.loc("textures/gui/torn_note.png");

    private final List<Integer> characters = Lists.newArrayList();

    public GUITornNote(long seed) {
        Random rand = new Random(seed);
        characters.add(10);
        characters.addAll(splitDigits(1000 + rand.nextInt(2000)));
        characters.add(11);
        characters.addAll(splitDigits(rand.nextInt(360)));
        characters.add(12);
        mc = Minecraft.getMinecraft();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int x = (width - 145 * 2) / 4;
        int y = (height - 130 * 2) / 4;
        GlStateManager.pushMatrix();
        GlStateManager.scale(2, 2, 2);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(x, y, 0, 0, 145, 130);
        x += (145 - (characters.size()+2) * 11);
        for (int i = 0; i < characters.size(); i ++) drawCharacter(x + (i * 11) , y + 50, characters.get(i));
        GlStateManager.scale(1, 1, 1);
        GlStateManager.popMatrix();
    }

    private List<Integer> splitDigits(int number) {
        return String.valueOf(number).chars().map(Character::getNumericValue).boxed().collect(Collectors.toList());
    }

    private void drawCharacter(int x, int y, int index) {
        drawTexturedModalRect(x, y, index * 11, 130, 11, 20);
    }

}
