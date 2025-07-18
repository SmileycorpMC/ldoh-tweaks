package net.smileycorp.ldoh.mixin;

import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.smileycorp.ldoh.common.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//replaces the ender dragon with a parasite
@Mixin(WorldProviderEnd.class)
public abstract class MixinWorldProviderEnd extends WorldProvider {

    private boolean hasSpawnedDragon;

    @Inject(at = @At("HEAD"), method = "init()V", cancellable = true)
    public void init(CallbackInfo callback) {
        biomeProvider = new BiomeProviderSingle(Biomes.SKY);
        NBTTagCompound nbt = this.world.getWorldInfo().getDimensionData(this.world.provider.getDimension());
        if (nbt.hasKey("hasSpawnedDragon")) hasSpawnedDragon = nbt.getBoolean("hasSpawnedDragon");
        callback.cancel();
    }

    @Inject(at = @At("HEAD"), method = "onWorldUpdateEntities()V", cancellable = true)
    public void onWorldUpdateEntities(CallbackInfo callback) {
        if (!hasSpawnedDragon) hasSpawnedDragon = ModUtils.spawnDragon(world);
    }

    @Inject(at = @At("TAIL"), method = "onWorldSave()V", cancellable = true)
    public void onWorldSave(CallbackInfo callback) {
        NBTTagCompound nbt = world.getWorldInfo().getDimensionData(world.provider.getDimension());
        nbt.setBoolean("hasSpawnedDragon", hasSpawnedDragon);
        world.getWorldInfo().setDimensionData(world.provider.getDimension(), nbt);
    }

}
