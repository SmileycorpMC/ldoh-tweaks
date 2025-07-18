package net.smileycorp.ldoh.integration.mobends.reaver;

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

public class ReaverController implements IAnimationController<ReaverData> {

    protected HardAnimationLayer<ReaverData> layerBase;
    protected HardAnimationLayer<ReaverData> layerSet;
    protected AnimationBit<ReaverData> bitStand, bitWalk, bitJump;
    protected AnimationBit<ReaverData>[] bitAnimationSet;

    //clone of ZombieController to handle our model
    @SuppressWarnings("unchecked")
    public ReaverController() {
        layerBase = new HardAnimationLayer<>();
        layerSet = new HardAnimationLayer<>();
        bitStand = new StandAnimationBit<>();
        bitWalk = new WalkAnimationBit<>();
        bitJump = new JumpAnimationBit<>();
        bitAnimationSet = (AnimationBit<ReaverData>[]) new AnimationBit[]{
                new ZombieLeanAnimationBit(),
                new ZombieStumblingAnimationBit()
        };
    }

    @Override
    public Collection<String> perform(ReaverData zombieData) {
        if (!zombieData.isOnGround() || zombieData.getTicksAfterTouchdown() < 1) {
            layerBase.playOrContinueBit(bitJump, zombieData);
        } else {
            if (zombieData.isStillHorizontally()) {
                layerBase.playOrContinueBit(bitStand, zombieData);
            } else {
                layerBase.playOrContinueBit(bitWalk, zombieData);
            }
        }

        layerSet.playOrContinueBit(bitAnimationSet[zombieData.getAnimationSet()], zombieData);

        final List<String> actions = new ArrayList<>();
        layerBase.perform(zombieData, actions);
        layerSet.perform(zombieData, actions);
        return actions;
    }

}
