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

    @CapabilityInject(IAmbushEvent.class)
    public final static Capability<IAmbushEvent> AMBUSH = null;

    @CapabilityInject(IHunger.class)
    public final static Capability<IHunger> HUNGER = null;

    @CapabilityInject(IApocalypse.class)
    public final static Capability<IApocalypse> APOCALYPSE = null;

    @CapabilityInject(IApocalypseBoss.class)
    public final static Capability<IApocalypseBoss> APOCALYPSE_BOSS = null;

    @CapabilityInject(IFollowers.class)
    public final static Capability<IFollowers> FOLLOWERS = null;

    @CapabilityInject(ICuring.class)
    public final static Capability<ICuring> CURING = null;
    
}
