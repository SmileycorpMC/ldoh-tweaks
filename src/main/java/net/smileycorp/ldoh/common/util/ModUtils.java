package net.smileycorp.ldoh.common.util;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import mcjty.lostcities.dimensions.world.LostCityChunkGenerator;
import mcjty.lostcities.dimensions.world.lost.BuildingInfo;
import net.insane96mcp.iguanatweaks.modules.ModuleMovementRestriction;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.hordes.infection.HordesInfection;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.capabilities.IVillageData;
import net.smileycorp.ldoh.common.capabilities.LDOHCapabilities;
import net.smileycorp.ldoh.common.entity.EntityCrawlingZombie;
import net.smileycorp.ldoh.common.entity.EntityDummyZombie0;
import net.smileycorp.ldoh.common.entity.EntityDummyZombie1;
import net.smileycorp.ldoh.common.entity.EntityDummyZombie2;
import net.smileycorp.ldoh.common.entity.EntityZombieFireman;
import net.smileycorp.ldoh.common.entity.EntityZombieNurse;
import net.tangotek.tektopia.Village;
import net.tangotek.tektopia.entities.EntityVillagerTek;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;
import rafradek.TF2weapons.item.ItemWeapon;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.legacy.wasteland.world.WastelandWorld;

public class ModUtils {

	public static ScheduledExecutorService DELAYED_THREAD_EXECUTOR = Executors.newSingleThreadScheduledExecutor();

	public static final AttributeModifier WASTELAND_MODIFIER = new AttributeModifier(UUID.fromString("22f4fa64-de73-4b45-9bb2-aae297639594"), "wasteland", 0.5, 2);
	public static final AttributeModifier FOLLOW_MODIFIER = new AttributeModifier(UUID.fromString("3dc892c7-0def-42d5-8e7f-bb9f00136ad9"), "follow", -1, 2);
	public static final AttributeModifier TIRED_MODIFIER = new AttributeModifier(UUID.fromString("d92e0875-9115-4d73-947d-905957cd4a72"), "tired", -0.5, 2);

