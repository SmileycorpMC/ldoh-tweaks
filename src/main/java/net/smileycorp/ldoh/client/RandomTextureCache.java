package net.smileycorp.ldoh.client;

import mezz.jei.collect.ListMultiMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.function.Predicate;

//random mob textures
public class RandomTextureCache implements ISelectiveResourceReloadListener {

    public static final RandomTextureCache INSTANCE = new RandomTextureCache();

    //stores alternate textures for our random mobs implementation
    private ListMultiMap<ResourceLocation, ResourceLocation> CACHE = new ListMultiMap<>();

    private RandomTextureCache() {
    }

    //set up the texture if it doesn't exist in our cache
    private void init(ResourceLocation loc) {
        String domain = loc.getResourceDomain();
        //convert the added texture location to the expected mcpatcher location
        String path = loc.getResourcePath().replace("textures", "mcpatcher").replace("entity", "mob")
                .replace("entities", "mob").replace(".png", "");
        IResourceManager rm = Minecraft.getMinecraft().getResourceManager();
        int i = 1;
        while (true) {
            //check while textures exist in the right format
            try {
                //find and add texture to the cache
                ResourceLocation checkedTexture = new ResourceLocation(domain, path + (i == 1 ? "" : i) + ".png");
                rm.getResource(checkedTexture);
                CACHE.put(loc, checkedTexture);
                i++;
            } catch (Exception e) {
                //stop searching if the texture isn't found
                break;
            }
        }
    }

    //get texture from entity properties and base texture
    public ResourceLocation getLoc(ResourceLocation loc, Entity entity) {
        if (!CACHE.containsKey(loc)) init(loc);
        List<ResourceLocation> list = CACHE.get(loc);
        if (list.isEmpty()) return loc;
            //follow optifine's implementation of converting entity id to texture id
        else return list.get((int) (entity.getUniqueID().getLeastSignificantBits() & 0x7FFFFFFFL) % list.size());
    }

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager, Predicate<IResourceType> predicate) {
        if (predicate.test(VanillaResourceType.TEXTURES)) {
            //reload all textures in the cache
            //(no point clearing them if we're likely just going to end up loading them again when we start rendering)
            for (ResourceLocation loc : CACHE.toImmutable().keySet()) init(loc);
        }
    }
}
