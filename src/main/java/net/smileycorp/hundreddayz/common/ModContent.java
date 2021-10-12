package net.smileycorp.hundreddayz.common;

import ichttt.mods.firstaid.common.items.FirstAidItems;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.hundreddayz.common.block.BlockBarbedWire;
import net.smileycorp.hundreddayz.common.block.BlockHordeSpawner;
import net.smileycorp.hundreddayz.common.entity.DamageSourceToxicGas;
import net.smileycorp.hundreddayz.common.entity.EntityTFZombie;
import net.smileycorp.hundreddayz.common.entity.EntityZombieNurse;
import net.smileycorp.hundreddayz.common.item.ItemBarbedWire;
import net.smileycorp.hundreddayz.common.item.ItemBase;
import net.smileycorp.hundreddayz.common.item.ItemGasMask;
import net.smileycorp.hundreddayz.common.item.ItemSpawner;
import net.smileycorp.hundreddayz.common.item.ItemSyringe;
import net.smileycorp.hundreddayz.common.tile.TileBarbedWire;
import net.smileycorp.hundreddayz.common.tile.TileHordeSpawner;
import net.smileycorp.hundreddayz.common.world.ModWorldGen;

import org.apache.commons.lang3.ArrayUtils;

import com.Fishmod.mod_LavaCow.init.FishItems;

@EventBusSubscriber(modid = ModDefinitions.modid)
public class ModContent {

