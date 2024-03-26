package net.smileycorp.ldoh.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.ldoh.common.Constants;
import net.smileycorp.ldoh.common.block.BlockLandmine;

@SideOnly(Side.CLIENT)
public class StateMapperLandmine extends StateMapperBase {

    //register landmine blockstate models, as they have complicated behaviour
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        String statename = "primed=" + state.getValue(BlockLandmine.PRIMED).toString();
        return new ModelResourceLocation(Constants.loc("landmine"), statename);
    }

}
