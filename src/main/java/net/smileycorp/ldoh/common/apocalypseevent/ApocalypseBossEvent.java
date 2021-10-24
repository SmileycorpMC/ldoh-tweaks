package net.smileycorp.ldoh.common.apocalypseevent;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.IOngoingEvent;
import net.smileycorp.atlas.api.util.DirectionUtils;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityParasiteBase;

public class ApocalypseBossEvent implements IOngoingEvent {
	
	private int timer = 0;
	private final EntityPlayer player;
	private boolean hasChanged = false;
	private int wave = 0;
	
	public ApocalypseBossEvent(EntityPlayer player) {
		this.player=player;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("timer")) {
			timer = nbt.getInteger("timer");
		}
		if (nbt.hasKey("wave")) {
			wave = nbt.getInteger("wave");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("timer", timer);
		nbt.setInteger("wave", wave);
		hasChanged = false;
		return nbt;
	}
	
	@Override
	public void update(World world) {
		if (!world.isRemote) {
			if (isActive(world)) {
				if ((timer == 0)) {
					spawnWave(world);
					if (wave==7) player.sendMessage(new TextComponentTranslation("message.hundreddayz.EventEnd"));
				}
				timer--;
				hasChanged = true;
			}
		}
	}
	
	@Override
	public boolean isActive(World world) {
		return player != null && wave  > 0 && wave < 7;
	}
	
	public boolean hasChanged() {
		return hasChanged;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}

	public void spawnWave(World world) {
		for (Class<? extends EntityParasiteBase> clazz : ApocalypseSpawnTable.getSpawnsForWave(wave, world.rand)) {
			Vec3d vec = DirectionUtils.getRandomDirectionVecXZ(world.rand);
			BlockPos localpos = DirectionUtils.getClosestLoadedPos(world, player.getPosition(), vec, 75);
			EntityLightningBolt bolt = new EntityLightningBolt(world, localpos.getX(), localpos.getY(), localpos.getZ(), true);
			world.spawnEntity(bolt);
			try {
				EntityParasiteBase entity = clazz.getConstructor(World.class).newInstance(world);
				entity.onAddedToWorld();
				entity.setPosition(localpos.getX(), localpos.getY(), localpos.getZ());
				world.spawnEntity(entity);
				entity.targetTasks.taskEntries.clear();
				entity.targetTasks.addTask(1, new EntityAIHurtByTarget(entity, true, new Class[] {EntityParasiteBase.class}));
				entity.targetTasks.addTask(2, new EntityAINearestAttackableTarget(entity, EntityPlayer.class, false));
				entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(150.0D);
			} catch (Exception e) {

			}
		}
		timer = 1000;
		wave++;
	}

	public void startEvent() {
		if (player!=null) {
			wave = 1;
			player.sendMessage(new TextComponentTranslation("message.hundreddayz.WorldsEnd"));
		}
	}
	
}
