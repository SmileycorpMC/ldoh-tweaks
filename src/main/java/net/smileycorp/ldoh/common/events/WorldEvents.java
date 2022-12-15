package net.smileycorp.ldoh.common.events;

import java.util.Random;

import com.legacy.wasteland.world.WastelandWorld;

import ivorius.reccomplex.events.StructureGenerationEvent;
import mcjty.lostcities.dimensions.world.LostCityChunkGenerator;
import mcjty.lostcities.dimensions.world.lost.BuildingInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;
import net.minecraftforge.event.world.WorldEvent.CreateSpawnPosition;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.ldoh.common.ConfigHandler;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.util.EnumBiomeType;
import net.smileycorp.ldoh.common.util.ModUtils;
import net.smileycorp.ldoh.common.world.WorldGenSafehouse;

@EventBusSubscriber(modid = ModDefinitions.MODID)
public class WorldEvents {

	//Spawn in World
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void setWorldSpawn(CreateSpawnPosition event) {
		World world = event.getWorld();
		//set gamerule to disable random offset when spawning
		if (world.getGameRules().getInt("spawnRadius")>0) world.getGameRules().setOrCreateGameRule("spawnRadius", "0");
		Random rand = world.rand;
		if (world.provider.getDimension() == 0) {
			WorldGenSafehouse safehouse = new WorldGenSafehouse();
			//tries to find a wasteland biome to spawn the player in
			Biome biome = null;
			int x = 0;
			int y = 0;
			int z = 0;
			int tries = 0;
			while (true) {
				if (EnumBiomeType.WASTELAND.matches(biome) || ConfigHandler.betaSpawnpoint) {
					y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
					//checks if the safehouse is spawning below y60 or if the structure bounds intersect with a city or another biome
					if (y <= 60 || !ModUtils.isOnlyWasteland(world, x, z) &! ConfigHandler.betaSpawnpoint) {
						y = 0;
						x += rand.nextInt(32) - rand.nextInt(32);
						z += rand.nextInt(32) - rand.nextInt(32);
					}
					else {
						//determines if the safehouse can be placed here
						if (ConfigHandler.betaSpawnpoint || safehouse.markPositions(world, new BlockPos(x, y-1, z), false)) break;
						y = 0;
						x += rand.nextInt(32) - rand.nextInt(32);
						z += rand.nextInt(32) - rand.nextInt(32);
					}
				} else if (tries % 10 == 0 && tries > 0) {
					Vec3d dir = DirectionUtils.getRandomDirectionVecXZ(rand);
					x += 100 * dir.x;
					z += 100 * dir.z;
				} else {
					x += rand.nextInt(64) - rand.nextInt(64);
					z += rand.nextInt(64) - rand.nextInt(64);
				}
				//gets the biome without loading chunks
				biome = world.getBiomeProvider().getBiomes(null, x, z, 1, 1, false)[0];
				tries++;

				//cancel after 3000 tries to not lock the game in an infinite loop
				if (tries >= 3000) {
					y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
					System.out.println("Found no suitable location for spawn");
					break;
				}
			}
			BlockPos spawn = new BlockPos(x, y, z);
			world.getWorldInfo().setSpawn(spawn);
			if (!ConfigHandler.noSafehouse) {
				if (!safehouse.isMarked()) {
					safehouse.markPositions(world, spawn.down(), true);
				}
				safehouse.generate(world, rand, world.getSpawnPoint().down());
			}
			event.setCanceled(true);
		}
	}

	//cancels recurrentcomplex structures from spawning in certain circumstances
	@SubscribeEvent
	public void structureGen(StructureGenerationEvent.Suggest event) {
		World world = event.getWorld();
		if (!world.isRemote) {
			//cancels structure if it intersects spawn chunks
			StructureBoundingBox box = event.spawnContext.boundingBox;
			BlockPos spawn = world.getSpawnPoint();
			if (box.intersectsWith(spawn.getX() - 20, spawn.getZ() - 20, spawn.getX() + 20, spawn.getZ() + 20)) {
				event.setCanceled(true);
				return;
			}
			IChunkProvider provider = world.getChunkProvider();
			//cancels structure if it's in a city
			for (int x = box.minX; x <= box.maxX; x++) {
				for (int z = box.minZ; z <= box.maxZ; z++) {
					if (world.getBiome(new BlockPos(x, 0, z)) == WastelandWorld.apocalypse_city) {
						event.setCanceled(true);
						return;
					}
				}
			}
			if (provider instanceof ChunkProviderServer) {
				IChunkGenerator gen = ((ChunkProviderServer)provider).chunkGenerator;
				if (gen instanceof LostCityChunkGenerator) {
					ChunkPos pos0 = world.getChunkFromBlockCoords(new BlockPos(box.minX, 0, box.minZ)).getPos();
					ChunkPos pos1 = world.getChunkFromBlockCoords(new BlockPos(box.maxX, 0, box.maxZ)).getPos();
					for (int i = pos0.x; i <= pos1.x; i++) {
						for (int j = pos0.z; j <= pos1.z; j++) {
							if (BuildingInfo.isCity(i, j, (LostCityChunkGenerator) gen)) {
								event.setCanceled(true);
								return;
							}
						}
					}
				}
			}
		}
	}

	//remove coal, iron and gold from generating outside of deserts so we can use our own generation
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void createDecorator(GenerateMinable event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		Biome biome = world.getBiome(pos);
		if (biome != WastelandWorld.apocalypse_desert) {
			GenerateMinable.EventType type = event.getType();
			if (type == GenerateMinable.EventType.COAL || type == GenerateMinable.EventType.IRON || type == GenerateMinable.EventType.GOLD) {
				event.setResult(Result.DENY);
			}
		}
	}

}
