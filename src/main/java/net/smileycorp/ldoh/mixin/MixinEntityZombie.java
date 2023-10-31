package net.smileycorp.ldoh.mixin;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.entity.ai.LDOHZombieAITasks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityZombie.class)
public abstract class MixinEntityZombie extends EntityMob {

	public MixinEntityZombie(World world) {
		super(world);
	}

	//could and should probably do this via events, but we need to make sure this happens before anything else is done
	@Inject(at=@At("TAIL"), method = "<init>")
	public void EntityZombie(World world, CallbackInfo callback) {
		//replace default task selector with our own version
		tasks = new LDOHZombieAITasks((EntityZombie)(EntityMob)this);
	}

}
