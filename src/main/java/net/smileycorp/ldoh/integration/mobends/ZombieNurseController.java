package net.smileycorp.ldoh.integration.mobends;

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

public class ZombieNurseController implements IAnimationController<ZombieNurseData> {

	protected HardAnimationLayer<ZombieNurseData> layerBase;
	protected HardAnimationLayer<ZombieNurseData> layerSet;
	protected AnimationBit<ZombieNurseData> bitStand, bitWalk, bitJump;
	protected AnimationBit<ZombieNurseData>[] bitAnimationSet;

	//clone of ZombieController to handle our model
	@SuppressWarnings("unchecked")
	public ZombieNurseController()
	{
		layerBase = new HardAnimationLayer<ZombieNurseData>();
		layerSet = new HardAnimationLayer<ZombieNurseData>();
		bitStand = new StandAnimationBit<ZombieNurseData>();
		bitWalk = new WalkAnimationBit<ZombieNurseData>();
		bitJump = new JumpAnimationBit<ZombieNurseData>();
		bitAnimationSet = (AnimationBit<ZombieNurseData>[]) new AnimationBit[] {
			new ZombieLeanAnimationBit(),
			new ZombieStumblingAnimationBit()
		};
	}

	@Override
	public Collection<String> perform(ZombieNurseData zombieData)
	{
		if (!zombieData.isOnGround() || zombieData.getTicksAfterTouchdown() < 1)
		{
			layerBase.playOrContinueBit(bitJump, zombieData);
		}
		else
		{
			if (zombieData.isStillHorizontally())
			{
				layerBase.playOrContinueBit(bitStand, zombieData);
			}
			else
			{
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
