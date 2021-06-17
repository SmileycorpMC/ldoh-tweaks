package net.smileycorp.hundreddayz.common.block;

import java.util.Random;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.hordes.common.hordeevent.HordeSpawnProvider;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;

public class TileEntityHordeSpawner extends TileEntity implements ITickable {

	public static final String TAG_UUID = "HUNDREDDAYZ-WORLD";

	@Override
	public void update() {
		if (!world.isRemote) {
			Random rand = world.rand;
	    	int day = Math.round(world.getWorldTime()/24000);
	    	if (world.getSpawnPoint().getDistance(pos.getX(), pos.getY(), pos.getZ()) > 50) {
		    	for (int i = 0; i < getRandomSize(rand); i++) {
		    		Vec3d dir = DirectionUtils.getRandomDirectionVecXZ(rand);
		    		BlockPos pos = DirectionUtils.getClosestLoadedPos(world, new BlockPos(this.pos.getX(), 0, this.pos.getZ()), dir, rand.nextInt(30)/10d);
		    		pos = new BlockPos(pos.getX(), world.getHeight(pos.getX(), pos.getZ()), pos.getZ());
		    		EntityMob entity = day >= 50 ? new EntityInfHuman(world) : new EntityZombie(world);
		    		entity.onAddedToWorld();
					entity.setPosition(pos.getX()+0.5f, pos.getY(), pos.getZ()+0.5f);
					world.spawnEntity(entity);
					entity.getCapability(HordeSpawnProvider.HORDESPAWN, null).setPlayerUUID(TAG_UUID);
					entity.enablePersistence();
		    	}
	    	}
	    	BlockHordeSpawner.breakBlock(world, pos);
		}
    }

	private int getRandomSize(Random rand) {
		if (rand.nextInt(2) == 0) return rand.nextInt(5) + 15;
		return rand.nextInt(8) + 40;
	}

}
