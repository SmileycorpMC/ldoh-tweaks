package net.smileycorp.ldoh.common.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.smileycorp.ldoh.common.block.BlockBarbedWire;
import net.smileycorp.ldoh.common.util.EnumBarbedWireMat;

public class TileBarbedWire extends TileEntity {
	
	protected int durability;
	protected int cooldown=0;
	protected EnumBarbedWireMat mat;
	
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
		AxisAlignedBB bb = new AxisAlignedBB(pos);
		for (EntityLiving entity : world.getEntitiesWithinAABB(EntityLiving.class, bb)) {
			if (mat.getDamage()>0) {
				entity.attackEntityFrom(DamageSource.CACTUS, mat.getDamage());
			}
			durability--;
			cooldown = 5;
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
		world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
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
        super.readFromNBT(compound);
    }

    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	compound.setInteger("durability", durability);
        compound.setInteger("cooldown", cooldown);
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
   		return tag;
   	}
   	
   	@Override
   	public void handleUpdateTag(NBTTagCompound tag) {
   		if (tag.hasKey("durability")) {
			durability = tag.getInteger("durability");
		}
   		super.handleUpdateTag(tag);
   	}

}
