package net.smileycorp.ldoh.client.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.smileycorp.ldoh.client.entity.model.ModelTF2CharacterLDOH;
import rafradek.TF2weapons.client.model.ModelHeavy;
import rafradek.TF2weapons.client.renderer.LayerArmorTint;
import rafradek.TF2weapons.client.renderer.LayerWearables;
import rafradek.TF2weapons.client.renderer.entity.RenderTF2Character;

public class RenderTF2CharacterLDOH extends RenderTF2Character {

	public RenderTF2CharacterLDOH(RenderManager manager){
		super(manager);
		mainModel = new ModelTF2CharacterLDOH();
		modelMain = (ModelBiped) mainModel;
		modelHeavy = new ModelHeavy();
		layerRenderers.clear();
		addLayer(new LayerHeldItemTF2(this));
		addLayer(new LayerArmorTint(this));
		addLayer(new LayerWearables(this));
		layerRenderers.removeIf(layer -> (LayerRenderer<?>)layer instanceof LayerCustomHead);
	}

	/*@Override
	protected ResourceLocation getEntityTexture(EntityTF2Character entity) {
		ResourceLocation loc = super.getEntityTexture(entity);
		if (entity.hasCapability(LDOHCapabilities.EXHAUSTION, null) &! entity.isRobot()) {
			IExhaustion cap = entity.getCapability(LDOHCapabilities.EXHAUSTION, null);
			if(cap.isSleeping(entity)) loc = ModDefinitions.getResource(loc.getResourcePath().replace("tf2", "tf_sleep"));
			else if (cap.isTired(entity)) loc = ModDefinitions.getResource(loc.getResourcePath().replace("tf2", "tf_tired"));
		}
		return loc;
	}*/

}
