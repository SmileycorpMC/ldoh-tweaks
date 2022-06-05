package net.smileycorp.ldoh.integration.iguanatweaks.mixin;

import net.insane96mcp.iguanatweaks.modules.ModuleMovementRestriction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.smileycorp.ldoh.common.util.ModUtils;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mrcrayfish.furniture.items.ItemCrate;


@Mixin(value = ModuleMovementRestriction.class)
public abstract class MixinModuleMovementRestriction {

	@Inject(at=@At("RETURN"), method = "GetStackWeight(Lnet/minecraft/item/ItemStack;)F", remap = false)
	private static void processInitialInteract(ItemStack stack, CallbackInfoReturnable<Float> callback) {
		Item item = stack.getItem();
		if (item instanceof ItemCrate) {
			float weight = ModUtils.calculateCrateWeight(stack, callback.getReturnValue());
			callback.setReturnValue(weight);
		}
	}

}
