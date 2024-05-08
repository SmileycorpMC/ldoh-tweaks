package net.smileycorp.ldoh.mixin;

import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.world.World;
import net.smileycorp.ldoh.common.entity.zombie.EntityProfessionZombie;
import net.smileycorp.ldoh.common.entity.zombie.EntityTF2Zombie;
import net.smileycorp.ldoh.common.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityZombie.class)
public abstract class MixinEntityZombie extends EntityMob {

	private boolean hasESMAI;
	private int phase = 0;

	public MixinEntityZombie(World world) {
		super(world);
	}

	@Inject(at=@At("TAIL"), method = "initEntityAI")
	public void initEntityAI(CallbackInfo callback) {
		//check to make sure all zombie derivative entities don't get the esm ai
		hasESMAI = ((EntityMob)this).getClass() == EntityZombie.class || ((EntityMob)this).getClass() == EntityZombieVillager.class
				|| ((EntityMob)this).getClass() == EntityHusk.class || ((EntityMob)this).getClass() == EntityTF2Zombie.class
				|| ((EntityMob)this).getClass() == EntityProfessionZombie.class;
		if (hasESMAI) ModUtils.addGriefTask(this);
	}


	@Inject(at=@At("TAIL"), method = "onLivingUpdate")
	public void livingUpdate(CallbackInfo callback) {
		//check to make sure all zombie derivative entities don't get the esm ai
		if (!hasESMAI || world.isRemote) return;
		if (phase < 1 && world.getWorldTime() > 264000) {
			phase = 1;
			ModUtils.addDigTask(this);
		}
		if (phase < 2 && world.getWorldTime() > 504000){
			phase = 2;
			ModUtils.addBuildTask(this);
		}
	}

}
