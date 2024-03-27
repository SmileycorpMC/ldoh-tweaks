package net.smileycorp.ldoh.common.events;

import biomesoplenty.api.block.BOPBlocks;
import com.Fishmod.mod_LavaCow.init.FishItems;
import com.animania.addons.extra.common.handler.ExtraAddonItemHandler;
import com.animania.addons.farm.common.handler.FarmAddonItemHandler;
import com.mrcrayfish.furniture.api.IRecipeRegistry;
import com.mrcrayfish.furniture.api.RecipeType;
import com.mrcrayfish.furniture.api.RecipeVariables;
import de.maxhenkel.car.items.ModItems;
import ichttt.mods.firstaid.common.items.FirstAidItems;
import mariot7.xlfoodmod.init.ItemListxlfoodmod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.FuelHandler;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.block.LDOHBlocks;
import net.smileycorp.ldoh.common.entity.EntityJuggernaut;
import net.smileycorp.ldoh.common.entity.EntityTurret;
import net.smileycorp.ldoh.common.entity.infphoenix.*;
import net.smileycorp.ldoh.common.entity.zombie.*;
import net.smileycorp.ldoh.common.fluid.LDOHFluids;
import net.smileycorp.ldoh.common.item.*;
import net.smileycorp.ldoh.common.recipes.BottlingRecipe;
import net.smileycorp.ldoh.common.recipes.LDOHRecipeRegistry;
import net.smileycorp.ldoh.common.recipes.MixingRecipe;
import net.smileycorp.ldoh.common.recipes.SiphoningRecipe;
import net.smileycorp.ldoh.common.tile.*;
import net.smileycorp.ldoh.common.world.ModWorldGen;
import pavocado.exoticbirds.init.ExoticbirdsItems;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(modid = Constants.MODID)
public class RegistryEvents {

