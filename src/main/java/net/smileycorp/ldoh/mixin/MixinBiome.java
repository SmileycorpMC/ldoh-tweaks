package net.smileycorp.ldoh.mixin;

import biomesoplenty.common.biome.BOPBiome;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class MixinBiome {

    //client side only
    //basically just a hack to make the common community names for biomes show in the nature's compass and F3 menu
    @Inject(at=@At("TAIL"), method = "getBiomeName", cancellable = true)
    public void getBiomeName(CallbackInfoReturnable<String> callback) {
        String ret = callback.getReturnValue();
        if (ret == null) return;
        if (ret.equals("Deep Ocean")) callback.setReturnValue("Canyon");
        if (ret.equals("Ocean")) callback.setReturnValue("Shallow Canyon");
        if (ret.equals("Wasteland City")) callback.setReturnValue("City");
        if (ret.equals("Wasteland") && ((Object)this) instanceof BOPBiome) callback.setReturnValue("Badlands");
    }
}
