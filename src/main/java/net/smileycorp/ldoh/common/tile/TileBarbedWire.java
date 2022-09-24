package net.smileycorp.ldoh.common.tile;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.smileycorp.ldoh.common.block.BlockBarbedWire;
import net.smileycorp.ldoh.common.block.LDOHBlocks;
import net.smileycorp.ldoh.common.damage.DamageSourceBarbedWire;
import net.smileycorp.ldoh.common.util.EnumBarbedWireMat;

public class TileBarbedWire extends TileEntity {

	protected Random rand = new Random();
	protected int durability;
	protected int cooldown=0;
	protected EnumBarbedWireMat mat;

	protected boolean isEnchanted = false;
	protected Map<Enchantment, Integer> enchant_map = new HashMap<Enchantment, Integer>();

	public TileBarbedWire() {
		this(EnumBarbedWireMat.IRON);
	}

	public TileBarbedWire(EnumBarbedWireMat material) {
		mat = material;
		durability = material.getDurability();
	}

	public int getOrUpdateCooldown() {
		return cooldown > 0 ? cooldown-- : 0;
	}

	public void causeDamage() {
		if (cooldown > 0) return;
		AxisAlignedBB bb = new AxisAlignedBB(pos);
		for (EntityLiving entity : world.getEntitiesWithinAABB(EntityLiving.class, bb)) {
			if (entity.isDead |! entity.isAddedToWorld()) break;
			if (mat.getDamage()>0) {
				float modifier = 1;
				for (Entry<Enchantment, Integer> entry : enchant_map.entrySet()) {
					modifier += entry.getKey().calcDamageByCreature(entry.getValue(), entity.getCreatureAttribute());
				}
				if (entity.attackEntityFrom(new DamageSourceBarbedWire(this), mat.getDamage() * modifier)) {
					if (!enchant_map.containsKey(Enchantments.UNBREAKING)) durability--;
					else if (rand.nextInt(enchant_map.get(Enchantments.UNBREAKING)) <= 0) durability--;
					cooldown = 5;
				}
				if (enchant_map.containsKey(Enchantments.FIRE_ASPECT)) {
					entity.setFire(enchant_map.get(Enchantments.FIRE_ASPECT) * 4);
				}
			}
		}
		sendUpdate();
	}

	public void damage(int damage) {
		durability -= damage;
		sendUpdate();
	}

	private void sendUpdate() {
		IBlockState state = world.getBlockState(pos);
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, state, state, 3);
		world.scheduleBlockUpdate(pos,getBlockType(),0,0);
		markDirty();
	}

	public int getDurability() {
		return durability;
	}

	public float getDamage() {
		return mat.getDamage();
	}

	public EnumBarbedWireMat getMaterial() {
		return world.getBlockState(pos).getValue(BlockBarbedWire.MATERIAL);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("durability")) {
			durability = compound.getInteger("durability");
		}
		if (compound.hasKey("cooldown")) {
			cooldown = compound.getInteger("cooldown");
		}
		if (compound.hasKey("enchantments")) {
			NBTTagCompound enchants = compound.getCompoundTag("enchantments");
			for (String name : enchants.getKeySet()) {
				try {
					Enchantment enchant = Enchantment.getEnchantmentByLocation(name);
					if (enchant != null) enchant_map.put(enchant, enchants.getInteger(name));
				} catch (Exception e) {}
			}
		}
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("durability", durability);
		compound.setInteger("cooldown", cooldown);
		if (!enchant_map.isEmpty()) {
			NBTTagCompound enchants = new NBTTagCompound();
			for (Entry<Enchantment, Integer> entry : enchant_map.entrySet())
				enchants.setInteger(entry.getKey().getRegistryName().toString(), entry.getValue());
			compound.setTag("enchantments", enchants);
		}
		return super.writeToNBT(compound);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 3, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager network, SPacketUpdateTileEntity packet) {
		super.onDataPacket(network, packet);
		handleUpdateTag(packet.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound tag = super.getUpdateTag();
		tag.setInteger("durability", durability);
		tag.setBoolean("isEnchanted", isEnchanted());
		return tag;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		if (tag.hasKey("durability")) {
			durability = tag.getInteger("durability");
		}
		if (tag.hasKey("isEnchanted")) {
			isEnchanted = tag.getBoolean("isEnchanted");
		}
		super.handleUpdateTag(tag);
	}

	public void setDurability(int durability) {
		this.durability = durability;
		sendUpdate();
	}

	public boolean isEnchanted() {
		return world.isRemote ? isEnchanted : !enchant_map.isEmpty();
	}

	public boolean hasEnchantment(Enchantment enchant) {
		return enchant_map.containsKey(enchant);
	}

	public int getEnchantmentLevel(Enchantment enchant) {
		return enchant_map.get(enchant);
	}

	public void applyEnchantment(Enchantment enchant, int level) {
		enchant_map.put(enchant, level);
	}

	public void removeEnchantment(Enchantment enchant) {
		enchant_map.remove(enchant);
	}

	public void clearEnchantments() {
		enchant_map.clear();
	}

	public ItemStack getDrop() {
		ItemStack drop = new ItemStack(LDOHBlocks.BARBED_WIRE, 1, mat.ordinal());
		NBTTagCompound nbt = new NBTTagCompound();
		if (getDurability() < mat.getDurability()) {
			nbt.setInteger("durability", getDurability());
			drop.setTagCompound(nbt);
		}
		if (isEnchanted()) {
			NBTTagList enchants = new NBTTagList();
			for (Entry<Enchantment, Integer> entry : enchant_map.entrySet()) {
				NBTTagCompound enchant = new NBTTagCompound();
				enchant.setShort("id", (short) Enchantment.getEnchantmentID(entry.getKey()));
				enchant.setShort("lvl", entry.getValue().shortValue());
				enchants.appendTag(enchant);
			}
			nbt.setTag("ench", enchants);
		}
		if (nbt.getSize() > 0) drop.setTagCompound(nbt);
		return drop;
	}

}
