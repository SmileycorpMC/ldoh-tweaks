package net.smileycorp.ldoh.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.smileycorp.hordes.infection.HordesInfection;
import net.smileycorp.hordes.infection.client.InfectionClientHandler;
import net.smileycorp.hordes.infection.network.CureEntityMessage;
import net.smileycorp.ldoh.client.gui.GUITornNote;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.smileycorp.ldoh.common.network.*;

//client hooks for our network packets
public class ClientHandler {

    //show action bar message with the specified text
    public static void displayActionBar(String text) {
        Minecraft mc = Minecraft.getMinecraft();
        ITextComponent message = new TextComponentTranslation(text);
        message.setStyle(new Style().setBold(true).setColor(TextFormatting.YELLOW));
        mc.ingameGUI.setOverlayMessage(message, true);
    }

    //sync entity food item to client
    public static void syncFood(SyncFoodMessage message) {
        Entity entity = message.getEntity(Minecraft.getMinecraft().world);
        if (entity == null) return;
        if (!entity.hasCapability(LDOHCapabilities.HUNGER, null)) return;
        entity.getCapability(LDOHCapabilities.HUNGER, null).setFoodStack(message.getStack());
    }

    //sync entity hunger value to client
    public static void syncHunger(SyncHungerMessage message) {
        Entity entity = message.getEntity(Minecraft.getMinecraft().world);
        if (entity == null) return;
        if (!entity.hasCapability(LDOHCapabilities.HUNGER, null)) return;
        entity.getCapability(LDOHCapabilities.HUNGER, null).setFoodLevel(message.getHunger());
    }

    //sync entity eating to client
    public static void startEating(StartEatingMessage message) {
        Entity entity = message.getEntity(Minecraft.getMinecraft().world);
        if (entity != null) if (entity.hasCapability(LDOHCapabilities.HUNGER, null))
            entity.getCapability(LDOHCapabilities.HUNGER, null).startEating((EntityLiving) entity);
    }

    //sync medic syringe count to client
    public static void syncSyringes(SyncSyringesMessage message) {
        Entity entity = message.getEntity(Minecraft.getMinecraft().world);
        if (entity == null) return;
        if (!entity.hasCapability(LDOHCapabilities.CURING, null)) return;
        entity.getCapability(LDOHCapabilities.CURING, null).setSyringeCount(message.getCount());
    }

    //sync medic curing an entity to client
    public static void syncMedicCure(SyncMedicCureMessage message) {
        Entity entity = message.getEntity(Minecraft.getMinecraft().world);
        if (!(entity instanceof EntityLiving)) return;
        ((EntityLiving) entity).removePotionEffect(HordesInfection.INFECTED);
        InfectionClientHandler.INSTANCE.processCureEntity(new CureEntityMessage(entity));
    }

    public static void openNoteGUI(long seed) {
        Minecraft.getMinecraft().displayGuiScreen(new GUITornNote(seed));
    }

    public static void rotateMatrix(EnumFacing facing) {
        switch (facing) {
            case WEST:
                GlStateManager.rotate(90, 0, 1, 0);
                return;
            case EAST:
                GlStateManager.rotate(-90, 0, 1, 0);
                return;
            case NORTH:
                return;
            case SOUTH:
                GlStateManager.rotate(180, 0, 1, 0);
                return;
            case UP:
                GlStateManager.rotate(-90, 0, 1, 1);
                return;
            case DOWN:
                GlStateManager.rotate(90, 0, 1, 1);
        }
    }

}
