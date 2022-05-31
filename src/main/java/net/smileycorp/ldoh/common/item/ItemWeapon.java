package net.smileycorp.ldoh.common.item;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Multimap;


public class ItemWeapon extends ItemBase {

	protected double damage;
	protected double speed;
	private boolean isTool;

	public ItemWeapon(String name, int durability, double damage, double speed, boolean isTool) {
		super(name);
		setMaxStackSize(1);
		setMaxDamage(durability);
		this.damage=damage;
		this.speed=speed;
		this.isTool = isTool;
	}

	public ItemWeapon(String name, int durability, double damage) {
		this(name, durability, damage, -2.4000000953674316D, false);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(1, attacker);
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {
		if (state.getBlockHardness(world, pos) != 0.0D &! isTool) {
			stack.damageItem(2, entity);
		}
		return true;
	}

	@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", damage, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", speed, 0));
		}
		return multimap;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		if (isTool) {
			Material material = state.getMaterial();
			return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : 8.0F;
		}
		return super.getDestroySpeed(stack, state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}
}
