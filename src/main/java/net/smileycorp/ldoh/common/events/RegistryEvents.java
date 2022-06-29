package net.smileycorp.ldoh.common.events;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.FuelHandler;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.block.LDOHBlocks;
import net.smileycorp.ldoh.common.entity.EntityCrawlingHusk;
import net.smileycorp.ldoh.common.entity.EntityCrawlingZombie;
import net.smileycorp.ldoh.common.entity.EntityDummyHusk0;
import net.smileycorp.ldoh.common.entity.EntityDummyHusk1;
import net.smileycorp.ldoh.common.entity.EntityDummyHusk2;
import net.smileycorp.ldoh.common.entity.EntityDummyZombie0;
import net.smileycorp.ldoh.common.entity.EntityDummyZombie1;
import net.smileycorp.ldoh.common.entity.EntityDummyZombie2;
import net.smileycorp.ldoh.common.entity.EntityIncendiaryProjectile;
import net.smileycorp.ldoh.common.entity.EntityLDOHArchitect;
import net.smileycorp.ldoh.common.entity.EntityLDOHTradesman;
import net.smileycorp.ldoh.common.entity.EntitySwatZombie;
import net.smileycorp.ldoh.common.entity.EntityTF2Zombie;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.entity.EntityZombieFireman;
import net.smileycorp.ldoh.common.entity.EntityZombieMechanic;
import net.smileycorp.ldoh.common.entity.EntityZombieNurse;
import net.smileycorp.ldoh.common.entity.EntityZombieTechnician;
import net.smileycorp.ldoh.common.item.ItemBarbedWire;
import net.smileycorp.ldoh.common.item.ItemBlockLDOH;
import net.smileycorp.ldoh.common.item.ItemBlockTooltip;
import net.smileycorp.ldoh.common.item.ItemTurret;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.tile.TileBarbedWire;
import net.smileycorp.ldoh.common.tile.TileHordeSpawner;
import net.smileycorp.ldoh.common.tile.TileLandmine;
import net.smileycorp.ldoh.common.tile.TileTurret;
import net.smileycorp.ldoh.common.world.ModWorldGen;

import com.Fishmod.mod_LavaCow.init.FishItems;

import de.maxhenkel.car.items.ModItems;

@EventBusSubscriber(modid = ModDefinitions.MODID)
public class RegistryEvents {

