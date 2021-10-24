package net.smileycorp.ldoh.client.entity;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class LayerItem<T extends EntityLivingBase> implements LayerRenderer<T>{

	protected final RenderLivingBase<?> renderer;
	
	protected final BiConsumer<T, RenderLivingBase<?>> transforms;
	protected final Function<T, ItemStack> stack;

	public LayerItem(RenderLivingBase<?> renderer, Function<T, ItemStack> stack, BiConsumer<T, RenderLivingBase<?>> transforms) {
		this.renderer = renderer;
		this.transforms = transforms;
		this.stack = stack;
	}
	
	 @Override
	public void doRenderLayer(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		ItemStack stack = this.stack.apply(entity);	
        GlStateManager.pushMatrix();      
        if (this.renderer.getMainModel().isChild) {
            GlStateManager.translate(0.0F, 0.75F, 0.0F);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
        }
        transforms.accept(entity, renderer);
        Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, TransformType.THIRD_PERSON_RIGHT_HAND, false);
        GlStateManager.popMatrix();
	 }

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
