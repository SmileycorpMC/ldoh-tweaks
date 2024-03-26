package net.smileycorp.ldoh.mixin;

import net.minecraft.util.ResourceLocation;
import net.smileycorp.ldoh.common.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourceLocation.class)
public class MixinResourceLocation {

    @Shadow
    protected String resourceDomain;

    @Shadow
    protected String resourcePath;

    @Inject(at = @At("TAIL"), method = "<init>(I[Ljava/lang/String;)V")
    public void init(int unused, String[] resourceName, CallbackInfo ci) {
        if (resourceDomain.equals("hundreddayz")) {
            if (resourcePath.contains("_no_break") || resourcePath.contains("_no_place")) {
                resourceDomain = "minecraft";
                resourcePath = resourcePath.contains("husk") ? "husk" : "zombie";
            } else resourceDomain = Constants.MODID;
        }
    }


}
