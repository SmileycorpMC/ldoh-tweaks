package net.smileycorp.ldoh.common.entity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.item.ItemSpawner;

public class EntityJuggernaut extends EntityMob implements IEnemyMachine {

    private static final DataParameter<Integer> ANGY_TICKS = EntityDataManager.createKey(EntityJuggernaut.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> ROTATE_TICKS = EntityDataManager.createKey(EntityJuggernaut.class, DataSerializers.VARINT);

    public EntityJuggernaut(World world) {
        super(world);
        setSize(3, 3);
    }

    /*public void updateAITasks() {
        super.updateAITasks();
        if (!navigator.noPath()) return;
        if (isAngy()) {
            fire();
            if (world.isRemote) if (ticksExisted % 5 == 0) playSound(new SoundEvent(ModDefinitions.JUGG_ALARM), 0.75f, 0);
            else dataManager.set(ANGY_TICKS, dataManager.get(ANGY_TICKS) - 1);
            return;
        }
        if (dataManager.get(ROTATE_TICKS) > 0) {
            rotationYaw = rotationYaw + 0.0785398163f;
            if (!world.isRemote) dataManager.set(ROTATE_TICKS, dataManager.get(ROTATE_TICKS) - 1);
            return;
        }
        Vec3d offset = getLookVec().scale(2);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(getPosition().add(offset.x, offset.y, offset.z));
        boolean x = DirectionUtils.getFacing(offset).getAxis() == EnumFacing.Axis.X;
        for (int i = 0; i < 3; i++) {
            for (int j = -1; j < 2; j++) {
                BlockPos pos1 = pos.add(x ? j : 0, i, x ? 0 : j);
                IBlockState state = world.getBlockState(pos1);
                if (state.getMaterial() == Material.AIR) continue;
                if (state.getBlockHardness(world, pos1) > -1) dataManager.set(ANGY_TICKS, 60);
                else dataManager.set(ROTATE_TICKS, 20);
                return;
            }
        }
        getNavigator().tryMoveToXYZ(x ? posX : posX + 1, posY, x ? posZ + 1 : posZ, 1);
    }*/

    public void fire() {

    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        if (source.getImmediateSource() instanceof EntityPlayer && ((EntityPlayer) source.getImmediateSource()).isCreative()) return true;
        if (source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer) source.getTrueSource()).isCreative()) return true;
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(ANGY_TICKS, 0);
        dataManager.register(ROTATE_TICKS, 0);
    }

    @Override
    public void applyEntityCollision(Entity entity) {}

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.01);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30);
    }

    @Override
    public float getEyeHeight() {
        return 1.5f;
    }

    /*@Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return ItemSpawner.getEggFor(this);
    }*/

    @Override
    public boolean isEnemy() {
        return true;
    }

    public boolean isAngy() {
        return dataManager.get(ANGY_TICKS) > 0;
    }

}
