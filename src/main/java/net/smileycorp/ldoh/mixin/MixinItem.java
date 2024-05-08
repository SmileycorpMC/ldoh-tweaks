package net.smileycorp.ldoh.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.smileycorp.ldoh.common.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {

    //add damage and attack speed to the crowbar
    @Inject(at = @At("TAIL"), method = "getAttributeModifiers(Lnet/minecraft/inventory/EntityEquipmentSlot;Lnet/minecraft/item/ItemStack;)Lcom/google/common/collect/Multimap;", cancellable = true, remap = false)
    public void getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack, CallbackInfoReturnable<Multimap<String, AttributeModifier>> callback) {
        Multimap<String, AttributeModifier> map = callback.getReturnValue();
        if (Loader.isModLoaded("cfm")) ModUtils.getAttributeModifiers(slot, stack, map);
        callback.setReturnValue(map);
    }
    
    @Inject(at = @At("HEAD"), method = "getContainerItem(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", remap = false, cancellable = true)
    public void getContainerItem(ItemStack stack, CallbackInfoReturnable<ItemStack> callback) {
        if (stack.getItem() == Items.EXPERIENCE_BOTTLE) callback.setReturnValue(new ItemStack(Items.GLASS_BOTTLE));
    }
    
    @Inject(at = @At("HEAD"), method = "hasContainerItem(Lnet/minecraft/item/ItemStack;)Z", remap = false, cancellable = true)
    public void hasContainerItem(ItemStack stack, CallbackInfoReturnable<Boolean> callback) {
        if (stack.getItem() == Items.EXPERIENCE_BOTTLE) callback.setReturnValue(true);
    }

}
