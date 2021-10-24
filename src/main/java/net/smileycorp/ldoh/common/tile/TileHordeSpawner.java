package net.smileycorp.ldoh.common.tile;

import java.util.Random;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.ldoh.common.block.BlockHordeSpawner;
import net.smileycorp.ldoh.common.entity.EntityDumbZombie;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;

public class TileHordeSpawner extends TileEntity implements ITickable {
	
	private boolean spawned = false;

	@Override
	public void update() {
		if (!world.isRemote &! spawned) {
			Random rand = world.rand;
	    	int day = Math.round(world.getWorldTime()/24000);
	    	if (world.getSpawnPoint().getDistance(pos.getX(), pos.getY(), pos.getZ()) >= 200) {
		    	for (int i = 0; i < getRandomSize(rand); i++) {
		    		Vec3d dir = DirectionUtils.getRandomDirectionVecXZ(rand);
		    		BlockPos pos = DirectionUtils.getClosestLoadedPos(world, new BlockPos(this.pos.getX(), 0, this.pos.getZ()), dir, rand.nextInt(30)/10d);
		    		pos = new BlockPos(pos.getX(), world.getHeight(pos.getX(), pos.getZ()), pos.getZ());
		    		EntityMob entity = getEntity(rand, day);
		    		entity.onAddedToWorld();
					entity.setPosition(pos.getX()+0.5f, pos.getY(), pos.getZ()+0.5f);
					world.spawnEntity(entity);
					entity.enablePersistence();
		    	}
	    	}
	    	spawned = true;
	    	BlockHordeSpawner.breakBlock(world, pos);
		}
    }

	private int getRandomSize(Random rand) {
		if (rand.nextInt(2) == 0) return rand.nextInt(5) + 15;
		return rand.nextInt(8) + 40;
	}
	
	private EntityMob getEntity(Random rand, int day) {
		if (day >= 50) return new EntityInfHuman(world);
		else if (rand.nextInt(3) > day/10) {
			int r = rand.nextInt(10);
			if (r < 5) return new EntityDumbZombie(world);
		}
		return new EntityZombie(world);
	}

}
