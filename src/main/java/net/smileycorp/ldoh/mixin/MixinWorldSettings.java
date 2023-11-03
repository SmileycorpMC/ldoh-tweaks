package net.smileycorp.ldoh.mixin;

import net.minecraft.world.WorldSettings;
import net.smileycorp.ldoh.common.difficulty.DifficultyOptions;
import net.smileycorp.ldoh.common.difficulty.GameDifficulty;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldSettings.class)
public class MixinWorldSettings implements DifficultyOptions {

    private GameDifficulty difficulty = GameDifficulty.HARDCORE;

    @Override
    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public void setGameDifficulty(GameDifficulty difficulty) {
        this.difficulty = difficulty;
    }

}
