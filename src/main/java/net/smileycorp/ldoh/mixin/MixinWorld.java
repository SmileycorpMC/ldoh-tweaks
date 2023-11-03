package net.smileycorp.ldoh.mixin;

import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.smileycorp.ldoh.common.GameDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(World.class)
public class MixinWorld implements GameDifficulty {

    @Shadow protected WorldInfo worldInfo;

    @Override
    public Level getGameDifficulty() {
        return GameDifficulty.getGameDifficulty(worldInfo);
    }

    @Override
    public void setGameDifficulty(Level difficulty) {
        GameDifficulty.setGameDifficulty(worldInfo, difficulty);
    }

}
