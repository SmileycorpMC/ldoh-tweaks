package net.smileycorp.ldoh.integration;

import com.Fishmod.mod_LavaCow.entities.EntitySkeletonKing;
import com.Fishmod.mod_LavaCow.entities.flying.EntityVespa;
import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

public class ModMixinLoader implements IFMLLoadingPlugin {
    
    public ModMixinLoader() {
        FermiumRegistryAPI.enqueueMixin(true, "modmixins.ldoh.json");
    }
    
    @Override
    public String[] getASMTransformerClass() {
        EntitySkeletonKing
        return new String[0];
    }
    
    @Override
    public String getModContainerClass() {
        return null;
    }
    
    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }
    
    @Override
    public void injectData(Map<String, Object> data) {}
    
    @Override
    public String getAccessTransformerClass() {
        return null;
    }
    
}
