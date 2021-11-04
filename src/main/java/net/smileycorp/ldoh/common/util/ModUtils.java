package net.smileycorp.ldoh.common.util;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import mcjty.lostcities.dimensions.world.LostCityChunkGenerator;
import mcjty.lostcities.dimensions.world.lost.BuildingInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.entity.EntityCrawlingZombie;
import net.smileycorp.ldoh.common.entity.EntityDummyZombie0;
import net.smileycorp.ldoh.common.entity.EntityDummyZombie1;
import net.smileycorp.ldoh.common.entity.EntityDummyZombie2;
import net.smileycorp.ldoh.common.entity.EntityZombieNurse;
import net.tangotek.tektopia.Village;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;
import rafradek.TF2weapons.item.ItemWeapon;
import biomesoplenty.api.biome.BOPBiomes;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.legacy.wasteland.world.WastelandWorld;

public class ModUtils {

	public static ScheduledExecutorService DELAYED_THREAD_EXECUTOR = Executors.newSingleThreadScheduledExecutor();

	public static final AttributeModifier WASTELAND_MODIFIER = new AttributeModifier(UUID.fromString("22f4fa64-de73-4b45-9bb2-aae297639594"), "wasteland", 0.5, 2);

	//sets player team and prints according message
	public static void addPlayerToTeam(EntityPlayer player, String team) {
		Scoreboard scoreboard = player.world.getScoreboard();
		scoreboard.addPlayerToTeam(player.getName(), team);
		ITextComponent component = new TextComponentString(team);
		component.setStyle(new Style().setColor(scoreboard.getTeam(team).getColor()));
		player.sendMessage(new TextComponentTranslation(ModDefinitions.joinTeamMessage, new Object[]{component.getFormattedText()}));
		player.sendMessage(new TextComponentTranslation(ModDefinitions.postJoinTeamMessage));
		if (!player.world.isRemote) {
			for (EntityPlayer other : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
				if (other!=player) {
					player.sendMessage(new TextComponentTranslation(ModDefinitions.otherJoinTeamMessage, new Object[]{player.getName(), component.getFormattedText()}));
				}
			}
		}
	}

	//removes unnececary nbt from tf2 weapons to prevent a crash
	public static ItemStack cleanTFWeapon(ItemStack stack) {
		if (stack.getItem() instanceof ItemWeapon) {
			NBTTagCompound nbt = stack.getTagCompound();
			NBTTagCompound newNbt = new NBTTagCompound();
			if (nbt.hasKey("Type")) newNbt.setString("Type", nbt.getString("Type"));
			if (nbt.hasKey("Attributes")) newNbt.setTag("Attributes", nbt.getCompoundTag("Attributes"));
			stack = new ItemStack(stack.getItem(), 1, stack.getMetadata());
			stack.setTagCompound(newNbt);
		}
		return stack;
	}

	//sets speed modifiers
	public static void setEntitySpeed(EntityMob entity) {
		World world = entity.world;
		Biome biome = world.getBiome(entity.getPosition());
		IAttributeInstance speed = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		if (biome == BOPBiomes.wasteland.get()) {
			if (!speed.hasModifier(WASTELAND_MODIFIER)) speed.applyModifier(WASTELAND_MODIFIER);
		}
		if (world.getWorldTime()%24000 < 12000) if (speed.getModifier(DayTimeSpeedModifier.MODIFIER_UUID) == null) speed.applyModifier(new DayTimeSpeedModifier(world));
	}

	//gets the cost of an item for a particular tektopia village
	public  static int getCost(Village village, int baseCost) {
		float mult = Math.min((village.getTownData().getProfessionSales() / 5) * 0.2F, 10.0F);
		return (int)(baseCost * (1.0F + mult));
	}

	//checks if a 64/64 area around the position consists of only regular wasteland
	public static boolean isOnlyWasteland(World world, int x, int z) {
		for (Biome biome : world.getBiomeProvider().getBiomes(null, x-32, z-32, 64, 64, false)) if (biome!= WastelandWorld.apocalypse) return false;
		return true;
	}

