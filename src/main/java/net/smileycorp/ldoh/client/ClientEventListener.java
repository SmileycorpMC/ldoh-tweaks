package net.smileycorp.ldoh.client;

import com.chaosthedude.realistictorches.blocks.RealisticTorchesBlocks;
import com.mrcrayfish.furniture.init.FurnitureItems;
import com.mrcrayfish.guns.item.ItemGun;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.WorldType;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.capabilities.ICuring;
import net.smileycorp.ldoh.common.capabilities.IHunger;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.smileycorp.ldoh.common.item.LDOHItems;
import rafradek.TF2weapons.client.gui.inventory.GuiMercenary;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;
import rafradek.TF2weapons.item.ItemAmmoPackage;
import rafradek.TF2weapons.item.ItemFromData;
import rafradek.TF2weapons.item.ItemHuntsman;

import java.awt.*;

@SuppressWarnings("deprecation")
@EventBusSubscriber(modid = Constants.MODID, value = Side.CLIENT)
public class ClientEventListener {

    public static Color GAS_COLOUR = new Color(0.917647059f, 1f, 0.0470588235f, 0.1f);
    public static ResourceLocation GAS_TEXTURE = Constants.loc("textures/misc/gas.png");
    public static ResourceLocation TF_HUNGER_TEXTURE = Constants.loc("textures/gui/tf_hunger.png");
    public static ResourceLocation MEDIC_SYRINGES_TEXTURE = Constants.loc("textures/gui/medic_syringes.png");

