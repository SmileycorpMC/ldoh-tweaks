package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.entity.zombie.EntityTF2Zombie;

public class RenderTF2Zombie extends RenderBiped<EntityTF2Zombie> {

    public RenderTF2Zombie(RenderManager rendermanager) {
        super(rendermanager, new ModelZombie(), 0.5F);
        addLayer(new LayerZombieArmour(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTF2Zombie entity) {
        //get the team and class from the entity to get the right texture
        return Constants.loc("textures/entity/tfzombie/" + entity.getTFTeam().getName() + "_" + entity.getTFClass() + "_zombie.png");
    }

}
