package net.smileycorp.ldoh.common.world.gen;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.common.block.BlockBOPDirt;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.smileycorp.ldoh.common.world.gen.biome.BiomeFrozenWasteland;
import net.smileycorp.ldoh.common.world.gen.biome.BiomeInfernalWasteland;
import net.smileycorp.ldoh.common.world.gen.biome.BiomeLDOHWasteland;

@SuppressWarnings("unchecked")
public class LDOHWorld {

	//world type
	public static final WorldType LDOH_WASTELAND = new WorldTypeLDOH();

	//wasteland

	public static final Biome SILTY_WASTELAND = new BiomeLDOHWasteland("Silty_Wasteland", 0x6C7F51,
			BOPBlocks.dirt.getDefaultState().withProperty(BlockBOPDirt.VARIANT, BlockBOPDirt.BOPDirtType.SILTY));

	public static final Biome LOAMY_WASTELAND = new BiomeLDOHWasteland("Loamy_Wasteland", 0x5F712E,
			BOPBlocks.dirt.getDefaultState().withProperty(BlockBOPDirt.VARIANT, BlockBOPDirt.BOPDirtType.LOAMY));

	public static final Biome SANDY_WASTELAND = new BiomeLDOHWasteland("Sandy_Wasteland", 0xBFB755,
			BOPBlocks.dirt.getDefaultState().withProperty(BlockBOPDirt.VARIANT, BlockBOPDirt.BOPDirtType.SANDY));

	//public static final Biome MUDDY_WASTELAND = new BiomeLDOHWasteland("Muddy_Wasteland", 0x6A7039, BOPBlocks.mud.getDefaultState());

	//badlands

	public static final Biome FROZEN_WASTELAND = new BiomeFrozenWasteland();

	public static final Biome INFERNAL_WASTELAND = new BiomeInfernalWasteland();

}
