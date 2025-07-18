package net.smileycorp.ldoh.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends EntityPlayer {

    public MixinEntityPlayerSP(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

}
