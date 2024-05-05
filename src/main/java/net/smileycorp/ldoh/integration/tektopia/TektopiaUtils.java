package net.smileycorp.ldoh.integration.tektopia;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.tangotek.tektopia.Village;
import net.tangotek.tektopia.entities.EntityVillagerTek;

public class TektopiaUtils {
    public static final Item TF2_PROF_TOKEN = new ItemTF2ProfessionToken();
    @CapabilityInject(IVillageData.class)
    public final static Capability<IVillageData> VILLAGE_DATA = null;
    
    public static boolean isToken(ItemStack stack) {
        return stack.getItem() == TF2_PROF_TOKEN;
    }

    public static void addTargetTask(EntityCreature entity) {
        entity.targetTasks.addTask(3, new EntityAINearestAttackableTarget(entity, EntityVillagerTek.class, false));
    }

    //gets the cost of an item for a particular tektopia village
    public static int getCost(Village village, int baseCost) {
        float mult = Math.min((village.getTownData().getProfessionSales() / 5) * 0.2F, 10.0F);
        return (int) (baseCost * (1.0F + mult));
    }

    public static boolean isTooFarFromVillage(EntityLiving entity, IBlockAccess world) {
        IVillageData cap = entity.getCapability(VILLAGE_DATA, null);
        if (!cap.hasVillage()) return false;
        BlockPos village = cap.getVillage().getCenter();
        return entity.getDistance(village.getX(), village.getY(), village.getZ()) >= 75;
    }

    public static boolean isVillager(EntityLivingBase target) {
        return target instanceof EntityVillagerTek;
    }
}
