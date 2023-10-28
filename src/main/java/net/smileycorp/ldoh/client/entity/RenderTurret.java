package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.client.entity.model.ModelTurret;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.entity.EntityTurret;

public class RenderTurret extends RenderLiving<EntityTurret> {

    public static final ResourceLocation TEXTURE = ModDefinitions.getResource("textures/entity/turret.png");
    public static final ResourceLocation ENEMY_TEXTURE = ModDefinitions.getResource("textures/entity/enemy_turret.png");

    public RenderTurret(RenderManager rendermanager) {
        super(rendermanager, new ModelTurret(), 0.5F);
    }

    //render turret with enemy texture if required
    @Override
    protected ResourceLocation getEntityTexture(EntityTurret entity) {
        return entity.isEnemy() ? ENEMY_TEXTURE : TEXTURE;
    }

}