	public static CreativeTabs CREATIVE_TAB = new CreativeTabs(ModDefinitions.getName("HundredDayzTab")){
		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(SYRINGE);
		}
	 };
	
	public static Item SPAWNER = new ItemSpawner();
	public static Item CLOTH_FABRIC = new ItemBase("Clothing_Fabric");
	public static Item SYRINGE = new ItemSyringe();
	public static Item GAS_FILTER = new ItemBase("Gas_Filter");
	public static Item GAS_MASK = new ItemGasMask();
	public static Item DIAMOND_NUGGET = new ItemBase("Diamond_Nugget");
	
	public static Block BARBED_WIRE = new BlockBarbedWire();
	public static Block HORDE_SPAWNER = new BlockHordeSpawner();
	
	/*public static Potion BLEED = new PotionBleeding();
	public static DamageSourceBleed BLEED_DAMAGE = new DamageSourceBleed();*/
	public static DamageSourceToxicGas TOXIC_GAS_DAMAGE = new DamageSourceToxicGas();
	
	public static Item[] items = {SPAWNER, CLOTH_FABRIC, SYRINGE, GAS_FILTER, GAS_MASK, DIAMOND_NUGGET};
	public static Block[] blocks = {BARBED_WIRE, HORDE_SPAWNER};
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		registry.registerAll(blocks);
		GameRegistry.registerTileEntity(TileBarbedWire.class, ModDefinitions.getResource("barbed_wire"));
		GameRegistry.registerTileEntity(TileHordeSpawner.class, ModDefinitions.getResource("horde_spawner"));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		GAS_FILTER.setMaxStackSize(1);
		IForgeRegistry<Item> registry = event.getRegistry();
		items = ArrayUtils.add(items, new ItemBarbedWire());
		registry.registerAll(items);
	}
	
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		GameRegistry.registerWorldGenerator(new ModWorldGen(), 0);
		int ID = 201;
		IForgeRegistry<EntityEntry> registry = event.getRegistry();
		EntityEntry TF_ZOMBIE = EntityEntryBuilder.create().entity(EntityTFZombie.class)
				.id(ModDefinitions.getResource("tf_zombie"), ID++)
				.name(ModDefinitions.getName("TFZombie")).tracker(80, 3, true).build();
		registry.register(TF_ZOMBIE);
		EntityEntry NURSE_ZOMBIE = EntityEntryBuilder.create().entity(EntityZombieNurse.class)
				.id(ModDefinitions.getResource("nurse_zombie"), ID++)
				.name(ModDefinitions.getName("NurseZombie")).tracker(80, 3, true).build();
		registry.register(NURSE_ZOMBIE);
	}
	
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		Item ITEM_BARBED_WIRE = Item.getItemFromBlock(BARBED_WIRE);
		//ore dictionary
		OreDictionary.registerOre("fabric", CLOTH_FABRIC);
		OreDictionary.registerOre("fabric", FishItems.CURSED_FABRIC);
		//syringes
		GameRegistry.addShapedRecipe(ModDefinitions.getResource("syringe_1"), ModDefinitions.getResource("syringe"), new ItemStack(SYRINGE, 1, 0), "GGI", 'G', 
				new ItemStack(Blocks.GLASS_PANE), 'I', new ItemStack(Items.IRON_NUGGET));
		GameRegistry.addShapedRecipe(ModDefinitions.getResource("syringe_2"), ModDefinitions.getResource("syringe"), new ItemStack(SYRINGE, 1, 0), "G", "G", "I", 'G', 
				new ItemStack(Blocks.GLASS_PANE), 'I', new ItemStack(Items.IRON_NUGGET));
		GameRegistry.addShapelessRecipe(ModDefinitions.getResource("string"), ModDefinitions.getResource("string"), new ItemStack(Items.STRING, 3), Ingredient.fromItem(CLOTH_FABRIC));
		GameRegistry.addShapelessRecipe(ModDefinitions.getResource("syringe_cure"), ModDefinitions.getResource("syringe_cure"), new ItemStack(SYRINGE, 1, 2), 
				Ingredient.fromStacks(new ItemStack(SYRINGE, 1, 1)), Ingredient.fromItem(FirstAidItems.MORPHINE));
		//gas mask
		GameRegistry.addShapelessRecipe(ModDefinitions.getResource("gas_filter"), ModDefinitions.getResource("gas_filter"), new ItemStack(GAS_FILTER, 1), 
				Ingredient.fromStacks(new ItemStack(Items.COAL, 1, 1)), new OreIngredient("fabric"), Ingredient.fromStacks(new ItemStack(Items.PAPER)), Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)));
		GameRegistry.addShapedRecipe(ModDefinitions.getResource("gas_mask"), ModDefinitions.getResource("gas_mask"), new ItemStack(GAS_MASK), "GCG", "ICI", "FCF", 
				'G', new ItemStack(Blocks.GLASS_PANE), 'I', new ItemStack(Items.IRON_INGOT), 'F', new ItemStack(GAS_FILTER), 'C', new OreIngredient("fabric"));	
		GameRegistry.addShapelessRecipe(ModDefinitions.getResource("gas_mask_repair"), ModDefinitions.getResource("gas_mask"), new ItemStack(GAS_MASK, 1), 
				Ingredient.fromItem(GAS_MASK), Ingredient.fromItem(GAS_FILTER), Ingredient.fromItem(GAS_FILTER));
		//bandages
		GameRegistry.addShapedRecipe(ModDefinitions.getResource("bandage_1"), ModDefinitions.getResource("bandage"), new ItemStack(FirstAidItems.BANDAGE, 1, 0), "CC", "CC", "CC",
				'C', new OreIngredient("fabric"));
		GameRegistry.addShapedRecipe(ModDefinitions.getResource("bandage_2"), ModDefinitions.getResource("bandage"), new ItemStack(FirstAidItems.BANDAGE, 1, 0), "CCC", "CCC",
				'C', new OreIngredient("fabric"));
		GameRegistry.addShapelessRecipe(ModDefinitions.getResource("plaster"), ModDefinitions.getResource("plaster"), new ItemStack(FirstAidItems.PLASTER, 3, 0), new OreIngredient("fabric"), 
				Ingredient.fromItem(Items.SLIME_BALL));
		GameRegistry.addShapedRecipe(ModDefinitions.getResource("iron_barbed_wire"), ModDefinitions.getResource("iron_barbed_wire"), new ItemStack(ITEM_BARBED_WIRE, 4, 0), " M ", "M M", " M ",
				'M', new ItemStack(Items.IRON_INGOT));
		GameRegistry.addShapedRecipe(ModDefinitions.getResource("gold_barbed_wire"), ModDefinitions.getResource("gold_barbed_wire"), new ItemStack(ITEM_BARBED_WIRE, 4, 1), " M ", "M M", " M ",
				'M', new ItemStack(Items.GOLD_INGOT));
		GameRegistry.addShapedRecipe(ModDefinitions.getResource("diamond_barbed_wire"), ModDefinitions.getResource("diamond_barbed_wire"), new ItemStack(ITEM_BARBED_WIRE, 4, 2), " M ", "M M", " M ",
				'M', new ItemStack(Items.DIAMOND));
		GameRegistry.addShapedRecipe(ModDefinitions.getResource("diamond"), ModDefinitions.getResource("diamond"), new ItemStack(Items.DIAMOND), "MMM", "MMM", "MMM",
				'M', new ItemStack(DIAMOND_NUGGET));
		GameRegistry.addShapelessRecipe(ModDefinitions.getResource("diamond_nugget"), ModDefinitions.getResource("diamond_nugget"), new ItemStack(DIAMOND_NUGGET, 9), 
				Ingredient.fromItem(Items.DIAMOND));
	}
}