	//sets player team and prints according message
	public static void addPlayerToTeam(EntityPlayer player, String team) {
		Scoreboard scoreboard = player.world.getScoreboard();
		scoreboard.addPlayerToTeam(player.getName(), team);
		ITextComponent component = new TextComponentString(team);
		component.setStyle(new Style().setColor(scoreboard.getTeam(team).getColor()));
		player.sendMessage(new TextComponentTranslation(ModDefinitions.JOIN_TEAM_MESSAGE, new Object[]{component.getFormattedText()}));
		player.sendMessage(new TextComponentTranslation(ModDefinitions.POST_JOIN_TEAM_MESSAGE));
		if (!player.world.isRemote) {
			for (EntityPlayer other : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
				if (other!=player) {
					player.sendMessage(new TextComponentTranslation(ModDefinitions.OTHER_JOIN_TEAM_MESSAGE, new Object[]{player.getName(), component.getFormattedText()}));
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
		if (EnumBiomeType.BADLANDS.matches(biome)) {
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
		for (Biome biome : world.getBiomeProvider().getBiomes(null, x-64, z-64, 128, 128, false)) if (biome!= WastelandWorld.apocalypse) return false;
		return true;
	}

	public static boolean isCity(World world, int x, int z) {
		for (Biome biome : world.getBiomeProvider().getBiomes(null, x<<4, z<<4, 16, 16, false)) if (EnumBiomeType.CITY.matches(biome)) return true;
		if (world.getChunkProvider() instanceof ChunkProviderServer) {
			if (((ChunkProviderServer)world.getChunkProvider()).chunkGenerator instanceof LostCityChunkGenerator) {
				LostCityChunkGenerator gen = (LostCityChunkGenerator) ((ChunkProviderServer)world.getChunkProvider()).chunkGenerator;
				ChunkPos cpos = world.getChunkFromBlockCoords(new BlockPos(x, 0, z)).getPos();
				for (int i = -1; i<=1; i++) {
					for (int k = -1; k<=1; k++) {
						if (BuildingInfo.isCity(cpos.x + i, cpos.z + k, gen)) return true;
					}
				}
			}
		}
		return false;
	}

	public static void tryJoinTeam(EntityPlayer player, EntityLivingBase entity) {
		//adds player to npc team
		if (entity instanceof EntityTF2Character) {
			ModUtils.addPlayerToTeam(player, entity.getTeam().getName());
		}
	}

	public static boolean canTarget(EntityLivingBase entity, EntityLivingBase target) {
		if (entity == target) return false;
		if (entity != null && target != null) {
			if (target instanceof EntityPlayer) if (((EntityPlayer) target).isSpectator()) return false;
			if (entity.getTeam() != null) { if (target.getTeam() != null || target instanceof EntityMob) return !entity.getTeam().isSameTeam(target.getTeam());
			} else return target instanceof EntityMob &!(target instanceof EntityTF2Character);
		}
		return false;
	}

	public static boolean shouldHeal(EntityLivingBase entity, EntityLivingBase target) {
		if (entity == target) return false;
		if (entity != null && target != null) {
			if (target instanceof EntityPlayer) if (((EntityPlayer) target).isSpectator()) return false;
			if (target instanceof EntityPlayer || target instanceof EntityVillagerTek || target instanceof EntityTF2Character) {
				if (!canTarget(entity, target)) {
					if (target.getHealth() < target.getMaxHealth() || target.isPotionActive(HordesInfection.INFECTED)) return true;
				}
			}
		}
		return false;
	}

	public static BlockPos readPosFromNBT(NBTTagCompound nbt) {
		return new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
	}

	public static NBTTagCompound writePosToNBT(BlockPos pos) {
		NBTTagCompound nbt = new NBTTagCompound();
		if (pos != null) {
			nbt.setInteger("x", pos.getX());
			nbt.setInteger("y", pos.getY());
			nbt.setInteger("z", pos.getZ());
		}
		return nbt;
	}

	public static String getPosString(BlockPos pos) {
		return "(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
	}

	public static Vec3d getVecFromPathPoint(PathPoint pathPoint) {
		return new Vec3d(pathPoint.x, pathPoint.y, pathPoint.z);
	}

	public static boolean isTooFarFromVillage(EntityLiving entity, IBlockAccess world) {
		IVillageData cap = entity.getCapability(LDOHCapabilities.VILLAGE_DATA, null);
		if (!cap.hasVillage()) return false;
		BlockPos village = cap.getVillage().getCenter();
		return entity.getDistance(village.getX(), village.getY(), village.getZ()) >= 75;
	}

	public static RayTraceResult rayTrace(World world, EntityLivingBase entity, float distance) {
		Vec3d eyepos = entity.getPositionEyes(1f);
		Vec3d lookangle = entity.getLook(1f);
		Vec3d lastVec = eyepos.addVector(lookangle.x, lookangle.y, lookangle.z);
		RayTraceResult blockRay = world.rayTraceBlocks(eyepos, eyepos.addVector(lookangle.x * distance, lookangle.y * distance, lookangle.z * distance), false, true, false);
		for (int x = 0; x <16*distance; x++) {
			float reach = x/16f;
			Vec3d vec = eyepos.addVector(lookangle.x*reach, lookangle.y*reach, lookangle.z*reach);
			if (!(blockRay == null || blockRay.hitVec == null)) if (blockRay.hitVec.distanceTo(eyepos) < vec.distanceTo(eyepos)) break;
			AxisAlignedBB AABB = new AxisAlignedBB(lastVec.x, lastVec.y, lastVec.z, vec.x, vec.y, vec.z);
			List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(entity, AABB);
			if (!entities.isEmpty()) {
				return new RayTraceResult(entities.get(0), lookangle);
			}
			lastVec = vec;
		}
		return blockRay;
	}

	public static void spawnHorde(World world, BlockPos basepos, boolean isNatural) {
		Random rand = world.rand;
		int day = Math.round(world.getWorldTime()/24000);
		int c = day >= 101 ? 20 : day>= 50 ? day-40 : -1;
		boolean isParasite = day >= 50 && rand.nextInt(100) <= c;
		if (!isNatural || world.getSpawnPoint().getDistance(basepos.getX(), basepos.getY(), basepos.getZ()) >= 200) {
			for (int i = 0; i < getRandomSize(rand); i++) {
				Vec3d dir = DirectionUtils.getRandomDirectionVecXZ(rand);
				BlockPos pos = DirectionUtils.getClosestLoadedPos(world, new BlockPos(basepos.getX(), 0, basepos.getZ()), dir, rand.nextInt(30)/10d);
				pos = new BlockPos(pos.getX()+rand.nextFloat(), world.getHeight(pos.getX(), pos.getZ()), pos.getZ()+rand.nextFloat());
				EntityMob entity = isParasite? new EntityInfHuman(world) : getEntity(world, rand, day, pos);
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
		if (rand.nextInt(7) == 0) {
			return new EntityCrawlingZombie(world);
		}
		if (world.getBiomeProvider().getBiomes(null, pos.getX(), pos.getZ(), 1, 1, true)[0] == WastelandWorld.apocalypse_city) {
			int r = rand.nextInt(100);
			if (r <= 1) return new EntityZombieNurse(world);
			else if (r <= 3) return new EntityZombieFireman(world);
			if (day < 10 || r < 25) new EntityDummyZombie2(world);
			else if (day < 20 || r < 50) return new EntityDummyZombie1(world);
			else if (r < 75) return new EntityDummyZombie0(world);
		}
		else {
			if (day < 10) return new EntityDummyZombie2(world);
			else if (day < 20) return new EntityDummyZombie1(world);
		}
		return new EntityZombie(world);
	}

	public static float calculateCrateWeight(ItemStack stack, float weight) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt = stack.writeToNBT(nbt);
		NBTTagList items = nbt.getTagList("Items", 10);
		for (int i = 0; i < items.tagCount(); i++){
			NBTTagCompound itemTags = items.getCompoundTagAt(i);
			ItemStack stackInBox = new ItemStack(Item.getByNameOrId(itemTags.getString("id")), itemTags.getByte("Count"), itemTags.getShort("Damage"));
			Block blockInBox = Block.getBlockFromItem(stackInBox.getItem());
			if (!blockInBox.equals(Blocks.AIR) && !stack.getItem().equals(Items.AIR))
				weight += ModuleMovementRestriction.getItemWeight(stackInBox) * stackInBox.getCount();
			if (weight == 0f)
				weight = 1f / 64f * stack.getCount();
		}
		return weight;
	}

}
