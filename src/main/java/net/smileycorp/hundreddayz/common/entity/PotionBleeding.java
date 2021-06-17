package net.smileycorp.hundreddayz.common.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.hundreddayz.common.ModContent;
import net.smileycorp.hundreddayz.common.ModDefinitions;

public class PotionBleeding extends Potion {
	
	public static ResourceLocation texture = new ResourceLocation(ModDefinitions.modid, "textures/gui/potions.png");
	
	public PotionBleeding() {
		super(true, 0x960100);
		String name = "Bleed";
		setPotionName("effect." + ModDefinitions.getName(name));
		setRegistryName(ModDefinitions.getResource(name));
		setIconIndex(0, 0);
	}
	
	@Override
    public boolean shouldRender(PotionEffect effect) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        return super.getStatusIconIndex();
    }
    
    @Override
	public void performEffect(EntityLivingBase entity, int amp) {
		int t = Math.round(20/(amp + 1));
		if (entity.ticksExisted%t==0) {
			entity.attackEntityFrom(ModContent.BLEED_DAMAGE, 1f);
		}
    }

}