	public static final Set<Item> ITEMS = new HashSet<Item>();
	public static final Set<Block> BLOCKS = new HashSet<Block>();

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		for (Field field : LDOHItems.class.getDeclaredFields()) {
			try {
				Object item = field.get(null);
				if (item instanceof Item) {
					registerItem(registry, (Item) item);
				}
			} catch (Exception e) {}
		}
		registerItem(registry, new ItemBarbedWire());
		registerItem(registry, new ItemBlockTooltip(LDOHBlocks.LANDMINE, 2));
		registerItem(registry, new ItemTurret());
		registerItem(registry, new ItemBlockLDOH(LDOHBlocks.HORDE_SPAWNER));
	}

	private static void registerItem(IForgeRegistry<Item> registry, Item item) {
		registry.register(item);
		ITEMS.add(item);
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		for (Field field : LDOHBlocks.class.getDeclaredFields()) {
			try {
				Object block = field.get(null);
				if (block instanceof Block) {
					registry.register((Block) block);
					BLOCKS.add((Block) block);
				}
			} catch (Exception e) {}
		}
		GameRegistry.registerTileEntity(TileBarbedWire.class, ModDefinitions.getResource("barbed_wire"));
		GameRegistry.registerTileEntity(TileHordeSpawner.class, ModDefinitions.getResource("horde_spawner"));
		GameRegistry.registerTileEntity(TileLandmine.class, ModDefinitions.getResource("landmine"));
		GameRegistry.registerTileEntity(TileTurret.class, ModDefinitions.getResource("turret"));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		GameRegistry.addSmelting(new ItemStack(LDOHItems.SYRINGE, 1, 3), new ItemStack(LDOHItems.SYRINGE, 1, 0), 0.1f);
		GameRegistry.addSmelting(new ItemStack(Blocks.SOUL_SAND), new ItemStack(Items.QUARTZ, 1, 0), 0.1f);
		FuelHandler.getInstance().registerFuel(ModItems.RAPECAKE, 50);
		OreDictionary.registerOre("fabric", LDOHItems.CLOTH_FABRIC);
		OreDictionary.registerOre("fabric", FishItems.CURSED_FABRIC);
		OreDictionary.registerOre("nuggetDiamond", LDOHItems.DIAMOND_NUGGET);
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		GameRegistry.registerWorldGenerator(new ModWorldGen(), 0);
		int ID = 201;
		IForgeRegistry<EntityEntry> registry = event.getRegistry();
		EntityEntry ZOMBIE_NO_BREAK = EntityEntryBuilder.create().entity(EntityDummyZombie0.class)
				.id(ModDefinitions.getResource("zombie_no_break"), ID++)
				.name(ModDefinitions.getName("ZombieNoBreak")).tracker(80, 3, true).build();
		registry.register(ZOMBIE_NO_BREAK);
		EntityEntry ZOMBIE_NO_PLACE = EntityEntryBuilder.create().entity(EntityDummyZombie1.class)
				.id(ModDefinitions.getResource("zombie_no_place"), ID++)
				.name(ModDefinitions.getName("ZombieNoPlace")).tracker(80, 3, true).build();
		registry.register(ZOMBIE_NO_PLACE);
		EntityEntry ZOMBIE_NO_BREAK_OR_PLACE = EntityEntryBuilder.create().entity(EntityDummyZombie2.class)
				.id(ModDefinitions.getResource("zombie_no_place_or_break"), ID++)
				.name(ModDefinitions.getName("ZombieNoBreakOrPlace")).tracker(80, 3, true).build();
		registry.register(ZOMBIE_NO_BREAK_OR_PLACE);
		EntityEntry HUSK_NO_BREAK = EntityEntryBuilder.create().entity(EntityDummyHusk0.class)
				.id(ModDefinitions.getResource("husk_no_break"), ID++)
				.name(ModDefinitions.getName("HuskNoBreak")).tracker(80, 3, true).build();
		registry.register(HUSK_NO_BREAK);
		EntityEntry HUSK_NO_PLACE = EntityEntryBuilder.create().entity(EntityDummyHusk1.class)
				.id(ModDefinitions.getResource("husk_no_place"), ID++)
				.name(ModDefinitions.getName("HuskNoPlace")).tracker(80, 3, true).build();
		registry.register(HUSK_NO_PLACE);
		EntityEntry HUSK_NO_BREAK_OR_PLACE = EntityEntryBuilder.create().entity(EntityDummyHusk2.class)
				.id(ModDefinitions.getResource("husk_no_place_or_break"), ID++)
				.name(ModDefinitions.getName("HuskNoBreakOrPlace")).tracker(80, 3, true).build();
		registry.register(HUSK_NO_BREAK_OR_PLACE);
		EntityEntry CRAWLING_ZOMBIE = EntityEntryBuilder.create().entity(EntityCrawlingZombie.class)
				.id(ModDefinitions.getResource("crawling_zombie"), ID++)
				.name(ModDefinitions.getName("CrawlingZombie")).tracker(80, 3, true).build();
		registry.register(CRAWLING_ZOMBIE);
		EntityEntry CRAWLING_HUSK = EntityEntryBuilder.create().entity(EntityCrawlingHusk.class)
				.id(ModDefinitions.getResource("crawling_husk"), ID++)
				.name(ModDefinitions.getName("CrawlingHusk")).tracker(80, 3, true).build();
		registry.register(CRAWLING_HUSK);
		EntityEntry TF_ZOMBIE = EntityEntryBuilder.create().entity(EntityTF2Zombie.class)
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
		EntityEntry TURRET = EntityEntryBuilder.create().entity(EntityTurret.class)
				.id(ModDefinitions.getResource("turret"), ID++)
				.name(ModDefinitions.getName("Turret")).tracker(80, 3, true).build();
		registry.register(TURRET);
		EntityEntry INCENDIARY_PROJECTILE = EntityEntryBuilder.create().entity(EntityIncendiaryProjectile.class)
				.id(ModDefinitions.getResource("incendiary_projectile"), ID++)
				.name(ModDefinitions.getName("IncendiaryProjectile")).tracker(64, 80, true).build();
		registry.register(INCENDIARY_PROJECTILE);
		EntityEntry ZOMBIE_FIREMAN = EntityEntryBuilder.create().entity(EntityZombieFireman.class)
				.id(ModDefinitions.getResource("zombie_fireman"), ID++)
				.name(ModDefinitions.getName("ZombieFireman")).tracker(80, 3, true).build();
		registry.register(ZOMBIE_FIREMAN);
	}

}