	public static boolean isCity(World world, int x, int z) {
		for (Biome biome : world.getBiomeProvider().getBiomes(null, x<<4, z<<4, 16, 16, false)) if (biome == WastelandWorld.apocalypse_city) return true;
		if (world.getChunkProvider() instanceof ChunkProviderServer) {
			if (((ChunkProviderServer)world.getChunkProvider()).chunkGenerator instanceof LostCityChunkGenerator) {
				LostCityChunkGenerator gen = (LostCityChunkGenerator) ((ChunkProviderServer)world.getChunkProvider()).chunkGenerator;
				ChunkPos cpos = world.getChunkFromBlockCoords(new BlockPos(x, 0, z)).getPos();
				for (int i = -1; i<=1; i++) {
					for (int k = -1; k<=1; k++) {
						if (BuildingInfo.isCity(cpos.x + i, cpos.z + k, gen)) return true;
						else System.out.println("chunk at "+(cpos.x + i)+", "+(cpos.z + k)+" is not city");
					}
				}
			}
		}
		return false;
	}

	public static void tryJoinTeam(EntityPlayer player, EntityLivingBase entity) {
		//adds player to npc team
		System.out.println("wading");
		if (entity instanceof EntityTF2Character) {
			ModUtils.addPlayerToTeam(player, entity.getTeam().getName());
		}

	}

	public static void spawnHorde(World world, BlockPos basepos) {
		Random rand = world.rand;
		int day = Math.round(world.getWorldTime()/24000);
		if (world.getSpawnPoint().getDistance(basepos.getX(), basepos.getY(), basepos.getZ()) >= 200) {
			for (int i = 0; i < getRandomSize(rand); i++) {
				Vec3d dir = DirectionUtils.getRandomDirectionVecXZ(rand);
				BlockPos pos = DirectionUtils.getClosestLoadedPos(world, new BlockPos(basepos.getX(), 0, basepos.getZ()), dir, rand.nextInt(30)/10d);
				pos = new BlockPos(pos.getX()+rand.nextFloat(), world.getHeight(pos.getX(), pos.getZ()), pos.getZ()+rand.nextFloat());
				EntityMob entity = getEntity(world, rand, day, pos);
				entity.setPosition(pos.getX()+0.5f, pos.getY(), pos.getZ()+0.5f);
				entity.enablePersistence();
				entity.onAddedToWorld();
				entity.onInitialSpawn(world.getDifficultyForLocation(entity.getPosition()), null);
				world.spawnEntity(entity);
			}
		}
	}

	private static int getRandomSize(Random rand) {
		if (rand.nextInt(2) == 0) return rand.nextInt(5) + 15;
		return rand.nextInt(8) + 40;
	}

	private static EntityMob getEntity(World world, Random rand, int day, BlockPos pos) {
		if (day >= 50) return new EntityInfHuman(world);
		if (rand.nextInt(5) == 0) {
			return new EntityCrawlingZombie(world);
		}
		if (world.getBiomeProvider().getBiomes(null, pos.getX(), pos.getZ(), 1, 1, true)[0] == WastelandWorld.apocalypse_city) {
			int r = rand.nextInt(100);
			if (r < 10) return new EntityZombie(world);
			if (r < 20) return new EntityDummyZombie1(world);
			if (r < 40) return new EntityDummyZombie0(world);
			if (r == 40) return new EntityZombieNurse(world);
			else return new EntityDummyZombie2(world);
		}
		else {
			if (rand.nextInt(3) > day/10) {
				int r = rand.nextInt(10);
				if (r < 3) return new EntityDummyZombie2(world);
				else if (r < 5) return new EntityDummyZombie0(world);
				else if (r == 7) return new EntityDummyZombie1(world);
			}
		}
		return new EntityZombie(world);
	}
}
