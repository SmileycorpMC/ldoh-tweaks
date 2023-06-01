package net.smileycorp.ldoh.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.smileycorp.ldoh.common.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.security.auth.callback.Callback;

@Mixin(value = FMLCommonHandler.class, remap = false)
public class MixinFMLCommonHandler {

	//fix mrcrayfish crashes
	@Inject(at=@At("HEAD"), method = "onPreClientTick()V", cancellable = true, remap = false)
	public void onPreClientTick(CallbackInfo callback) {
		if (Minecraft.getMinecraft().world == null) callback.cancel();
	}

	@Inject(at=@At("HEAD"), method = "onPostClientTick()V", cancellable = true, remap = false)
	public void onPostClientTick(CallbackInfo callback) {
		if (Minecraft.getMinecraft().world == null) callback.cancel();
	}

}
