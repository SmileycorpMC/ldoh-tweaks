package net.smileycorp.ldoh.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class LDOHCapabilities {

	@CapabilityInject(ISpawnTracker.class)
	public final static Capability<ISpawnTracker> SPAWN_TRACKER = null;

	@CapabilityInject(IBreakBlocks.class)
	public final static Capability<IBreakBlocks> BLOCK_BREAKING = null;

	@CapabilityInject(IUnburiedSpawner.class)
	public final static Capability<IUnburiedSpawner> UNBURIED_SPAWNER = null;

	@CapabilityInject(IMiniRaid.class)
	public final static Capability<IMiniRaid> MINI_RAID = null;

	@CapabilityInject(IHunger.class)
	public final static Capability<IHunger> HUNGER = null;

	@CapabilityInject(IApocalypse.class)
	public final static Capability<IApocalypse> APOCALYPSE = null;

	@CapabilityInject(IFollowers.class)
	public final static Capability<IFollowers> FOLLOWERS = null;

	@CapabilityInject(ICuring.class)
	public final static Capability<ICuring> CURING = null;

	@CapabilityInject(IVillageData.class)
	public final static Capability<IVillageData> VILLAGE_DATA = null;

	@CapabilityInject(IRandomEntity.class)
    public static final Capability<IRandomEntity> RANDOM_ENTITY = null;
}
