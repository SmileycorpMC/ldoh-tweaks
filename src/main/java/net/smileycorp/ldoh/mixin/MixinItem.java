package net.smileycorp.ldoh.mixin;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.smileycorp.ldoh.common.util.ModUtils;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Multimap;

@Mixin(Item.class)
public class MixinItem {

	@Inject(at=@At("TAIL"), method = "getAttributeModifiers(Lnet/minecraft/inventory/EntityEquipmentSlot;Lnet/minecraft/item/ItemStack;)Lcom/google/common/collect/Multimap;", cancellable = true, remap = false)
	public void getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack, CallbackInfoReturnable<Multimap<String, AttributeModifier>> callback) {
		Multimap<String, AttributeModifier> map = callback.getReturnValue();
		ModUtils.getAttributeModifiers(slot, stack, map);
		callback.setReturnValue(map);
	}

}
