package net.smileycorp.ldoh.integration.mobends.reaver;

import goblinbob.mobends.core.client.model.BoxSide;
import goblinbob.mobends.core.client.model.ModelPart;
import goblinbob.mobends.core.client.model.ModelPartExtended;
import goblinbob.mobends.core.client.model.ModelPartPostOffset;
import goblinbob.mobends.core.data.IEntityDataFactory;
import goblinbob.mobends.standard.mutators.ZombieMutatorBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.smileycorp.ldoh.client.entity.model.ModelThinZombie;
import net.smileycorp.ldoh.common.entity.zombie.EntityReaver;

public class ReaverMutator extends ZombieMutatorBase<ReaverData, EntityReaver, ModelThinZombie> {

    public ReaverMutator(IEntityDataFactory<EntityReaver> dataFactory) {
        super(dataFactory);
    }

    @Override
    public boolean createParts(ModelThinZombie original, float scaleFactor) {
        // Body
        original.bipedBody = body = (ModelPartPostOffset) new ModelPartPostOffset(original, 16, 16)
                .setPostOffset(0.0F, -12f, 0.0F)
                .setPosition(0.0F, 12.0F, 0.0F);
        body.addBox(-3.5F, -12f, -1.5F, 7, 12, 3, scaleFactor);
        
        ModelRenderer intestines = new ModelRenderer(original, 45, 36);
        intestines.setRotationPoint(0.0F, 2.0F, 0.0F);
        intestines.addBox(-3.0F, -3.7F, -4.5F, 6, 5, 0, 0.0F);
        intestines.rotateAngleX = -0.7f;
        intestines.rotateAngleZ = 0.2f;
        body.addChild(intestines);

        // Head
        original.bipedHead = head = new ModelPart(original, 0, 0)
                .setParent(body)
                .setPosition(0.0F, -0.0F, 0.0F);
        head.addBox(-3.5F, -8.0F, -3.5F, 7, 8, 7, scaleFactor);
        original.bipedHeadwear = headwear = new ModelPart(original, 32, 0).setParent(head);
        headwear.addBox(-3.5F, -8.0F, -3.5F, 7, 8, 7, scaleFactor + 0.5F);

        //head.rotateAngleZ=-0.5f;

        // Arms
        float armY = -10F;

        original.bipedLeftArm = leftArm = (ModelPartExtended) new ModelPartExtended(original, 40, 16)
                .setParent(body)
                .setPosition(3F, armY, 0.0F)
                .setMirror(true);
        leftArm.developBox(-1.5f, -2.0F, -2.0F, 3, 6, 3, scaleFactor)
                .inflate(0.01F, 0, 0.01F)
                .hideFace(BoxSide.BOTTOM)
                .create();

        original.bipedRightArm = rightArm = (ModelPartExtended) new ModelPartExtended(original, 40, 16)
                .setParent(body)
                .setPosition(-3F, armY, 0.0F);
        rightArm.developBox(-1.5f, -2.0F, -2.0F, 3, 6, 3, scaleFactor)
                .inflate(0.01F, 0, 0.1F)
                .hideFace(BoxSide.BOTTOM)
                .create();

        leftForeArm = (ModelPartPostOffset) new ModelPartPostOffset(original, 40, 16 + 6)
                .setPostOffset(0, -4F, -2F)
                .setParent(leftArm)
                .setPosition(0.0F, 4.0F, 2.0F)
                .setMirror(true);
        leftForeArm.developBox(-1.5f, 0.0F, -4.0F, 3, 6, 3, scaleFactor)
                .hideFace(BoxSide.TOP)
                .offsetTextureQuad(BoxSide.BOTTOM, 0, -6F)
                .create();

        leftArm.setExtension(leftForeArm);
        rightForeArm = (ModelPartPostOffset) new ModelPartPostOffset(original, 40, 16 + 6)
                .setPostOffset(0, -4F, -2F)
                .setParent(rightArm)
                .setPosition(0.0F, 4.0F, 2.0F);
        rightForeArm.developBox(-1.5f, 0.0F, -4.0F, 3, 6, 3, scaleFactor)
                .hideFace(BoxSide.TOP)
                .offsetTextureQuad(BoxSide.BOTTOM, 0, -6F)
                .create();
        rightArm.setExtension(rightForeArm);

        // Legs
        original.bipedRightLeg = rightLeg = (ModelPartExtended) new ModelPartExtended(original, 0, 16)
                .setPosition(0.0F, 12F, 0.5F);
        rightLeg.addBox(-3.9F, 0.0F, -2.0F, 3, 6, 3, scaleFactor);
        original.bipedLeftLeg = leftLeg = (ModelPartExtended) new ModelPartExtended(original, 0, 16)
                .setPosition(0.0F, 12.0F, 0.5F)
                .setMirror(true);
        leftLeg.addBox(-0.1F, 0.0F, -2.0F, 3, 6, 3, scaleFactor);
        leftForeLeg = new ModelPart(original, 0, 16 + 6)
                .setParent(leftLeg)
                .setPosition(0, 6.0F, -2.0F)
                .setMirror(true);

        leftForeLeg.developBox(-0.1F, 0.0F, 0.0F, 3, 6, 3, scaleFactor)
                .inflate(0.01F, 0, 0.01F)
                .offsetTextureQuad(BoxSide.BOTTOM, 0, -6F)
                .create();
        leftLeg.setExtension(leftForeLeg);
        rightForeLeg = new ModelPart(original, 0, 16 + 6)
                .setParent(rightLeg)
                .setPosition(0, 6.0F, -2.0F);

        rightForeLeg.developBox(-3.9F, 0.0F, 0.0F, 3, 6, 3, scaleFactor)
                .inflate(0.01F, 0, 0.01F)
                .offsetTextureQuad(BoxSide.BOTTOM, 0, -6F)
                .create();
        rightLeg.setExtension(rightForeLeg);

        return true;
    }

    @Override
    public void storeVanillaModel(ModelThinZombie model) {
        ModelThinZombie vanillaModel = new ModelThinZombie(0.0F, 0.0f);
        this.vanillaModel = vanillaModel;
        super.storeVanillaModel(model);
    }

    @Override
    public boolean shouldModelBeSkipped(ModelBase model) {
        return !(model instanceof ModelThinZombie);
    }

}
