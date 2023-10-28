package net.smileycorp.ldoh.mixin;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FMLCommonHandler.class, remap = false)
public class MixinFMLCommonHandler {

    //fix mrcrayfish crashes
    @Inject(at = @At("HEAD"), method = "onPreClientTick()V", cancellable = true, remap = false)
    public void onPreClientTick(CallbackInfo callback) {
        if (Minecraft.getMinecraft().world == null) callback.cancel();
    }

    @Inject(at = @At("HEAD"), method = "onPostClientTick()V", cancellable = true, remap = false)
    public void onPostClientTick(CallbackInfo callback) {
        if (Minecraft.getMinecraft().world == null) callback.cancel();
    }

}
