package net.smileycorp.ldoh.integration.mobends.fireman;

import goblinbob.mobends.core.animation.bit.AnimationBit;
import goblinbob.mobends.core.animation.controller.IAnimationController;
import goblinbob.mobends.core.animation.layer.HardAnimationLayer;
import goblinbob.mobends.standard.animation.bit.biped.JumpAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.StandAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.WalkAnimationBit;
import goblinbob.mobends.standard.animation.bit.zombie_base.ZombieLeanAnimationBit;
import goblinbob.mobends.standard.animation.bit.zombie_base.ZombieStumblingAnimationBit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ZombieFiremanController implements IAnimationController<ZombieFiremanData> {

    protected HardAnimationLayer<ZombieFiremanData> layerBase;
    protected HardAnimationLayer<ZombieFiremanData> layerSet;
    protected AnimationBit<ZombieFiremanData> bitStand, bitWalk, bitJump;
    protected AnimationBit<ZombieFiremanData>[] bitAnimationSet;

    @SuppressWarnings("unchecked")
    public ZombieFiremanController() {
        this.layerBase = new HardAnimationLayer<>();
        this.layerSet = new HardAnimationLayer<>();
        this.bitStand = new StandAnimationBit<>();
        this.bitWalk = new WalkAnimationBit<>();
        this.bitJump = new JumpAnimationBit<>();
        this.bitAnimationSet = new AnimationBit[]{
                new ZombieLeanAnimationBit(),
                new ZombieStumblingAnimationBit()
        };
    }

    @Override
    public Collection<String> perform(ZombieFiremanData data) {
        if (!data.isOnGround() || data.getTicksAfterTouchdown() < 1) {
            this.layerBase.playOrContinueBit(bitJump, data);
        } else {
            if (data.isStillHorizontally()) {
                this.layerBase.playOrContinueBit(bitStand, data);
            } else {
                this.layerBase.playOrContinueBit(bitWalk, data);
            }
        }
        this.layerSet.playOrContinueBit(bitAnimationSet[data.getAnimationSet()], data);
        final List<String> actions = new ArrayList<>();
        this.layerBase.perform(data, actions);
        this.layerSet.perform(data, actions);
        return actions;
    }

}
