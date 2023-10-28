package net.smileycorp.ldoh.common.entity.zombie;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.util.IDummyZombie;

public class EntityDummyZombie2 extends EntityZombie implements IDummyZombie {

    public EntityDummyZombie2(World world) {
        super(world);
    }

    @Override
    public boolean shouldBurnInDay() {
        return false;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        ResourceLocation loc = new ResourceLocation("minecraft", "zombie");
        if (EntityList.ENTITY_EGGS.containsKey(loc)) {
            ItemStack stack = new ItemStack(Items.SPAWN_EGG);
            ItemMonsterPlacer.applyEntityIdToItemStack(stack, loc);
            return stack;
        }
        return ItemStack.EMPTY;
    }

}
