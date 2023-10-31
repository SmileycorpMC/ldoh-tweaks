package net.smileycorp.ldoh.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.ldoh.common.damage.DamageSourceToxicGas;
import net.smileycorp.ldoh.common.item.LDOHItems;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Mod(modid = ModDefinitions.MODID, name = ModDefinitions.NAME, version = ModDefinitions.NAME, dependencies = ModDefinitions.DEPENDENCIES)
public class LDOHTweaks {

    @Instance(ModDefinitions.MODID)
    public static LDOHTweaks INSTANCE;

    @SidedProxy(clientSide = ModDefinitions.CLIENT, serverSide = ModDefinitions.COMMON)
    public static CommonProxy PROXY;

    public static final DamageSource TOXIC_GAS_DAMAGE = new DamageSourceToxicGas();
    public static final DamageSource SHRAPNEL_DAMAGE = new DamageSource("Shrapnel");

    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(ModDefinitions.getName("HundredDayzTab")) {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(LDOHItems.SYRINGE, 1, 3);
        }
    };

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
}
