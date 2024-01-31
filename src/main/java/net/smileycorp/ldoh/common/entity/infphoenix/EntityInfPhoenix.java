package net.smileycorp.ldoh.common.entity.infphoenix;

import com.dhanantry.scapeandrunparasites.client.particle.SRPEnumParticle;
import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIAttackMeleeStatusAOE;
import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIFlightAttack;
import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIFlightLimits;
import com.dhanantry.scapeandrunparasites.entity.ai.EntityAISwimmingDiving;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityCanMelt;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityCutomAttack;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPInfected;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityLesh;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.dhanantry.scapeandrunparasites.init.SRPSounds;
import com.dhanantry.scapeandrunparasites.util.SRPAttributes;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.ModDefinitions;
import net.smileycorp.ldoh.common.entity.ai.AIMoveRandomFlying;
import net.smileycorp.ldoh.common.entity.ai.FlyingMoveControl;
import net.smileycorp.ldoh.common.item.ItemSpawner;

public abstract class EntityInfPhoenix extends EntityPInfected implements EntityCanMelt, EntityCutomAttack {

    private static final DataParameter<Boolean> MELTING = EntityDataManager.createKey(EntityInfPhoenix.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> HEIGH = EntityDataManager.createKey(EntityInfPhoenix.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> NECK_PHASE = EntityDataManager.createKey(EntityInfPhoenix.class, DataSerializers.VARINT);

    private float aSize = 1;
    private int sound;

    public EntityInfPhoenix(World world) {
        super(world);
        setSize(1.5F, 1.5F);
        moveHelper = new FlyingMoveControl(this);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        tasks.addTask(0, new EntityAISwimmingDiving(this, 0.08));
        tasks.addTask(3, new EntityAIAttackMeleeStatusAOE(this, 1, false, 8.0, 2.0));
        tasks.addTask(3, new EntityAIFlightAttack(this, 64.0));
        tasks.addTask(4, new EntityAIFlightLimits(this, -7, false));
        tasks.addTask(4, new AIMoveRandomFlying(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2f);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(SRPConfig.infectedFollow);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(HEIGH, 0.0F);
        dataManager.register(MELTING, false);
        dataManager.register(NECK_PHASE, 0);
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (!world.isRemote) {
            EntityLivingBase target = getAttackTarget();
            if (target == null && getNeckPhase() > 0) dataManager.set(NECK_PHASE, getNeckPhase() - 1);
            else if (target != null && getNeckPhase() < 10) dataManager.set(NECK_PHASE, getNeckPhase() + 1);
        }
    }

    public int getNeckPhase() {
        return dataManager.get(NECK_PHASE);
    }

    @Override
    public void melt() {
        setWait(1000);
        dataManager.set(HEIGH, 1.4F);
        dataManager.set(MELTING, true);
    }

    @Override
    public boolean isMelting() {
        return dataManager.get(MELTING);
    }

    @Override
    public void melting() {
        if (this.isMelting()) {
            if (sound % 20 == 0) playSound(SRPSounds.INFECTED_MELT, 1.0F, 1.0F);
            sound++;
            if (getTHeigh() > 0.7) {
                setaSize(-0.005F);
                setTHeigh(-0.01F);
                setSize(width, this.getTHeigh());
            }
            if (world.isRemote) {
                spawnParticles(SRPEnumParticle.GCLOUD, 127, 106, 0);
                spawnParticles(SRPEnumParticle.GCLOUD, 127, 0, 0);
                return;
            }
            if (getTHeigh() <= 0.7 || sound >= 73) {
                EntityLesh result = new EntityLesh(world);
                result.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
                if (hasCustomName()) result.setCustomNameTag(getCustomNameTag());
                setDead();
                world.spawnEntity(result);
                result.setLegs(SRPAttributes.INFADVENTURER_V, false);
            }
        }

    }

    @Override
    public boolean attackEntityAsMobAOE(Entity entity) {
        return attackEntityAsMob(entity);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {};

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public float getTHeigh() {
        return dataManager.get(HEIGH);
    }

    @Override
    public void setTHeigh(float amount) {
        dataManager.set(HEIGH, dataManager.get(HEIGH) + amount);
    }

    @Override
    public float getaSize() {
        return this.aSize;
    }

    @Override
    public void setaSize(float amount) {
        this.aSize += amount;
    }

    @Override
    public float getSelfeFlashIntensity2() {
        return aSize;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.getParasiteStatus() != 0 ? SRPSounds.MOBSILENCE : SRPSounds.INFECTEDWOLF_GROWL;
    }

    @Override
    public final int getParasiteIDRegister() {
        return 100 + getIndex();
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return isImmune(source) || super.isEntityInvulnerable(source);
    }

    @Override
    protected ResourceLocation getLootTable() {
        return ModDefinitions.getResource("entities/inf_phoenix");
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return ItemSpawner.getEggFor(this);
    }

    public abstract String getName();

    protected abstract boolean isImmune(DamageSource source);

    protected abstract int getIndex();

    @Override
    public int canSpawnByIDData() {
        return 100;
    }

}
