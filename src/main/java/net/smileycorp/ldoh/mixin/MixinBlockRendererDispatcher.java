package net.smileycorp.ldoh.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.smileycorp.atlas.api.client.RenderingUtils;
import net.smileycorp.ldoh.client.ClientEventListener;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRendererDispatcher.class)
public class MixinBlockRendererDispatcher {

	@Inject(at=@At("TAIL"), method = "renderBlock(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/BufferBuilder;)Z", cancellable = true)
	public void renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, BufferBuilder buffer, CallbackInfoReturnable<Boolean> callback) {
		if (blockAccess.getWorldType() != WorldType.FLAT && pos.getY() == 30) {
			if (!state.isOpaqueCube()) {
				Minecraft mc = Minecraft.getMinecraft();
				WorldClient world = mc.world;
				Vec3d offset = state.getOffset(world, pos);
				double x = pos.getX() + offset.x;
				double y = pos.getY() + offset.y + 0.9d;
				double z = pos.getZ() + offset.z;
				RenderingUtils.renderPlanarQuad(buffer, EnumFacing.UP, x, y, z, 0, ClientEventListener.GAS_COLOUR,
						mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/glass"), world, world.getCombinedLight(pos, 15), pos);
			}
		}
	}


}
