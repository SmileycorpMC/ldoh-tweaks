package net.smileycorp.ldoh.common;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.ldoh.common.block.BlockBarbedWire;
import net.smileycorp.ldoh.common.block.BlockHordeSpawner;
import net.smileycorp.ldoh.common.block.BlockLandmine;
import net.smileycorp.ldoh.common.capabilities.IBreakBlocks;
import net.smileycorp.ldoh.common.capabilities.IHunger;
import net.smileycorp.ldoh.common.capabilities.IMiniRaid;
import net.smileycorp.ldoh.common.capabilities.ISpawnTracker;
import net.smileycorp.ldoh.common.capabilities.IUnburiedSpawner;
import net.smileycorp.ldoh.common.damage.DamageSourceToxicGas;
import net.smileycorp.ldoh.common.entity.EntityCrawlingHusk;
import net.smileycorp.ldoh.common.entity.EntityCrawlingZombie;
import net.smileycorp.ldoh.common.entity.EntityDumbZombie;
import net.smileycorp.ldoh.common.entity.EntityLDOHArchitect;
import net.smileycorp.ldoh.common.entity.EntityLDOHTradesman;
import net.smileycorp.ldoh.common.entity.EntitySwatZombie;
import net.smileycorp.ldoh.common.entity.EntityTFZombie;
import net.smileycorp.ldoh.common.entity.EntityZombieMechanic;
import net.smileycorp.ldoh.common.entity.EntityZombieNurse;
import net.smileycorp.ldoh.common.entity.EntityZombieTechnician;
import net.smileycorp.ldoh.common.item.ItemBarbedWire;
import net.smileycorp.ldoh.common.item.ItemBase;
import net.smileycorp.ldoh.common.item.ItemBlockTooltip;
import net.smileycorp.ldoh.common.item.ItemGasMask;
import net.smileycorp.ldoh.common.item.ItemHat;
import net.smileycorp.ldoh.common.item.ItemHelmet;
import net.smileycorp.ldoh.common.item.ItemSpawner;
import net.smileycorp.ldoh.common.item.ItemSyringe;
import net.smileycorp.ldoh.common.item.ItemTFProfessionToken;
import net.smileycorp.ldoh.common.item.ItemWeapon;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;
import net.smileycorp.ldoh.common.tile.TileHordeSpawner;
import net.smileycorp.ldoh.common.tile.TileLandmine;
import net.smileycorp.ldoh.common.world.ModWorldGen;

import org.apache.commons.lang3.ArrayUtils;

import com.Fishmod.mod_LavaCow.init.FishItems;

@EventBusSubscriber(modid = ModDefinitions.modid)
public class ModContent {

	@CapabilityInject(ISpawnTracker.class)
	public final static Capability<ISpawnTracker> SPAWN_TRACKER = null;

	@CapabilityInject(IBreakBlocks.class)
	public final static Capability<IBreakBlocks> BLOCK_BREAKING = null;

	@CapabilityInject(IUnburiedSpawner.class)
	public final static Capability<IUnburiedSpawner> UNBURIED_SPAWNER = null;

	@CapabilityInject(IMiniRaid.class)
	public final static Capability<IMiniRaid> MINI_RAID = null;

	@CapabilityInject(IHunger.class)
	public final static Capability<IHunger> HUNGER = null;


