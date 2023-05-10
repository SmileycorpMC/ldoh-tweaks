package net.smileycorp.ldoh.client;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.smileycorp.atlas.api.client.RenderingUtils;
import net.smileycorp.ldoh.common.block.BlockBarbedWire;

public class BakedModelBarbedWire extends BakedModelWrapper<IBakedModel> {

	private final IModel base;

	private final ImmutableMap<String, String> enchanted_texturemap = ImmutableMap.<String, String>builder().put("texture", "misc/enchanted_item_glint").build();

	public BakedModelBarbedWire(IBakedModel originalModel, IModel base) {
		super(originalModel);
		this.base = base;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing facing, long rand) {
		List<BakedQuad> quads = originalModel.getQuads(state, facing, rand);
		System.out.println(base.getTextures());
		try {
			if (state instanceof IExtendedBlockState && ((IExtendedBlockState) state).getUnlistedNames().contains(BlockBarbedWire.IS_ENCHANTED)) {
				if (((IExtendedBlockState) state).getValue(BlockBarbedWire.IS_ENCHANTED)) {
					IModel newModel = base.retexture(enchanted_texturemap);
					quads.addAll(newModel.bake(newModel.getDefaultState(), DefaultVertexFormats.BLOCK, RenderingUtils.defaultTextureGetter).getQuads(state, facing, rand));

					System.out.println(newModel.getTextures());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quads;
	}

}
