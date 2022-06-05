package net.smileycorp.ldoh.integration.iguanatweaks.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mrcrayfish.guns.item.ItemColored;
import com.mrcrayfish.guns.item.ItemGun;

@Mixin(ItemGun.class)
public class MixinItemGun extends ItemColored {

	@Inject(at=@At("HEAD"), method = "onItemRightClick(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;")
	public void processInitialInteract(World world, EntityPlayer player, EnumHand hand, CallbackInfoReturnable<ActionResult<?>> callback) {

	}

}
