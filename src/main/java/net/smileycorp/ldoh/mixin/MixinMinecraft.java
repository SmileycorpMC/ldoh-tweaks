package net.smileycorp.ldoh.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.smileycorp.ldoh.common.tile.TileTurret;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    //add proper turret te data
    @Inject(at = @At("HEAD"), method = "storeTEInStack", cancellable = true)
    public void storeTEInStack(ItemStack stack, TileEntity te, CallbackInfoReturnable<ItemStack> callback) {
        if (!(te instanceof TileTurret)) return;
        TileTurret tile = (TileTurret) te;
        NBTTagCompound nbt = tile.getDropNBT();
        if (tile.getEntity() != null) if (tile.getEntity().isEnemy()) nbt.setBoolean("isEnemy", true);
        if (!nbt.hasNoTags()) stack.setTagCompound(nbt);
        callback.setReturnValue(stack);
    }
    
}
