package net.smileycorp.ldoh.mixin;

import net.minecraft.world.WorldSettings;
import net.smileycorp.ldoh.common.GameDifficulty;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldSettings.class)
public class MixinWorldSettings implements GameDifficulty {

    private Level difficulty = Level.HARDCORE;

    @Override
    public Level getGameDifficulty() {
        return difficulty;
    }

    @Override
    public void setGameDifficulty(Level difficulty) {
        this.difficulty = difficulty;
    }

}
