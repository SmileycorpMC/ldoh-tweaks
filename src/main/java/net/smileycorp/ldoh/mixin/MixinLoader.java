package net.smileycorp.ldoh.mixin;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModClassLoader;
import net.minecraftforge.fml.common.ModContainer;
import net.smileycorp.ldoh.integration.iguanatweaks.mixin.MixinModuleMovementRestriction;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.Proxy;
import org.spongepowered.asm.util.asm.ASM;

import java.lang.reflect.Field;
import java.util.List;

@Mixin(Loader.class)
public class MixinLoader {

	@Shadow(remap = false) private List<ModContainer> mods;
	@Shadow(remap = false) private ModClassLoader modClassLoader;

	@Inject(method = "loadMods", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/LoadController;transition(Lnet/minecraftforge/fml/common/LoaderState;Z)V", ordinal = 1), remap = false)
	private void loadMods(List<String> injectedModContainers, CallbackInfo ci) {
		for (ModContainer mod : mods) {
			try {
				modClassLoader.addFile(mod.getSource());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		Mixins.addConfiguration("mixins.ldoh.iguanatweaks.json");
		Proxy mixinProxy = (Proxy) Launch.classLoader.getTransformers().stream().filter(transformer -> transformer instanceof Proxy).findFirst().get();
		try {
			Field transformerField = Proxy.class.getDeclaredField("transformer");
			transformerField.setAccessible(true);
			IMixinTransformer transformer = (IMixinTransformer) transformerField.get(mixinProxy);
			transformer.reload(MixinModuleMovementRestriction.class.getName(), new ClassNode(ASM.API_VERSION));
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}
}