    //Render Gas Overlay when below gas level
    @SubscribeEvent
    public void postRenderOverlay(RenderGameOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null || event.getType() != ElementType.ALL) return;
        if (player.getPosition().getY() > 29.2 || player.world.getWorldType() == WorldType.FLAT) return;
        int r = GAS_COLOUR.getRed();
        int g = GAS_COLOUR.getGreen();
        int b = GAS_COLOUR.getBlue();
        int a = GAS_COLOUR.getAlpha();
        int height = mc.displayHeight;
        int width = mc.displayWidth;
        float t = (player.ticksExisted + event.getPartialTicks()) * 0.01f;
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        mc.getTextureManager().bindTexture(GAS_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        bufferbuilder.pos(0, height, 0).tex(t, 1 + t).color(r, g, b, a).normal(0, 1, 0).endVertex();
        bufferbuilder.pos(width, height, 0).tex(1 + t, 1 + t).color(r, g, b, a).normal(0, 1, 0).endVertex();
        bufferbuilder.pos(width, 0, 0).tex(1 + t, t).color(r, g, b, a).normal(0, 1, 0).endVertex();
        bufferbuilder.pos(0, 0, 0).tex(t, t).color(r, g, b, a).normal(0, 1, 0).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    //Render gas layer in world when above gas level
    @SubscribeEvent
    public static void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();
        if (entity == null) return;
        if (entity.posY < 29.2 && entity.world.getWorldType() == WorldType.FLAT) return;
        RenderManager rm = mc.getRenderManager();
        //scale renderer base on render distance
        int size = rm.options == null ? 0 : (rm.options.renderDistanceChunks + 1) * 16;
        int r = GAS_COLOUR.getRed();
        int g = GAS_COLOUR.getGreen();
        int b = GAS_COLOUR.getBlue();
        int a = GAS_COLOUR.getAlpha();
        //coords for the centre of the current chunk
        int cx = ((int) Math.floor(entity.posX / 16)) * 16 + 8;
        int cz = ((int) Math.floor(entity.posZ / 16)) * 16 + 8;
        final double x = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * event.getPartialTicks());
        final double y = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * event.getPartialTicks());
        final double z = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * event.getPartialTicks());
        float t = (entity.ticksExisted + event.getPartialTicks()) * 0.01f;
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();
        Vec3d start = new Vec3d(cx - x + 0.5 - size, 30.9 - y, cz - z + 0.5 - size);
        Vec3d end = new Vec3d(cx - x + 0.5 + size, 30.9 - y, cz - z + 0.5 + size);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        double dy = (end.y - start.y) * 0.5d;
        Minecraft.getMinecraft().getTextureManager().bindTexture(GAS_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        buffer.pos(start.x, start.y, start.z).tex(t, t).color(r, g, b, a).normal(0, 1, 0).endVertex();
        buffer.pos(start.x, start.y + dy , end.z).tex(t, t + 1).color(r, g, b, a).normal(0, 1, 0).endVertex();
        buffer.pos(end.x, end.y, end.z).tex(t + 1, t + 1).color(r, g, b, a).normal(0, 1, 0).endVertex();
        buffer.pos(end.x, start.y + dy, start.z).tex(t + 1, t).color(r, g, b, a).normal(0, 1, 0).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    //all the hunger and syringe rendering should probably be moved to it's own class
    //and maybe have players be able to interact with the slot
    //could potentially be part of the capabilities?
    @SubscribeEvent
    public static void drawGUI(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!(event.getGui() instanceof GuiMercenary)) return;
        GuiMercenary gui = (GuiMercenary) event.getGui();
        EntityTF2Character entity = gui.mercenary;
        if (entity.hasCapability(LDOHCapabilities.HUNGER, null) & !entity.isRobot()) {
            //draw merc hunger
            Minecraft mc = Minecraft.getMinecraft();
            IHunger hunger = entity.getCapability(LDOHCapabilities.HUNGER, null);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(TF_HUNGER_TEXTURE);
            //draw background
            gui.drawTexturedModalRect(gui.getGuiLeft() - 54, gui.getGuiTop(), 0, 0, 54, 70);
            //get the correct texture coordinate for the current hunger icon
            int v = entity.isPotionActive(MobEffects.HUNGER) ? 79 : 70;
            int food = hunger.getFoodLevel();
            //draw hunger icons
            for (int i = 0; i <= Math.ceil(food / 2); i++) {
                int x = gui.getGuiLeft() - 50 + ((4 - i % 5) * 9);
                int y = gui.getGuiTop() + 16 + ((int) Math.floor(i / 5) * 9);
                int u = (i + 1) * 2 > food ? 9 : 0;
                if ((i * 2) < food) gui.drawTexturedModalRect(x, y, u, v, 9, 9);
            }
            FontRenderer font = gui.fontRenderer;
            if (!hunger.getFoodSlot().isEmpty()) {
                //draw hunger slot item
                GlStateManager.enableLighting();
                GlStateManager.enableRescaleNormal();
                RenderHelper.enableGUIStandardItemLighting();
                ItemStack stack = hunger.getFoodSlot();
                gui.itemRender.renderItemAndEffectIntoGUI(stack, gui.getGuiLeft() - 36, gui.getGuiTop() + 42);
                gui.itemRender.renderItemOverlayIntoGUI(font, stack, gui.getGuiLeft() - 36, gui.getGuiTop() + 42, null);
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableLighting();
            }
            //draw hunger text
            String text = I18n.translateToLocal("gui.text.Hunger");
            font.drawString(text, gui.getGuiLeft() - 26 - (font.getStringWidth(text) / 2), gui.getGuiTop() + 5, 4210752);
        }
        if (entity.hasCapability(LDOHCapabilities.CURING, null)) {
            //draw medic syringes inventory
            Minecraft mc = Minecraft.getMinecraft();
            ICuring curing = entity.getCapability(LDOHCapabilities.CURING, null);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(MEDIC_SYRINGES_TEXTURE);
            //draw background
            gui.drawTexturedModalRect(gui.getGuiLeft() - 54, gui.getGuiTop() + 112, 0, 0, 54, 54);
            FontRenderer font = gui.fontRenderer;
            int count = curing.getSyringeCount();
            if (count > 0) {
                //render syringe items
                GlStateManager.enableLighting();
                GlStateManager.enableRescaleNormal();
                RenderHelper.enableGUIStandardItemLighting();
                ItemStack stack = new ItemStack(LDOHItems.SYRINGE, count, 2);
                gui.itemRender.renderItemAndEffectIntoGUI(stack, gui.getGuiLeft() - 36, gui.getGuiTop() + 136);
                gui.itemRender.renderItemOverlayIntoGUI(font, stack, gui.getGuiLeft() - 36, gui.getGuiTop() + 136, null);
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableLighting();
            }
            //draw syringe title
            String text = I18n.translateToLocal("gui.text.Curing");
            font.drawString(text, gui.getGuiLeft() - 26 - (font.getStringWidth(text) / 2), gui.getGuiTop() + 117, 4210752);
        }
    }

    @SubscribeEvent
    public static void drawGUIOverlay(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!(event.getGui() instanceof GuiMercenary)) return;
        GuiMercenary gui = (GuiMercenary) event.getGui();
        EntityTF2Character entity = gui.mercenary;
        int mouseX = event.getMouseX();
        int mouseY = event.getMouseY();
        //show tooltips for hunger items
        if (entity.hasCapability(LDOHCapabilities.HUNGER, null) & !entity.isRobot()) {
            IHunger hunger = entity.getCapability(LDOHCapabilities.HUNGER, null);
            if (!hunger.getFoodSlot().isEmpty()) {
                int slotX = gui.getGuiLeft() - 36;
                int slotY = gui.getGuiTop() + 42;
                //is mouse over the hunger slot?
                if (mouseX >= slotX && mouseX <= slotX + 18 && mouseY >= slotY && mouseY <= slotY + 18)
                    gui.renderToolTip(hunger.getFoodSlot(), mouseX, mouseY);
            }
        }
        //show tooltips for medic curing slot
        if (entity.hasCapability(LDOHCapabilities.CURING, null)) {
            int count = entity.getCapability(LDOHCapabilities.CURING, null).getSyringeCount();
            if (count > 0) {
                int slotX = gui.getGuiLeft() - 36;
                int slotY = gui.getGuiTop() + 136;
                //is mouse over the hunger slot?
                if (mouseX >= slotX && mouseX <= slotX + 18 && mouseY >= slotY && mouseY <= slotY + 18)
                    gui.renderToolTip(new ItemStack(LDOHItems.SYRINGE, count, 2), mouseX, mouseY);
            }
        }
    }

    //add tooltips to other mods items
    @SubscribeEvent
    public static void addInformation(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        if (item == FurnitureItems.CROWBAR)
            event.getToolTip().add(1, new TextComponentTranslation("tooltip.ldoh.Crowbar").getFormattedText());
        else if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            if (block == RealisticTorchesBlocks.torchUnlit)
                event.getToolTip().add(1, new TextComponentTranslation("tooltip.ldoh.UnlitTorch").getFormattedText());
        } else if (item instanceof ItemExpBottle)
            event.getToolTip().add(1, new TextComponentTranslation("tooltip.ldoh.ExpBottle").getFormattedText());
        else if (item instanceof ItemFromData) {
            if (item instanceof ItemHuntsman)
                event.getToolTip().add(1, new TextComponentTranslation("tooltip.ldoh.Ammo",
                        new ItemStack(Items.ARROW).getDisplayName()).getFormattedText());;
            int type = ((ItemFromData) item).getAmmoType(stack);
            if (type <= 0 || type >= 16) return;
            ItemStack ammo = ItemAmmoPackage.getAmmoForType(type, 1);
            if (ammo.getMetadata() >= 16) return;
            event.getToolTip().add(1, new TextComponentTranslation("tooltip.ldoh.Ammo",
                    ammo.getDisplayName()).getFormattedText());;
        } else if (item instanceof ItemGun) {
            ResourceLocation name = ((ItemGun) stack.getItem()).getModifiedGun(stack).projectile.item;
            if (name == null) return;
            Item ammo = ForgeRegistries.ITEMS.getValue(name);
            if (ammo == null) return;
            event.getToolTip().add(1, new TextComponentTranslation("tooltip.ldoh.Ammo",
                    new ItemStack(ammo).getDisplayName()).getFormattedText());;
        }
    }

}
