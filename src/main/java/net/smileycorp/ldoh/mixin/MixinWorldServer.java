package net.smileycorp.ldoh.mixin;

import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.smileycorp.ldoh.common.difficulty.DifficultyOptions;
import net.smileycorp.ldoh.common.difficulty.GameDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldServer.class)
public class MixinWorldServer implements DifficultyOptions {

    private GameDifficulty difficulty = GameDifficulty.HARDCORE;

    @Inject(at = @At("HEAD"), method = "initialize")
    public void initialize(WorldSettings settings, CallbackInfo callback) {
        setGameDifficulty(DifficultyOptions.getDifficulty(settings));
    }

    @Override
    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public void setGameDifficulty(GameDifficulty difficulty) {
        this.difficulty = difficulty;
    }

}
