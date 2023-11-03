package net.smileycorp.ldoh.mixin;

import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiCreateWorld.class)
public class MixinGuiCreateWorld {

    @ModifyVariable(method = "actionPerformed", at = @At(value = "STORE", ordinal = 0))
    public WorldSettings worldSettings(WorldSettings settings) {
        //(Dif)
        return settings;
    }

}
