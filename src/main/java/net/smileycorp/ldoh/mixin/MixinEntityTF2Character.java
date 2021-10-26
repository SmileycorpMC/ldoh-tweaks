package net.smileycorp.ldoh.mixin;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.util.ModUtils;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import rafradek.TF2weapons.entity.IEntityTF2;

@Mixin(targets = {"rafradek.TF2weapons.entity.mercenary.EntityTF2Character"}, remap = false)
public abstract class MixinEntityTF2Character extends EntityCreature implements IMob, IMerchant, IEntityTF2, IEntityOwnable {
	
	public MixinEntityTF2Character(World world) {
		super(world);
	}
	
	@Inject(at=@At("HEAD"), method = "processInteract(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Z", cancellable = true)
	public void processInteract(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> callback) {
		if (!world.isRemote) {
			if (player.getTeam() == null) {
				ModUtils.addPlayerToTeam(player, getTeam().getName());
			}
		}
	}
	
	
	@Override
	@Shadow
	public Team getTeam() {
		 throw new IllegalStateException("Mixin failed to shadow getTeam()");
	}
	
}
