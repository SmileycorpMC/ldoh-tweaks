package net.smileycorp.ldoh.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.smileycorp.ldoh.common.util.ModUtils;
import net.smileycorp.ldoh.common.world.BlockStateCache;
import net.smileycorp.ldoh.integration.tektopia.TektopiaUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
			//join entity team, used to make player join merc teams before opening their gui
			if (player.getTeam() == null) ModUtils.tryJoinTeam(player, this);
			//hack to bypass mixin restrictions
			//check if tektopia is installed because the pack no longer requires it
			if (Loader.isModLoaded("tektopia") && TektopiaUtils.isToken(stack)) {
				//use merc tokens transforming tek villagers code before handling other villager interactions
				stack.interactWithEntity(player, this, hand);
				callback.setReturnValue(true);
				callback.cancel();
			}
		}
	}

	@Redirect(method = "getCanSpawnHere", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
	public IBlockState getCanSpawnHere$getBlockState(World world, BlockPos pos) {
		return BlockStateCache.getBlockState(world, pos);
	}

}
