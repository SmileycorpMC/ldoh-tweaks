package net.smileycorp.ldoh.mixin;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.item.LDOHItems;
import net.smileycorp.ldoh.common.util.ModUtils;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends EntityLivingBase {

	public MixinEntityLiving(World world) {
		super(world);
	}

	@Inject(at=@At("HEAD"), method = "processInitialInteract(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Z", cancellable = true)
	public void processInitialInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> callback) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote) {
			if (player.getTeam() == null) ModUtils.tryJoinTeam(player, this);
			if (stack.getItem() == LDOHItems.TF_PROF_TOKEN) {
				stack.interactWithEntity(player, this, hand);
				callback.setReturnValue(true);
				callback.cancel();
			}
		}
	}

}
