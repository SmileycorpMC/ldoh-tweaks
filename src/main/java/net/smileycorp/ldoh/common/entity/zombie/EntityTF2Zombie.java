package net.smileycorp.ldoh.common.entity.zombie;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.item.ItemSpawner;
import net.smileycorp.ldoh.common.util.ModUtils;
import rafradek.TF2weapons.TF2weapons;
import rafradek.TF2weapons.common.WeaponsCapability;
import rafradek.TF2weapons.entity.mercenary.EntityTF2Character;
import rafradek.TF2weapons.util.TF2Class;

import javax.annotation.Nullable;

public class EntityTF2Zombie extends EntityZombie {

    protected static final DataParameter<Byte> TF_TEAM = EntityDataManager.createKey(EntityTF2Zombie.class, DataSerializers.BYTE);
    protected static final DataParameter<String> TF_CLASS = EntityDataManager.createKey(EntityTF2Zombie.class, DataSerializers.STRING);

    protected Team tfteam;
    protected TF2Class tfclass;
    protected EntityTF2Character baseentity;
    private WeaponsCapability weaponCap;

    public EntityTF2Zombie(World world) {
        super(world);
        setRandomProperties();
    }

    public EntityTF2Zombie(EntityTF2Character entity) {
        super(entity.world);
        buildPropertiesFrom(entity);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(TF_TEAM, Byte.valueOf((byte) 0));
        dataManager.register(TF_CLASS, "demoman");
        weaponCap = new WeaponsCapability(this);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
    }

    public void buildPropertiesFrom(EntityTF2Character entity) {
        TF2Class tfclass = entity.getTF2Class();
        setTFClass(tfclass);
        if (entity.getTeam() != null) {
            setTFTeam(entity.getTeam());
        } else setRandomTeam();
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            setItemStackToSlot(slot, ModUtils.cleanTFWeapon(entity.getItemStackFromSlot(slot)));
        }
        posX = entity.posX;
        posY = entity.posY;
        posZ = entity.posZ;
        renderYawOffset = entity.renderYawOffset;
        if (entity.hasCustomName()) {
            setCustomNameTag(entity.getCustomNameTag());
        }
        baseentity = entity;
        return;
    }

    public void setTFTeam(Team tfteam) {
        if (tfteam == world.getScoreboard().getTeam("BLU") || tfteam == world.getScoreboard().getTeam("RED")) {
            this.tfteam = tfteam;
            dataManager.set(TF_TEAM, (byte) getTeamKey(tfteam));
        } else setRandomTeam();
    }

    public Team getTFTeam() {
        return getTeam(dataManager.get(TF_TEAM).intValue());
    }

    public void setTFClass(TF2Class tfclass) {
        this.tfclass = tfclass;
        dataManager.set(TF_CLASS, tfclass.name());
    }

    public TF2Class getTFClass() {
        return TF2Class.valueOf(dataManager.get(TF_CLASS));
    }

    private void setRandomProperties() {
        setRandomTeam();
        setRandomClass();
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        if (baseentity == null) {
            try {
                baseentity = tfclass.createEntity(world);
                baseentity.onInitialSpawn(world.getDifficultyForLocation(getPosition()), null);
                getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(baseentity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
                getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(baseentity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue());
                getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(baseentity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getBaseValue());
                setHealth(getMaxHealth());
                heal(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (baseentity != null) {
            for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                setItemStackToSlot(slot, ModUtils.cleanTFWeapon(baseentity.getItemStackFromSlot(slot)));
            }
        }
    }

    @Override
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
            ItemStack itemstack = getItemStackFromSlot(entityequipmentslot);
            if (!itemstack.isEmpty() && rand.nextFloat() - lootingModifier * 0.01f < 0.15f)
                entityDropItem(itemstack, 0.0F);
        }
    }

    private void setRandomTeam() {
        setTFTeam(getTeam(rand.nextInt(2)));
    }

    private Team getTeam(int key) {
        return key == 0 ? world.getScoreboard().getTeam("BLU") : world.getScoreboard().getTeam("RED");
    }

    private int getTeamKey(Team team) {
        return team == world.getScoreboard().getTeam("RED") ? 1 : 0;
    }

    private void setRandomClass() {
        setTFClass(TF2Class.getRandomClass());
    }

    public EntityTF2Character getCured() {
        EntityTF2Character entity;
        try {
            entity = getTFClass().createEntity(world);
            world.getScoreboard().addPlayerToTeam(entity.getCachedUniqueIdString(), getTFTeam().getName());
            entity.posX = posX;
            entity.posY = posY;
            entity.posZ = posZ;
            entity.renderYawOffset = renderYawOffset;
            for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                entity.setItemStackToSlot(slot, ModUtils.cleanTFWeapon(getItemStackFromSlot(slot)));
            }
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean shouldBurnInDay() {
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("tfteam", getTeamKey(tfteam));
        compound.setString("tfclass", tfclass.name());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("tfteam")) {
            setTFTeam(getTeam(compound.getInteger("tfteam")));
        }
        if (compound.hasKey("tfclass")) {
            setTFClass(TF2Class.valueOf(compound.getString("tfclass")));
        }

    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == TF2weapons.WEAPONS_CAP || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == TF2weapons.WEAPONS_CAP) {
            return (T) weaponCap;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public String getName() {
        try {
            if (hasCustomName()) return getCustomNameTag();
            return new TextComponentTranslation("entity.Zombie.name").appendSibling(new TextComponentString(" ")).appendSibling(getTFClass().getLocalizedName()).getFormattedText();
        } catch (Exception e) {
            return new TextComponentTranslation("entity." + ModDefinitions.MODID + ".TFZombie.name").getFormattedText();
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return ItemSpawner.getEggFor(this);
    }

}
