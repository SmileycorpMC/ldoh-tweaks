package net.smileycorp.ldoh.mixin;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.WorldInfo;
import net.smileycorp.ldoh.common.GameDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldInfo.class)
public class MixinWorldInfo implements GameDifficulty {

    private Level difficulty = Level.HARDCORE;

    @Override
    public Level getGameDifficulty() {
        return difficulty;
    }

    @Override
    public void setGameDifficulty(Level difficulty) {
        this.difficulty = difficulty;
    }

    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/world/WorldSettings;Ljava/lang/String;)V")
    public void WorldInfo(WorldSettings settings, String name, CallbackInfo callbackInfo) {
        setGameDifficulty(GameDifficulty.getGameDifficulty(settings));
    }

    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/nbt/NBTTagCompound;)V")
    public void WorldInfo(NBTTagCompound nbt, CallbackInfo callback) {
        if (nbt.hasKey("GameDifficulty")) setGameDifficulty(GameDifficulty.getGameDifficulty(nbt.getByte("GameDifficulty")));
    }

    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/world/storage/WorldInfo;)V")
    public void WorldInfo(WorldInfo info, CallbackInfo callback) {
        setGameDifficulty(GameDifficulty.getGameDifficulty(info));
    }

    @Inject(at = @At("TAIL"), method = "updateTagCompound")
    public void updateTagCompound(NBTTagCompound nbt, NBTTagCompound playerNbt, CallbackInfo callback) {
        nbt.setByte("GameDifficulty", (byte)getGameDifficulty().ordinal());
    }

    @Inject(at = @At("HEAD"), method = "getGameType", cancellable = true)
    public void getGameType(CallbackInfoReturnable<GameType> callback) {
        callback.setReturnValue(getGameDifficulty() == Level.SANDBOX ? GameType.CREATIVE : getGameDifficulty() ==
                Level.TUTORIAL ? GameType.ADVENTURE : GameType.SURVIVAL);
    }

    @Inject(at = @At("HEAD"), method = "isHardcoreModeEnabled", cancellable = true)
    public void isHardcoreModeEnabled(CallbackInfoReturnable<Boolean> callback) {
        callback.setReturnValue(getGameDifficulty().hardcore());
    }

    @Inject(at = @At("HEAD"), method = "areCommandsAllowed", cancellable = true)
    public void areCommandsAllowed(CallbackInfoReturnable<Boolean> callback) {
        callback.setReturnValue(getGameDifficulty() == Level.SANDBOX);
    }

}
