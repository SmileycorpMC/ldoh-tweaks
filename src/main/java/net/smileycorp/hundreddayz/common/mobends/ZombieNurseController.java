package net.smileycorp.hundreddayz.common.mobends;

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
	public ZombieNurseController()
	{
		this.layerBase = new HardAnimationLayer<>();
		this.layerSet = new HardAnimationLayer<>();
		this.bitStand = new StandAnimationBit<>();
		this.bitWalk = new WalkAnimationBit<>();
		this.bitJump = new JumpAnimationBit<>();
		this.bitAnimationSet = new AnimationBit[] {
			new ZombieLeanAnimationBit(),
			new ZombieStumblingAnimationBit()
		};
	}
	
	@Override
	public Collection<String> perform(ZombieNurseData zombieData)
	{
		if (!zombieData.isOnGround() || zombieData.getTicksAfterTouchdown() < 1)
		{
			this.layerBase.playOrContinueBit(bitJump, zombieData);
		}
		else
		{
			if (zombieData.isStillHorizontally())
			{
				this.layerBase.playOrContinueBit(bitStand, zombieData);
			}
			else
			{
				this.layerBase.playOrContinueBit(bitWalk, zombieData);
			}
		}
		
		this.layerSet.playOrContinueBit(bitAnimationSet[zombieData.getAnimationSet()], zombieData);
		
		final List<String> actions = new ArrayList<>();
		this.layerBase.perform(zombieData, actions);
		this.layerSet.perform(zombieData, actions);
		return actions;
	}
	
}
