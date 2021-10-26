package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.smileycorp.ldoh.common.entity.EntitySwatZombie;

public class RenderSwatZombie extends RenderSpecialZombie<EntitySwatZombie> {

    public RenderSwatZombie(RenderManager manager) {
        super(manager, "swat_zombie");
        /*ItemModelPart<EntitySwatZombie> layerBackitem = new ItemModelPart<EntitySwatZombie>(this, (e) -> e.getBackItem(), (e, r) -> {
        	float x =((ModelBiped)r.getMainModel()).bipedBody.rotateAngleX;
        	float y =((ModelBiped)r.getMainModel()).bipedBody.rotateAngleY;
        	float z =((ModelBiped)r.getMainModel()).bipedBody.rotateAngleZ;
        	GlStateManager.rotate(x * (180F / (float)Math.PI) + 90, 1f, 0, 0);
        	GlStateManager.rotate(y * (180F / (float)Math.PI), 0, 1f, 0);
        	GlStateManager.rotate(z * (180F / (float)Math.PI) + 90, 0, 0, 1f);
        });
        addLayer(layerBackitem);
        ItemModelPart<EntitySwatZombie> layerHolsteritem = new ItemModelPart<EntitySwatZombie>(this, (e) -> e.getHolsterItem(), (e, r) -> {
        	float x =((ModelBiped)r.getMainModel()).bipedBody.rotateAngleX;
        	float y =((ModelBiped)r.getMainModel()).bipedBody.rotateAngleY;
        	float z =((ModelBiped)r.getMainModel()).bipedBody.rotateAngleZ;
        	GlStateManager.rotate(x * (180F / (float)Math.PI) - 90, 1f, 0, 0);
        	GlStateManager.rotate(y * (180F / (float)Math.PI) + 180, 0, 1f, 0);
        	GlStateManager.rotate(z * (180F / (float)Math.PI), 0, 0, 1f);
        	GlStateManager.translate(x, 0.4f, -0.5f);
        });
        addLayer(layerHolsteritem);*/
    }
}
