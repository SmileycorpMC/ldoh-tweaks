package net.smileycorp.ldoh.common.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.ldoh.common.LDOHTweaks;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.ModMobEntry;
import net.smileycorp.ldoh.common.entity.infphoenix.*;
import net.smileycorp.ldoh.common.entity.zombie.*;
import rafradek.TF2weapons.util.TF2Class;

import java.util.ArrayList;
import java.util.List;

public class ItemSpawner extends Item implements IMetaItem {

    String name = "MobSpawner";

    public static List<ModMobEntry> entries = new ArrayList<ModMobEntry>();

    public ItemSpawner() {
        setCreativeTab(LDOHTweaks.CREATIVE_TAB);
        setUnlocalizedName(ModDefinitions.getName(name));
        setRegistryName(ModDefinitions.getResource(name));
        setHasSubtypes(true);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new BehaviorDefaultDispenseItem() {
            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                double x = source.getX() + enumfacing.getFrontOffsetX();
                double y = source.getBlockPos().getY() + enumfacing.getFrontOffsetY() + 0.2F;
                double z = source.getZ() + enumfacing.getFrontOffsetZ();
                spawnEntity(source.getWorld(), stack.getMetadata(), new BlockPos(x, y, z));
                stack.shrink(1);
                return stack;
            }
        });
    }

    @Override
    public String byMeta(int meta) {
        return "normal";
    }

    @Override
    public int getMaxMeta() {
        return entries.size();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (int i = 0; i < getMaxMeta(); i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        int meta = stack.getMetadata();
        if (meta >= getMaxMeta()) meta = 0;
        return super.getItemStackDisplayName(stack) + " " + entries.get(meta).getLocalisedName();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (world.isRemote) return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        ItemStack stack = player.getHeldItem(hand);
        int meta = stack.getMetadata();
        if (meta >= getMaxMeta()) meta = 0;
        RayTraceResult raytrace = rayTrace(world, player, true);
        if (raytrace != null) {
            if (raytrace.getBlockPos() != null) {
                BlockPos pos = raytrace.getBlockPos().offset(raytrace.sideHit);
                spawnEntity(world, meta, pos);
                if (!player.capabilities.isCreativeMode) stack.shrink(1);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }

    private EntityLiving spawnEntity(World world, int meta, BlockPos pos) {
        EntityLiving entity = entries.get(meta).getEntityToSpawn(world, pos);
        entity.onInitialSpawn(world.getDifficultyForLocation(pos), null);
        entity.setPosition(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
        world.spawnEntity(entity);
        return entity;
    }

    static {
        for (TF2Class tfclass : TF2Class.getClasses()) {
            entries.add(new ModMobEntry(tfclass, 0x436C34, 0xEF0000, "RED"));
            entries.add(new ModMobEntry(tfclass, 0x436C34, 0x0000E2, "BLU"));
        }
        entries.add(new ModMobEntry(EntityCrawlingZombie.class, "entity.ldoh.CrawlingZombie.name", 0x436C34, 0xBA8644));
        entries.add(new ModMobEntry(EntityCrawlingHusk.class, "entity.ldoh.CrawlingHusk.name", 0xA5926A, 0xBA8644));
        entries.add(new ModMobEntry(EntityTF2Zombie.class, "entity.ldoh.TFZombie.name", 0x0000E2, 0xEF0000));
        entries.add(new ModMobEntry(EntityZombieNurse.class, "entity.ldoh.NurseZombie.name", 0x436C34, 0xB5ABB4));
        entries.add(new ModMobEntry(EntitySwatZombie.class, "entity.ldoh.SwatZombie.name", 0x436C34, 0x0C0C0D));
        entries.add(new ModMobEntry(EntityZombieMechanic.class, "entity.ldoh.ZombieMechanic.name", 0x436C34, 0x394A6B));
        entries.add(new ModMobEntry(EntityZombieTechnician.class, "entity.ldoh.ZombieTechnician.name", 0x436C34, 0xD4EB5C));
        entries.add(new ModMobEntry(EntityZombieFireman.class, "entity.ldoh.ZombieFireman.name", 0x436C34, 0x20263B));
        entries.add(new ModMobEntry(EntityInfPhoenixCloud.class, "entity.ldoh.InfCloudPhoenix.name", 0x2A0101, 16121598));
        entries.add(new ModMobEntry(EntityInfPhoenixDesert.class, "entity.ldoh.InfDesertPhoenix.name", 0x2A0101, 16767917));
        entries.add(new ModMobEntry(EntityInfPhoenixEnder.class, "entity.ldoh.InfEnderPhoenix.name", 0x2A0101, 3154227));
        entries.add(new ModMobEntry(EntityInfPhoenixFire.class, "entity.ldoh.InfFirePhoenix.name", 0x2A0101, 16095278));
        entries.add(new ModMobEntry(EntityInfPhoenixNether.class, "entity.ldoh.InfNetherPhoenix.name", 0x2A0101, 5111808));
        entries.add(new ModMobEntry(EntityInfPhoenixSnow.class, "entity.ldoh.InfSnowPhoenix.name", 0x2A0101, 16777215));
        entries.add(new ModMobEntry(EntityInfPhoenixTwilight.class, "entity.ldoh.InfTwilightPhoenix.name", 0x2A0101, 395019));
        entries.add(new ModMobEntry(EntityInfPhoenixWater.class, "entity.ldoh.InfWaterPhoenix.name", 0x2A0101, 7253246));
        entries.add(new ModMobEntry(EntityReaver.class, "entity.ldoh.Reaver.name", 0x87727E, 0xC4BCBA));
        try {
            NBTTagCompound libraryNBT = JsonToNBT.getTagFromJson("{PersistenceRequired:1b, "
                    + "DeathLootTable:\"" + ModDefinitions.getResource("entities/library_zombie") + "\"}");
            entries.add(new ModMobEntry(EntityReaver.class, "entity.ldoh.LibraryZombie.name", 0x87727E, 0xC4BCBA, libraryNBT));
            NBTTagCompound hospitalNBT = JsonToNBT.getTagFromJson("{PersistenceRequired:1b, "
                    + "DeathLootTable:\"" + ModDefinitions.getResource("entities/hospital_zombie") + "\"}");
            entries.add(new ModMobEntry(EntityReaver.class, "entity.ldoh.HospitalZombie.name", 0x87727E, 0xC4BCBA, hospitalNBT));
        } catch (NBTException e) {
            e.printStackTrace();
        }
        //entries.add(new ModMobEntry(EntityJuggernaut.class, "entity.ldoh.Juggernaut.name", 0x8A4411, 0x333736));
    }

    public static ItemStack getEggFor(EntityLiving entity) {
        for (ModMobEntry entry : entries) {
            if (entry.doesMatch(entity)) {
                return new ItemStack(LDOHItems.SPAWNER, 1, entries.indexOf(entry));
            }
        }
        return null;
    }

}
