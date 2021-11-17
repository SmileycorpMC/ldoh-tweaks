package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.client.entity.model.ModelTF2CharacterLDOH;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.IExhaustion;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import rafradek.TF2weapons.client.model.ModelHeavy;
import rafradek.TF2weapons.client.renderer.entity.RenderTF2Character;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;

public class RenderTF2CharacterLDOH extends RenderTF2Character {

	public RenderTF2CharacterLDOH(RenderManager manager){
		super(manager);
		mainModel = new ModelTF2CharacterLDOH();
		modelMain = (ModelBiped) mainModel;
		modelHeavy = new ModelHeavy();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTF2Character entity) {
		ResourceLocation loc = super.getEntityTexture(entity);
		if (entity.hasCapability(LDOHCapabilities.EXHAUSTION, null) &! entity.isRobot()) {
			IExhaustion cap = entity.getCapability(LDOHCapabilities.EXHAUSTION, null);
			if(cap.isSleeping(entity)) loc = ModDefinitions.getResource(loc.getResourcePath().replace("tf2", "tf_sleep"));
			else if (cap.isTired(entity)) loc = ModDefinitions.getResource(loc.getResourcePath().replace("tf2", "tf_tired"));
		}
		return loc;
	}

}
