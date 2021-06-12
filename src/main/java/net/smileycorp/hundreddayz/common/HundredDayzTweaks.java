package net.smileycorp.hundreddayz.common;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import rafradek.TF2weapons.item.ItemWeapon;

@Mod(modid=ModDefinitions.modid, name = ModDefinitions.name, version = ModDefinitions.name, dependencies = ModDefinitions.dependencies)
public class HundredDayzTweaks {
	
	@SidedProxy(clientSide = ModDefinitions.client, serverSide = ModDefinitions.common)
	public static CommonProxy PROXY;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		PROXY.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		PROXY.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postInit(event);
	}
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		PROXY.serverStart(event);
	}
	
	//removes unnececary nbt from tf2 weapons to prevent a crash
	public static ItemStack cleanTFWeapon(ItemStack stack) {
		if (stack.getItem() instanceof ItemWeapon) {
			NBTTagCompound nbt = stack.getTagCompound();
			NBTTagCompound newNbt = new NBTTagCompound();
			if (nbt.hasKey("Type")) newNbt.setString("Type", nbt.getString("Type"));
			if (nbt.hasKey("Attributes")) newNbt.setTag("Attributes", nbt.getCompoundTag("Attributes"));
			stack = new ItemStack(stack.getItem(), 1, stack.getMetadata());
			stack.setTagCompound(newNbt);
		}
		return stack;
	}
}
