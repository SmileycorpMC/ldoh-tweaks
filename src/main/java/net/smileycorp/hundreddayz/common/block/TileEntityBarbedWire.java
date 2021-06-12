package net.smileycorp.hundreddayz.common.block;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityBarbedWire extends TileEntity {
	
	protected int durability;
	protected float damage;
	protected int cooldown=0;
	
	public TileEntityBarbedWire() {
		this(EnumBarbedWireMat.IRON);
	}

	public TileEntityBarbedWire(EnumBarbedWireMat material) {
		damage = material.getDamage();
		durability = material.getDurability();
	}
	
	public int getOrUpdateCooldown() {
		return cooldown > 0 ? cooldown-- : 0;
	}

	public void causeDamage() {
		AxisAlignedBB bb = new AxisAlignedBB(pos);
		for (EntityLiving entity : world.getEntitiesWithinAABB(EntityLiving.class, bb)) {
			if (damage>0) {
				entity.attackEntityFrom(DamageSource.CACTUS, damage);
			}
			durability--;
			cooldown = 5;
		}
	}
	
	public int getDurability() {
		return durability;
	}
	
	public float getDamage() {
		return damage;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("durability")) {
			durability = compound.getInteger("durability");
		}
		if (compound.hasKey("cooldown")) {
			cooldown = compound.getInteger("cooldown");
		}
        super.readFromNBT(compound);
    }

    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	compound.setInteger("durability", durability);
        compound.setInteger("cooldown", cooldown);
        return super.writeToNBT(compound);
    }
    
    @Override
   	public NBTTagCompound getUpdateTag() {
   		NBTTagCompound tag = super.getUpdateTag();
   		return writeToNBT(tag);
   	}
   	
   	@Override
   	public void handleUpdateTag(NBTTagCompound tag) {
   		super.handleUpdateTag(tag);
   		readFromNBT(tag);
   	}

}
