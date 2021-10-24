package net.smileycorp.ldoh.common.apocalypseevent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.ldoh.common.ModDefinitions;

import com.mojang.authlib.GameProfile;

public class WorldSaveApocalypseEvent extends WorldSavedData {
	
	public static final String DATA = ModDefinitions.modid + "_BossEvent";
	
	private Map<String, ApocalypseBossEvent> ongoingEvents =  new HashMap<String, ApocalypseBossEvent>();
	
	public WorldSaveApocalypseEvent() {
		super(DATA);
	}
	
	public WorldSaveApocalypseEvent(String data) {
		super(data);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		for (String uuid : nbt.getKeySet()) {
			if (nbt.getTagId(uuid) == 10) {
				EntityPlayer player = getPlayerFromUUID(uuid);
				ApocalypseBossEvent event = new ApocalypseBossEvent(player);
				event.readFromNBT(nbt.getCompoundTag(uuid));
				ongoingEvents.put(uuid, event);
			}
		}
	}

	private EntityPlayer getPlayerFromUUID(String uuid) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		return side == Side.CLIENT ? null :
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(UUID.fromString(uuid));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
				String uuid = player.getUniqueID().toString();
				if (!ongoingEvents.containsKey(uuid)) {
					ApocalypseBossEvent event = new ApocalypseBossEvent(player);
					ongoingEvents.put(uuid, event);
				}
			}
			for (Entry<String, ApocalypseBossEvent> entry : ongoingEvents.entrySet()) {
				String uuid = entry.getKey();
				ApocalypseBossEvent event = entry.getValue();
				nbt.setTag(uuid, event.writeToNBT(new NBTTagCompound()));
			}
		}
		return nbt;
	}
	
	public Set<ApocalypseBossEvent> getEvents() {
		Set<ApocalypseBossEvent> events = new HashSet<ApocalypseBossEvent>();
		for (ApocalypseBossEvent event : ongoingEvents.values()) {
			if (event.getPlayer()!=null) events.add(event);
		}
		return events;
	}
	
	public ApocalypseBossEvent getEventForPlayer(EntityPlayer player) {
		return getEventForPlayer(player.getUniqueID());
	}
	
	public ApocalypseBossEvent getEventForPlayer(GameProfile profile) {
		return getEventForPlayer(profile.getId());
	}
	
	public ApocalypseBossEvent getEventForPlayer(UUID uuid) {
		return getEventForPlayer(uuid.toString());
	}
	
	public ApocalypseBossEvent getEventForPlayer(String uuid) {
		if (! ongoingEvents.containsKey(uuid)) {
			EntityPlayer player = getPlayerFromUUID(uuid);
			ApocalypseBossEvent event = new ApocalypseBossEvent(player);
			ongoingEvents.put(uuid, event);
			markDirty();
		}
		return ongoingEvents.get(uuid);
	}

	public static WorldSaveApocalypseEvent get(World world) {
		WorldSaveApocalypseEvent data = (WorldSaveApocalypseEvent) world.getMapStorage().getOrLoadData(WorldSaveApocalypseEvent.class, WorldSaveApocalypseEvent.DATA);
		if (data== null) {
			data = new WorldSaveApocalypseEvent();
			world.getMapStorage().setData(WorldSaveApocalypseEvent.DATA, data);
		}
		return data;
	}
	
	
}
