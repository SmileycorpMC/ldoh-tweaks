package net.smileycorp.ldoh.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.world.BlockStateCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//bullshit hacky nonsense to bypass half the calculations used in zombie ai
//normally optimisation would require reducing entities, but we kinda need them so this is the next best thing
//well it would be if I didn't accidentally create a performance black hole
//better caching is helping a bit but every ounce of performance needs to be squeezed out of optimising pathfinding so it's better in the long run
@Mixin(EntityZombie.class)
public abstract class MixinEntityZombie extends EntityMob {

	public MixinEntityZombie(World world) {
		super(world);
	}

	//could and should probably do this via events, but we need to make sure this happens before anything else is done
	@Inject(at=@At("TAIL"), method = "<init>")
	public void EntityZombie(World world, CallbackInfo callback) {
		//replace default task selector with our own version
		//tasks = new LDOHZombieAITasks((EntityZombie)(EntityMob)this);
	}

	@Redirect(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
	public IBlockState attackEntityFrom$getBlockState(World world, BlockPos pos) {
		return BlockStateCache.getBlockState(world, pos);
	}

}
