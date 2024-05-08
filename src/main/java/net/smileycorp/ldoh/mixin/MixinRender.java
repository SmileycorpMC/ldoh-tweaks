package net.smileycorp.ldoh.mixin;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.smileycorp.ldoh.client.RandomTextureCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//this is the reak hacky shit
@Mixin(Render.class)
public abstract class MixinRender {

    //mostly the same as vanilla behavior
    @Inject(at = @At("HEAD"), method = "bindEntityTexture(Lnet/minecraft/entity/Entity;)Z", cancellable = true)
    protected void bindEntityTexture(Entity entity, CallbackInfoReturnable<Boolean> callback) {
        //check if optifine is installed so we can default to their random mob texture loading
        if (Loader.isModLoaded("optifine")) return;
        //get the base texture
        ResourceLocation resourcelocation = getEntityTexture(entity);
        if (resourcelocation == null) {
            callback.setReturnValue(false);
        } else {
            //redirect to our random texture cache to get any alternate textures
            bindTexture(RandomTextureCache.INSTANCE.getLoc(resourcelocation, entity));
            callback.setReturnValue(true);
        }
    }

    @Shadow
    protected abstract ResourceLocation getEntityTexture(Entity entity);

    @Shadow
    public abstract void bindTexture(ResourceLocation location);


}
