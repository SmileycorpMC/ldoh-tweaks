package net.smileycorp.ldoh.common.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.followme.common.ai.AIFollowPlayer;
import net.smileycorp.followme.common.network.FollowSyncMessage;
import net.smileycorp.followme.common.network.PacketHandler;
import net.smileycorp.ldoh.common.util.ModUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public interface IFollowers {

	public boolean isCrouching();

	public void setCrouching();

	public void setUncrouching();

	public boolean stopFollowing(EntityLiving entity);
	
	public boolean isFollowing(EntityLiving entity);

	public void readFromNBT(NBTTagCompound compound);

	public NBTTagCompound writeToNBT();

	public static class Storage implements IStorage<IFollowers> {

		@Override
		public NBTBase writeNBT(Capability<IFollowers> capability, IFollowers instance, EnumFacing side) {
			return instance.writeToNBT();
		}

		@Override
		public void readNBT(Capability<IFollowers> capability, IFollowers instance, EnumFacing side, NBTBase nbt) {
			instance.readFromNBT((NBTTagCompound) nbt);
		}


	}

	public static class Followers implements IFollowers {

		protected EntityPlayer player;
		protected boolean crouching = false;

		protected List<WeakReference<EntityLiving>> followers = new ArrayList<WeakReference<EntityLiving>>();

		public Followers() {
			this(null);
		}

		public Followers(EntityPlayer player) {
			this.player = player;
		}

		@Override
		public void readFromNBT(NBTTagCompound nbt) {
			if (nbt.hasKey("crouching")) crouching = nbt.getBoolean("crouching");
			if (player!=null && nbt.hasKey("followers")) for (int id : nbt.getIntArray("followers")) {
				Entity entity = player.world.getEntityByID(id);
				if (entity instanceof EntityLiving) followers.add(new WeakReference<EntityLiving>((EntityLiving) entity))	;
			}
		}

		@Override
		public NBTTagCompound writeToNBT() {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("crouching", crouching);
			int[] ids = new int[]{};
			for (WeakReference<EntityLiving> ref : followers) {
				EntityLiving entity = ref.get();
				if (entity != null) {
					if (!entity.isDead && entity.isAddedToWorld()) ArrayUtils.add(ids, entity.getEntityId());
				}
			}
			nbt.setIntArray("followers", ids);
			return nbt;
		}

		@Override
		public boolean isCrouching() {
			return crouching;
		}

		@Override
		public void setCrouching() {
			if (player!=null) {
				World world = player.world;
				for (EntityLiving entity : world.getEntitiesWithinAABB(EntityLiving.class, player.getEntityBoundingBox().grow(6))) {
					if (entity!=null) {
						EntityAITasks tasks = entity.tasks;
						AIFollowPlayer followAI = null;
						for (EntityAITaskEntry task : tasks.taskEntries) {
							if (task.action instanceof AIFollowPlayer) {
								AIFollowPlayer ai = (AIFollowPlayer) task.action;
								if (ai.getUser()==player) {
									followAI = ai;
									break;
								}
							}
						}
						if (followAI != null) {
							tasks.removeTask(followAI);
							IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
							if (!attribute.hasModifier(ModUtils.FOLLOW_MODIFIER)) attribute.applyModifier(ModUtils.FOLLOW_MODIFIER);
							followers.add(new WeakReference<EntityLiving>(entity));
						}
					}
				}
			}
			crouching = true;
		}

		@Override
		public void setUncrouching() {
			if (player!=null) {
				for (WeakReference<EntityLiving> ref : followers) {
					EntityLiving entity = ref.get();
					if (entity != null) {
						if (!entity.isDead && entity.isAddedToWorld()) {
							IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
							if (attribute.hasModifier(ModUtils.FOLLOW_MODIFIER)) attribute.removeModifier(ModUtils.FOLLOW_MODIFIER);
							AIFollowPlayer task = new AIFollowPlayer(entity, player);
							for (EntityAITaskEntry entry : entity.targetTasks.taskEntries) if (entry.using) entry.action.resetTask();
							entity.tasks.addTask(0, task);
						}
					}
				}
			}
			followers.clear();
			crouching = false;
		}

		@Override
		public boolean stopFollowing(EntityLiving entity) {
			if (player!=null) {
				WeakReference<EntityLiving> target = null;
				for (WeakReference<EntityLiving> ref : followers) {
					if (ref.get() == entity) {
						target = ref;
						break;
					}
				}
				if (target!=null) {
					if (player instanceof EntityPlayerMP) {
						PacketHandler.NETWORK_INSTANCE.sendTo(new FollowSyncMessage(entity, true), (EntityPlayerMP) player);
					}
					IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
					if (attribute.hasModifier(ModUtils.FOLLOW_MODIFIER)) attribute.removeModifier(ModUtils.FOLLOW_MODIFIER);
					followers.remove(target);
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean isFollowing(EntityLiving entity) {
			if (player!=null) {
				for (WeakReference<EntityLiving> ref : followers) {
					if (ref.get() == entity) return true;
				}
			}
			return false;
		}
	}

	public static class Provider implements ICapabilitySerializable<NBTTagCompound> {

		protected final IFollowers instance;

		public Provider(EntityPlayer player) {
			instance = new Followers(player);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == LDOHCapabilities.FOLLOWERS;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return capability == LDOHCapabilities.FOLLOWERS ? LDOHCapabilities.FOLLOWERS.cast(instance) : null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return (NBTTagCompound) LDOHCapabilities.FOLLOWERS.getStorage().writeNBT(LDOHCapabilities.FOLLOWERS, instance, null);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			LDOHCapabilities.FOLLOWERS.getStorage().readNBT(LDOHCapabilities.FOLLOWERS, instance, null, nbt);
		}

	}

}
