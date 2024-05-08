package net.smileycorp.ldoh.common.entity.zombie;

import com.mrcrayfish.guns.init.ModGuns;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.item.LDOHItems;

public class EntitySwatZombie extends EntityProfessionZombie {

    private static final ItemStack MACHINE_PISTOL = createGun();
    private static final ItemStack SHOTGUN = createShotgun();
    private static final ItemStack PISTOL = createPistol();

    protected static final DataParameter<ItemStack> BACK_ITEM = EntityDataManager.createKey(EntitySwatZombie.class, DataSerializers.ITEM_STACK);
    protected static final DataParameter<ItemStack> HOLSTER_ITEM = EntityDataManager.createKey(EntitySwatZombie.class, DataSerializers.ITEM_STACK);

    public EntitySwatZombie(World world) {
        super(world);
    }

    @Override
    protected void setEquipment() {
        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, MACHINE_PISTOL);
        setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(LDOHItems.GAS_MASK));
    }

    @Override
    protected String getStage() {
        return "turret_stage";
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(BACK_ITEM, SHOTGUN);
        dataManager.register(HOLSTER_ITEM, PISTOL);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
    }

    public ItemStack getBackItem() {
        return dataManager.get(BACK_ITEM);
    }

    public ItemStack getHolsterItem() {
        return dataManager.get(HOLSTER_ITEM);
    }

    public void setBackItem(ItemStack stack) {
        dataManager.set(BACK_ITEM, stack);
    }

    public void setHolsterItem(ItemStack stack) {
        dataManager.set(HOLSTER_ITEM, stack);
    }

    private static ItemStack createGun() {
        ItemStack gun = new ItemStack(ModGuns.MACHINE_PISTOL);
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagCompound attachments = new NBTTagCompound();
        NBTTagCompound barrel = new NBTTagCompound();
        barrel.setString("id", "cgm:silencer");
        barrel.setByte("Count", (byte) 1);
        barrel.setShort("Damage", (short) 0);
        attachments.setTag("barrel", barrel);
        NBTTagCompound scope = new NBTTagCompound();
        scope.setString("id", "cgm:scope");
        scope.setByte("Count", (byte) 1);
        scope.setShort("Damage", (short) 1);
        attachments.setTag("scope", scope);
        nbt.setTag("attachments", attachments);
        nbt.setInteger("color", 1908001);
        gun.setTagCompound(nbt);
        return gun;
    }

    private static ItemStack createShotgun() {
        ItemStack gun = new ItemStack(ModGuns.SHOTGUN);
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagCompound attachments = new NBTTagCompound();
        NBTTagCompound scope = new NBTTagCompound();
        scope.setString("id", "cgm:scope");
        scope.setByte("Count", (byte) 1);
        scope.setShort("Damage", (short) 0);
        attachments.setTag("scope", scope);
        nbt.setTag("attachments", attachments);
        nbt.setInteger("color", 1908001);
        gun.setTagCompound(nbt);
        return gun;
    }

    private static ItemStack createPistol() {
        ItemStack gun = new ItemStack(ModGuns.PISTOL);
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagCompound attachments = new NBTTagCompound();
        NBTTagCompound barrel = new NBTTagCompound();
        barrel.setString("id", "cgm:silencer");
        barrel.setByte("Count", (byte) 1);
        barrel.setShort("Damage", (short) 0);
        attachments.setTag("barrel", barrel);
        nbt.setTag("attachments", attachments);
        nbt.setInteger("color", 1908001);
        gun.setTagCompound(nbt);
        return gun;
    }

}