	public static CreativeTabs CREATIVE_TAB = new CreativeTabs(ModDefinitions.getName("HundredDayzTab")){
		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(SYRINGE);
		}
	};

	public static final Item SPAWNER = new ItemSpawner();
	public static final Item SYRINGE = new ItemSyringe();
	public static final Item CLOTH_FABRIC = new ItemBase("Clothing_Fabric");
	public static final Item DIAMOND_NUGGET = new ItemBase("Diamond_Nugget");
	public static final Item GAS_FILTER = new ItemBase("Gas_Filter").setMaxStackSize(1);
	public static final Item GAS_MASK = new ItemGasMask();
	public static final Item NURSE_HAT = new ItemHat("Nurse_Hat");
	public static final Item MECHANIC_HAT = new ItemHat("Mechanic_Hat");
	public static final Item HARDHAT = new ItemHelmet("Hardhat", 100, 2, 0f);
	public static final Item BONESAW = new ItemWeapon("Bonesaw", 100, 5.5);
	public static final Item TF_PROF_TOKEN = new ItemTFProfessionToken();

	public static final Block HORDE_SPAWNER = new BlockHordeSpawner();
	public static final Block BARBED_WIRE = new BlockBarbedWire();
	public static final Block LANDMINE = new BlockLandmine();

	public static final DamageSource TOXIC_GAS_DAMAGE = new DamageSourceToxicGas();
	public static final DamageSource SHRAPNEL_DAMAGE = new DamageSource("Shrapnel");

	public static final SoundEvent TF_ENEMY_SOUND = new SoundEvent(ModDefinitions.getResource("tf_enemy"));
	public static final SoundEvent TF_ALLY_SOUND = new SoundEvent(ModDefinitions.getResource("tf_ally"));

	public static Item[] items = {TF_PROF_TOKEN, SPAWNER, SYRINGE, CLOTH_FABRIC, DIAMOND_NUGGET, GAS_FILTER, GAS_MASK, NURSE_HAT, MECHANIC_HAT, HARDHAT, BONESAW};
	public static Block[] blocks = {HORDE_SPAWNER, BARBED_WIRE, LANDMINE};

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		registry.registerAll(blocks);
		GameRegistry.registerTileEntity(TileBarbedWire.class, ModDefinitions.getResource("barbed_wire"));
		GameRegistry.registerTileEntity(TileHordeSpawner.class, ModDefinitions.getResource("horde_spawner"));
		GameRegistry.registerTileEntity(TileLandmine.class, ModDefinitions.getResource("landmine"));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		items = ArrayUtils.addAll(items, new ItemBarbedWire(), new ItemBlockTooltip(LANDMINE, 2));
		registry.registerAll(items);
	}

	@SuppressWarnings("unused")
	private static Item createItem(Block block) {
		return new ItemBlock(block).setUnlocalizedName(block.getUnlocalizedName().substring(4)).setRegistryName(block.getRegistryName());
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		GameRegistry.registerWorldGenerator(new ModWorldGen(), 0);
		int ID = 201;
		IForgeRegistry<EntityEntry> registry = event.getRegistry();
		EntityEntry DUMB_ZOMBIE = EntityEntryBuilder.create().entity(EntityDumbZombie.class)
				.id(ModDefinitions.getResource("dumb_zombie"), ID++)
				.name(ModDefinitions.getName("DumbZombie")).tracker(80, 3, true).build();
		registry.register(DUMB_ZOMBIE);
		EntityEntry CRAWLING_ZOMBIE = EntityEntryBuilder.create().entity(EntityCrawlingZombie.class)
				.id(ModDefinitions.getResource("crawling_zombie"), ID++)
				.name(ModDefinitions.getName("CrawlingZombie")).tracker(80, 3, true).build();
		registry.register(CRAWLING_ZOMBIE);
		EntityEntry CRAWLING_HUSK = EntityEntryBuilder.create().entity(EntityCrawlingHusk.class)
				.id(ModDefinitions.getResource("crawling_husk"), ID++)
				.name(ModDefinitions.getName("CrawlingHusk")).tracker(80, 3, true).build();
		registry.register(CRAWLING_HUSK);
		EntityEntry TF_ZOMBIE = EntityEntryBuilder.create().entity(EntityTFZombie.class)
				.id(ModDefinitions.getResource("tf_zombie"), ID++)
				.name(ModDefinitions.getName("TFZombie")).tracker(80, 3, true).build();
		registry.register(TF_ZOMBIE);
		EntityEntry NURSE_ZOMBIE = EntityEntryBuilder.create().entity(EntityZombieNurse.class)
				.id(ModDefinitions.getResource("nurse_zombie"), ID++)
				.name(ModDefinitions.getName("NurseZombie")).tracker(80, 3, true).build();
		registry.register(NURSE_ZOMBIE);
		EntityEntry SWAT_ZOMBIE = EntityEntryBuilder.create().entity(EntitySwatZombie.class)
				.id(ModDefinitions.getResource("swat_zombie"), ID++)
				.name(ModDefinitions.getName("SwatZombie")).tracker(80, 3, true).build();
		registry.register(SWAT_ZOMBIE);
		EntityEntry ZOMBIE_MECHANIC = EntityEntryBuilder.create().entity(EntityZombieMechanic.class)
				.id(ModDefinitions.getResource("zombie_mechanic"), ID++)
				.name(ModDefinitions.getName("ZombieMechanic")).tracker(80, 3, true).build();
		registry.register(ZOMBIE_MECHANIC);
		EntityEntry ZOMBIE_TECHNICIAN = EntityEntryBuilder.create().entity(EntityZombieTechnician.class)
				.id(ModDefinitions.getResource("zombie_technician"), ID++)
				.name(ModDefinitions.getName("ZombieTechnician")).tracker(80, 3, true).build();
		registry.register(ZOMBIE_TECHNICIAN);
		EntityEntry ARCHITECT = EntityEntryBuilder.create().entity(EntityLDOHArchitect.class)
				.id(ModDefinitions.getResource("architect"), ID++)
				.name("villager.architect").tracker(80, 3, true).build();
		registry.register(ARCHITECT);
		EntityEntry TRADESMAN = EntityEntryBuilder.create().entity(EntityLDOHTradesman.class)
				.id(ModDefinitions.getResource("tradesman"), ID++)
				.name("villager.tradesman").tracker(80, 3, true).build();
		registry.register(TRADESMAN);

	}

	@SubscribeEvent
	public static void registerOredict(RegistryEvent.Register<IRecipe> event) {
		OreDictionary.registerOre("fabric", CLOTH_FABRIC);
		OreDictionary.registerOre("fabric", FishItems.CURSED_FABRIC);
		OreDictionary.registerOre("nuggetDiamond",DIAMOND_NUGGET);
	}

}
