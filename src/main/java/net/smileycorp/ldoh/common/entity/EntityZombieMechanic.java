package net.smileycorp.ldoh.common.entity;

import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.smileycorp.ldoh.common.item.ItemSpawner;
import net.smileycorp.ldoh.common.item.LDOHItems;
import de.maxhenkel.car.items.ModItems;

public class EntityZombieMechanic extends EntityZombie {

	public EntityZombieMechanic(World world) {
		super(world);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.WRENCH));
		setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(LDOHItems.MECHANIC_HAT));
		inventoryArmorDropChances[EntityEquipmentSlot.MAINHAND.getIndex()] = 0.1F;
		inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.1F;
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		if (source.getTrueSource() instanceof EntityPlayer) {
			if (!GameStageHelper.hasStage((EntityPlayer) source.getTrueSource(), "car_stage")) {
				Item item = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation("gamestagebooks", "car_stage"));
				if(item!=null) entityDropItem(new ItemStack(item), 0.0F);
			}
		}
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
