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

public abstract class EntityProfessionZombie extends EntityZombie {

	public EntityProfessionZombie(World world) {
		super(world);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		setEquipment();
		inventoryArmorDropChances[EntityEquipmentSlot.MAINHAND.getIndex()] = 0.1F;
		inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.1F;
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		if (source.getTrueSource() instanceof EntityPlayer) tryDropBook((EntityPlayer) source.getTrueSource());
		else if (source.getTrueSource() instanceof EntityTurret) tryDropBook(((EntityTurret) source.getTrueSource()).getOwner());
	}

	private void tryDropBook(EntityPlayer player) {
		if (player != null) {
			if (!GameStageHelper.hasStage(player, getStage())) {
				Item item = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation("gamestagebooks", getStage()));
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

	protected abstract void setEquipment();
	protected abstract String getStage();

}
