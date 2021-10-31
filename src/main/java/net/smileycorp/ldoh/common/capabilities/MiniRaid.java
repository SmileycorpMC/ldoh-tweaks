package net.smileycorp.ldoh.common.capabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.ldoh.common.entity.EntityMiniRaidAI;
import net.smileycorp.ldoh.common.entity.EntitySwatZombie;
import net.smileycorp.ldoh.common.entity.EntityZombieMechanic;
import net.smileycorp.ldoh.common.entity.EntityZombieNurse;
import net.smileycorp.ldoh.common.entity.EntityZombieTechnician;
import net.smileycorp.ldoh.common.util.EnumTFClass;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityBano;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityCanra;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityEmana;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityHull;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityNogla;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityRanrac;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityShyco;

public class MiniRaid implements IMiniRaid {

	protected static final int[] times = {315000, 434000, 606000, 660000, 828000, 1094000, 1172000, 1373000, 1646000, 1756000, 1832000, 2130000};
	protected static final RaidType[] types = {RaidType.ALLY, RaidType.ALLY, RaidType.ZOMBIE, RaidType.ALLY, RaidType.ENEMY, RaidType.ZOMBIE, RaidType.ALLY, RaidType.ALLY, RaidType.PARASITE, RaidType.ENEMY, RaidType.ALLY, RaidType.PARASITE};

	protected int phase = 0;
	protected int cooldown = 0;

	protected EntityPlayer player = null;

	public MiniRaid() {
		this(null);
	}

	public MiniRaid(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public boolean shouldSpawnRaid() {
		if (player == null || phase >= types.length) return false;
		if (player.world.getWorldTime() >= times[phase] && cooldown-- <= 0) return true;
		return false;
	}

	@Override
	public void spawnRaid() {
		if (player!=null) {
			RaidType type = types[phase];
			World world = player.world;
			if (!world.isRemote) {
				Random rand = world.rand;
				BlockPos pos = DirectionUtils.getClosestLoadedPos(world, player.getPosition(), DirectionUtils.getRandomDirectionVecXZ(rand), 75);
				for(EntityLiving entity : buildList(world, type, phase)) {
					double x = pos.getX() + (rand.nextFloat() * 2) - 1;
					double z = pos.getZ() + (rand.nextFloat() * 2) - 1;
					int y = ((WorldServer)world).getHeight(new BlockPos(x, 0, z)).getY();
					entity.setPosition(x, y, z);
					entity.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
					entity.tasks.addTask(1, new EntityMiniRaidAI(entity, player));
					world.spawnEntity(entity);
				}
			}
			cooldown = 6000;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("phase", phase);
		nbt.setInteger("cooldown", cooldown);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("phase")) {
			phase = nbt.getInteger("phase");
		}
		if (nbt.hasKey("cooldown")) {
			cooldown = nbt.getInteger("cooldown");
		}
	}

	private List<EntityLiving> buildList(World world, RaidType type, int phase) {
		Random rand = world.rand;
		List<EntityLiving> spawnlist = new ArrayList<EntityLiving>();
		switch (type) {
		case ALLY:
			if (player.getTeam() == null || (player.getTeam().getName() == "RED") || player.getTeam().getName() == "BLU") break;
			for (int i = 0; i < phase * 2.5; i++)
				try {
					EntityTF2Character entity = EnumTFClass.getRandomClass().createEntity(world);
					world.getScoreboard().addPlayerToTeam(entity.getCachedUniqueIdString(), player.getTeam().getName());
					entity.setEntTeam(player.getTeam().getName() == "RED" ? 0 : 1);
					spawnlist.add(entity);
				} catch (Exception e) {}
			break;
		case ENEMY:
			if (player.getTeam() == null || !(player.getTeam().getName() == "RED" || player.getTeam().getName() == "BLU"))
				return buildList(world, phase < 8 ? RaidType.ZOMBIE : RaidType.PARASITE, phase);
			for (int i = 0; i < phase * 2.5; i++)
				try {
					EntityTF2Character entity = EnumTFClass.getRandomClass().createEntity(world);
					world.getScoreboard().addPlayerToTeam(entity.getCachedUniqueIdString(), player.getTeam().getName() == "RED" ? "BLU" : "RED");
					entity.setEntTeam(player.getTeam().getName() == "RED" ? 1 : 0);
					spawnlist.add(entity);
				} catch (Exception e) {}
			break;
		case PARASITE:
			for (int i = 0; i < phase*0.5; i++) {
				int r = rand.nextInt(7);
				if (r == 0) spawnlist.add(new EntityShyco(world));
				else if (r == 1) spawnlist.add(new EntityCanra(world));
				else if (r == 2) spawnlist.add(new EntityNogla(world));
				else if (r == 3) spawnlist.add(new EntityHull(world));
				else if (r == 4) spawnlist.add(new EntityEmana(world));
				else if (r == 5) spawnlist.add(new EntityBano(world));
				else if (r == 6) spawnlist.add(new EntityRanrac(world));
			}
			for (int i = 0; i < phase*1.5; i++) spawnlist.add(new EntityInfHuman(world));
			break;
		case ZOMBIE:
			spawnlist.add(new EntityZombieNurse(world));
			for (int i = 0; i < phase; i++) {
				int r = rand.nextInt(3);
				if (r == 0) spawnlist.add(new EntitySwatZombie(world));
				else if (r == 1) spawnlist.add(new EntityZombieMechanic(world));
				else if (r == 2) spawnlist.add(new EntityZombieTechnician(world));
			}
			for (int i = 0; i < phase*2.5; i++) spawnlist.add(new EntityZombie(world));
			break;
		}
		return spawnlist;
	}

}