    public static final Set<Item> ITEMS = new HashSet<>();
    public static final Set<Block> BLOCKS = new HashSet<>();
    public static final Set<BlockFluidClassic> FLUID_BLOCKS = new HashSet<>();
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for (Field field : LDOHItems.class.getDeclaredFields()) {
            try {
                Object item = field.get(null);
                if (item instanceof Item) {
                    registerItem(registry, (Item) item);
                }
            } catch (Exception e) {
            }
        }
        registerItem(registry, new ItemBarbedWire());
        registerItem(registry, new ItemBlockTooltip(LDOHBlocks.LANDMINE, 2));
        registerItem(registry, new ItemTurret());
        registerItem(registry, new ItemBlockTooltip(LDOHBlocks.FILING_CABINET));
        registerItem(registry, new ItemBlockLDOH(LDOHBlocks.HORDE_SPAWNER));
        //registerItem(registry, new ItemBlockMeta(LDOHBlocks.LABORATORY_BLOCK));
    }

    public static void registerItem(IForgeRegistry<Item> registry, Item item) {
        registry.register(item);
        ITEMS.add(item);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        for (Field field : LDOHBlocks.class.getDeclaredFields()) {
            try {
                Block block = (Block) field.get(null);
                registry.register(block);
                BLOCKS.add(block);
            } catch (Exception e) {
                System.err.println(field);
                e.printStackTrace();
            }
        }
        FluidRegistry.enableUniversalBucket();
        for (Field field : LDOHFluids.class.getDeclaredFields()) {
            try {
                Fluid fluid = (Fluid) field.get(null);
                FluidRegistry.registerFluid(fluid);
                BlockFluidClassic block = new BlockFluidClassic(fluid, Material.WATER);
                block.setRegistryName(fluid.getName());
                block.setUnlocalizedName(fluid.getUnlocalizedName().substring(4));
                registry.register(block);
                fluid.setBlock(block);
                FLUID_BLOCKS.add(block);
                if (fluid != LDOHFluids.EXPERIENCE) FluidRegistry.addBucketForFluid(fluid);
            } catch (Exception e) {
                System.err.println(field);
                e.printStackTrace();
            }
        }
        GameRegistry.registerTileEntity(TileBarbedWire.class, Constants.loc("barbed_wire"));
        GameRegistry.registerTileEntity(TileHordeSpawner.class, Constants.loc("horde_spawner"));
        GameRegistry.registerTileEntity(TileLandmine.class, Constants.loc("landmine"));
        GameRegistry.registerTileEntity(TileTurret.class, Constants.loc("turret"));
        GameRegistry.registerTileEntity(TileFilingCabinet.class, Constants.loc("filing_cabinet"));
    }
    
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        for (ItemStack egg : OreDictionary.getOres("egg"))
            GameRegistry.addSmelting(egg, new ItemStack(ItemListxlfoodmod.fried_egg), 0.1f);
        GameRegistry.addSmelting(new ItemStack(LDOHItems.SYRINGE, 1, 3), new ItemStack(LDOHItems.SYRINGE, 1, 0), 0.1f);
        GameRegistry.addSmelting(new ItemStack(Blocks.SOUL_SAND), new ItemStack(Items.QUARTZ, 1, 0), 0.1f);
        FuelHandler.getInstance().registerFuel(ModItems.RAPECAKE, 50);
        OreDictionary.registerOre("fabric", LDOHItems.CLOTH_FABRIC);
        OreDictionary.registerOre("fabric", FishItems.CURSED_FABRIC);
        OreDictionary.registerOre("nuggetDiamond", LDOHItems.DIAMOND_NUGGET);
        LDOHRecipeRegistry.registerMixingRecipe(new MixingRecipe(new FluidStack(LDOHFluids.ENRICHED_ANTIBODY_SERUM, 250),
                new FluidStack(BOPBlocks.blood_fluid, 125), new FluidStack(LDOHFluids.NECROTIC_BLOOD, 125), new FluidStack(LDOHFluids.MORPHINE, 125)));
        LDOHRecipeRegistry.registerBottlingRecipe(new BottlingRecipe(new ItemStack(Items.POTIONITEM), new FluidStack(FluidRegistry.WATER, 125), new ItemStack(Items.GLASS_BOTTLE)));
        LDOHRecipeRegistry.registerBottlingRecipe(new BottlingRecipe(new ItemStack(Items.EXPERIENCE_BOTTLE), new FluidStack(LDOHFluids.EXPERIENCE, 125), new ItemStack(Items.GLASS_BOTTLE)));
        LDOHRecipeRegistry.registerBottlingRecipe(new BottlingRecipe(new ItemStack(FirstAidItems.MORPHINE), new FluidStack(LDOHFluids.MORPHINE, 125), new ItemStack(Items.GLASS_BOTTLE)));
        LDOHRecipeRegistry.registerBottlingRecipe(new BottlingRecipe(new ItemStack(LDOHItems.SYRINGE, 1, 1), new FluidStack(BOPBlocks.blood_fluid, 125), new ItemStack(LDOHItems.SYRINGE, 1, 0)));
        LDOHRecipeRegistry.registerBottlingRecipe(new BottlingRecipe(new ItemStack(LDOHItems.SYRINGE, 1, 2), new FluidStack(LDOHFluids.ENRICHED_ANTIBODY_SERUM, 125), new ItemStack(LDOHItems.SYRINGE, 1, 0)));
        LDOHRecipeRegistry.registerSiphoningRecipe(new SiphoningRecipe(new FluidStack(LDOHFluids.EXPERIENCE, 125), new ItemStack(Items.GLASS_BOTTLE), new ItemStack(Items.EXPERIENCE_BOTTLE)));
        LDOHRecipeRegistry.registerSiphoningRecipe(new SiphoningRecipe(new FluidStack(LDOHFluids.MORPHINE, 125), new ItemStack(Items.GLASS_BOTTLE), new ItemStack(FirstAidItems.MORPHINE)));
        LDOHRecipeRegistry.registerSiphoningRecipe(new SiphoningRecipe(new FluidStack(BOPBlocks.blood_fluid, 125), new ItemStack(LDOHItems.SYRINGE, 1, 3), new ItemStack(LDOHItems.SYRINGE, 1, 1)));
        LDOHRecipeRegistry.registerSiphoningRecipe(new SiphoningRecipe(new FluidStack(LDOHFluids.ENRICHED_ANTIBODY_SERUM, 125), new ItemStack(LDOHItems.SYRINGE, 1, 3), new ItemStack(LDOHItems.SYRINGE, 1, 2)));
        LDOHRecipeRegistry.registerSiphoningRecipe(new SiphoningRecipe(new FluidStack(LDOHFluids.NECROTIC_BLOOD, 25), ItemStack.EMPTY, new ItemStack(Items.ROTTEN_FLESH)));
    }

    public static void registerCFMRecipes(IRecipeRegistry registry) {
        //register washing machine and dishwasher recipes
        for (Item item : ForgeRegistries.ITEMS) {
            if (item.getRegistryName().getResourceDomain().equals("minecraft") | !item.isRepairable()) continue;
            if (item.getEquipmentSlot(new ItemStack(item)) != null)
                registry.registerRecipe(RecipeType.WASHING_MACHINE, new RecipeVariables().setInput(new ItemStack(item)));
            else registry.registerRecipe(RecipeType.DISHWASHER, new RecipeVariables().setInput(new ItemStack(item)));
        }
        //register freezer recipes
        registry.registerRecipe(RecipeType.FREEZER, new RecipeVariables().setInput(new ItemStack(Blocks.PACKED_ICE))
                .setOutput(new ItemStack(BOPBlocks.hard_ice)));
        registry.registerRecipe(RecipeType.FREEZER, new RecipeVariables().setInput(new ItemStack(Items.FLINT))
                .setOutput(new ItemStack(Items.PRISMARINE_SHARD)));
        //register grill recipes
        registry.registerRecipe(RecipeType.GRILL, new RecipeVariables().setInput(new ItemStack(Items.PORKCHOP))
                .setOutput(new ItemStack(Items.COOKED_PORKCHOP)));
        registry.registerRecipe(RecipeType.GRILL, new RecipeVariables().setInput(new ItemStack(ExtraAddonItemHandler.rawFrogLegs))
                .setOutput(new ItemStack(ExtraAddonItemHandler.cookedFrogLegs)));
        registry.registerRecipe(RecipeType.GRILL, new RecipeVariables().setInput(new ItemStack(FarmAddonItemHandler.rawPrimeBacon))
                .setOutput(new ItemStack(FarmAddonItemHandler.cookedPrimeBacon)));
        registry.registerRecipe(RecipeType.GRILL, new RecipeVariables().setInput(new ItemStack(FarmAddonItemHandler.rawPrimeBeef))
                .setOutput(new ItemStack(FarmAddonItemHandler.cookedPrimeBeef)));
        registry.registerRecipe(RecipeType.GRILL, new RecipeVariables().setInput(new ItemStack(FarmAddonItemHandler.rawPrimeSteak))
                .setOutput(new ItemStack(FarmAddonItemHandler.cookedPrimeSteak)));
        registry.registerRecipe(RecipeType.GRILL, new RecipeVariables().setInput(new ItemStack(FarmAddonItemHandler.rawPrimePork))
                .setOutput(new ItemStack(FarmAddonItemHandler.cookedPrimePork)));
        //register oven recipes
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(Items.CHORUS_FRUIT))
                .setOutput(new ItemStack(Items.CHORUS_FRUIT_POPPED)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(ExtraAddonItemHandler.rawFrogLegs))
                .setOutput(new ItemStack(ExtraAddonItemHandler.cookedFrogLegs)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(ExtraAddonItemHandler.rawPeacock))
                .setOutput(new ItemStack(ExtraAddonItemHandler.cookedPeacock)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(ExtraAddonItemHandler.rawPrimePeacock))
                .setOutput(new ItemStack(ExtraAddonItemHandler.cookedPrimePeacock)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(ExtraAddonItemHandler.rawPrimeRabbit))
                .setOutput(new ItemStack(ExtraAddonItemHandler.cookedPrimeRabbit)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FarmAddonItemHandler.rawPrimeBacon))
                .setOutput(new ItemStack(FarmAddonItemHandler.cookedPrimeBacon)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FarmAddonItemHandler.rawPrimeBeef))
                .setOutput(new ItemStack(FarmAddonItemHandler.cookedPrimeBeef)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FarmAddonItemHandler.rawPrimeSteak))
                .setOutput(new ItemStack(FarmAddonItemHandler.cookedPrimeSteak)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FarmAddonItemHandler.rawPrimePork))
                .setOutput(new ItemStack(FarmAddonItemHandler.cookedPrimePork)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FarmAddonItemHandler.rawPrimeMutton))
                .setOutput(new ItemStack(FarmAddonItemHandler.cookedPrimeMutton)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FarmAddonItemHandler.rawHorse))
                .setOutput(new ItemStack(FarmAddonItemHandler.cookedHorse)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FishItems.PARASITE_ITEM))
                .setOutput(new ItemStack(FishItems.PARASITE_ITEM_COOKED)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FishItems.MOUSSE))
                .setOutput(new ItemStack(FishItems.MEATBALL)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FishItems.ZOMBIEPIRANHA_ITEM))
                .setOutput(new ItemStack(FishItems.ZOMBIEPIRANHA_ITEM_COOKED)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FishItems.PIRANHA))
                .setOutput(new ItemStack(FishItems.PIRANHA_COOKED)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FishItems.CHEIROLEPIS))
                .setOutput(new ItemStack(FishItems.CHEIROLEPIS_COOKED)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FishItems.MIMIC_CLAW))
                .setOutput(new ItemStack(FishItems.MIMIC_CLAW_COOKED)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FishItems.PTERA_WING, 1, OreDictionary.WILDCARD_VALUE))
                .setOutput(new ItemStack(FishItems.PTERA_WING_COOKED)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FishItems.ENIGMOTH_LARVA_ITEM))
                .setOutput(new ItemStack(FishItems.ENIGMOTH_LARVA_ITEM_COOKED)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(FishItems.FROZENTHIGH))
                .setOutput(new ItemStack(Items.ROTTEN_FLESH, 4)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(ItemListxlfoodmod.cheese))
                .setOutput(new ItemStack(ItemListxlfoodmod.cheese_puff)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(ItemListxlfoodmod.marshmallow))
                .setOutput(new ItemStack(ItemListxlfoodmod.roasted_marshmallow)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(ItemListxlfoodmod.dough))
                .setOutput(new ItemStack(ItemListxlfoodmod.cooked_dough)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(ItemListxlfoodmod.raw_chicken_wing))
                .setOutput(new ItemStack(ItemListxlfoodmod.cooked_chicken_wing)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(ItemListxlfoodmod.onion))
                .setOutput(new ItemStack(ItemListxlfoodmod.onion_rings)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(ItemListxlfoodmod.rice))
                .setOutput(new ItemStack(ItemListxlfoodmod.fried_rice)));
        registry.registerRecipe(RecipeType.OVEN, new RecipeVariables().setInput(new ItemStack(ExoticbirdsItems.birdmeat))
                .setOutput(new ItemStack(ExoticbirdsItems.cooked_birdmeat)));
        registry.registerRecipe(RecipeType.MINEBAY, new RecipeVariables().addValue("currency", new ItemStack(Items.IRON_INGOT))
                .addValue("price", 8).setInput(new ItemStack(LDOHItems.EXPERIENCE_BUCKET)));
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        GameRegistry.registerWorldGenerator(new ModWorldGen(), 0);
        int ID = 201;
        IForgeRegistry<EntityEntry> registry = event.getRegistry();
        registry.register(EntityEntryBuilder.create().entity(EntityCrawlingZombie.class)
                .id(Constants.loc("crawling_zombie"), ID++)
                .name(Constants.name("CrawlingZombie")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityCrawlingHusk.class)
                .id(Constants.loc("crawling_husk"), ID++)
                .name(Constants.name("CrawlingHusk")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityTF2Zombie.class)
                .id(Constants.loc("tf_zombie"), ID++)
                .name(Constants.name("TFZombie")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityZombieNurse.class)
                .id(Constants.loc("nurse_zombie"), ID++)
                .name(Constants.name("NurseZombie")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntitySwatZombie.class)
                .id(Constants.loc("swat_zombie"), ID++)
                .name(Constants.name("SwatZombie")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityZombieMechanic.class)
                .id(Constants.loc("zombie_mechanic"), ID++)
                .name(Constants.name("ZombieMechanic")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityZombieTechnician.class)
                .id(Constants.loc("zombie_technician"), ID++)
                .name(Constants.name("ZombieTechnician")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityTurret.class)
                .id(Constants.loc("turret"), ID++)
                .name(Constants.name("Turret")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityIncendiaryProjectile.class)
                .id(Constants.loc("incendiary_projectile"), ID++)
                .name(Constants.name("IncendiaryProjectile")).tracker(64, 80, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityZombieFireman.class)
                .id(Constants.loc("zombie_fireman"), ID++)
                .name(Constants.name("ZombieFireman")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityInfPhoenixCloud.class)
                .id(Constants.loc("inf_cloud_phoenix"), ID++)
                .name(Constants.name("InfCloudPhoenix")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityInfPhoenixDesert.class)
                .id(Constants.loc("inf_desert_phoenix"), ID++)
                .name(Constants.name("InfDesertPhoenix")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityInfPhoenixEnder.class)
                .id(Constants.loc("inf_ender_phoenix"), ID++)
                .name(Constants.name("InfEnderPhoenix")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityInfPhoenixFire.class)
                .id(Constants.loc("inf_fire_phoenix"), ID++)
                .name(Constants.name("InfFirePhoenix")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityInfPhoenixNether.class)
                .id(Constants.loc("inf_nether_phoenix"), ID++)
                .name(Constants.name("InfNetherPhoenix")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityInfPhoenixSnow.class)
                .id(Constants.loc("inf_snow_phoenix"), ID++)
                .name(Constants.name("InfSnowPhoenix")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityInfPhoenixTwilight.class)
                .id(Constants.loc("inf_twilight_phoenix"), ID++)
                .name(Constants.name("InfTwilightPhoenix")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityInfPhoenixWater.class)
                .id(Constants.loc("inf_water_phoenix"), ID++)
                .name(Constants.name("InfWaterPhoenix")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityJuggernaut.class)
                .id(Constants.loc("juggernaut"), ID++)
                .name(Constants.name("Juggernaut")).tracker(80, 3, true).build());
        registry.register(EntityEntryBuilder.create().entity(EntityReaver.class)
                .id(Constants.loc("reaver"), ID++)
                .name(Constants.name("Reaver")).tracker(80, 3, true).build());
    }

}
