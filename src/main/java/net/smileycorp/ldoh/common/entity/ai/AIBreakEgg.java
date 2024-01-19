package net.smileycorp.ldoh.common.entity.ai;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.smileycorp.atlas.api.entity.ai.EntityAIGoToPos;
import pavocado.exoticbirds.entity.Birds.Phoenix.EntityPhoenix;
import pavocado.exoticbirds.init.ExoticbirdsBlocks;
import pavocado.exoticbirds.init.ExoticbirdsMod;

import java.util.List;

public class AIBreakEgg extends EntityAIGoToPos {

	protected int ticksForCheck = 0;
	protected int ticksWaitedAtTarget = 0;
	protected final List<Block> eggBlocks = Lists.newArrayList(ExoticbirdsBlocks.nest, ExoticbirdsBlocks.phoenix_egg, ExoticbirdsBlocks.birdcage_acacia,
			ExoticbirdsBlocks.birdcage_birch, ExoticbirdsBlocks.birdcage_dark_oak, ExoticbirdsBlocks.birdcage_gold, ExoticbirdsBlocks.birdcage_iron,
			ExoticbirdsBlocks.birdcage_jungle, ExoticbirdsBlocks.birdcage_oak, ExoticbirdsBlocks.birdcage_spruce);

	public AIBreakEgg(EntityLiving entity) {
		super(entity, null);
	}

	@Override
	public boolean shouldExecute() {
		if (ticksForCheck-- <= 0) {
			ticksForCheck = 30;
			return isValidTarget(pos) ? true : findTarget();
			EntityPhoenix
		}
		return false;
	}

	private boolean findTarget() {
		BlockPos pos = entity.getPosition();
		for (int j = 0; j <= 3; j++) {
			for (int i = 0; i <= 24; i++) {
				for (int k = 0; k <= 24; k++) {
					BlockPos offset = pos.add(i, j, k);
					if (isValidTarget(offset)) {
						pos = offset;
						return true;
					}
					offset = pos.add(-i, -j, -k);
					if (isValidTarget(offset)) {
						pos = offset;
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isValidTarget(BlockPos pos) {
		if (pos == null) return false;
		Chunk chunk = entity.world.getChunkFromBlockCoords(pos);
		if (chunk != null) {
			Block block = chunk.getBlockState(pos).getBlock();
			if (eggBlocks.contains(block)) return true;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return isValidTarget(pos);
	}

	@Override
	public void resetTask() {
		pos = null;
		ticksForCheck = 30;
		ticksWaitedAtTarget = 0;
		super.resetTask();
	}

	@Override
	public void updateTask() {
		if (pos != null) {
			if (entity.getDistanceSqToCenter(pos) <= 1) {
				super.resetTask();
				if (ticksWaitedAtTarget++ >= 30) {
					entity.playSound(SoundEvents.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1f, 0.75f);
					world.setBlockToAir(pos);
					pos = null;
				} else if (ticksWaitedAtTarget % 5==0) entity.playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 1f, 0.75f);
			} else super.updateTask();
		}
	}

}
