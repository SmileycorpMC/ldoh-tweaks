package net.smileycorp.ldoh.common.entity.zombie;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.item.ItemSpawner;

public class EntityReaver extends EntityZombie {
    
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntityReaver.class, DataSerializers.BYTE);
    
    public EntityReaver(World world) {
        super(world);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(CLIMBING, (byte)0);
        stepHeight = 1f;
    }
    
    @Override
    protected PathNavigate createNavigator(World world)
    {
        return new PathNavigateClimber(this, world);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
        getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
    }
    
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) setBesideClimbableBlock(collidedHorizontally);
    }
    
    public boolean isOnLadder()
    {
        return isBesideClimbableBlock();
    }
    
    public boolean isBesideClimbableBlock() {
        return (dataManager.get(CLIMBING).byteValue() & 1) != 0;
    }
    
    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = dataManager.get(CLIMBING).byteValue();
        b0 = (byte) (climbing ? b0 | 1 : b0 & -2);
        dataManager.set(CLIMBING, Byte.valueOf(b0));
    }
    
    @Override
    public boolean shouldBurnInDay() {
        return false;
    }
    
    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return ItemSpawner.getEggFor(this);
    }
    
}
